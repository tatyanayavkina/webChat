package com.chat.server.controller;

import com.chat.server.controller.utils.DeferredUnreadMessages;
import com.chat.server.model.*;
import com.chat.server.oauth2.domain.UserResource;
import com.chat.server.oauth2.service.AccessService;
import com.chat.server.service.MessageService;
import com.chat.server.service.RequestService;
import com.chat.server.service.UserService;
import com.chat.server.utils.SingleThreadTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 03.11.2015.
 */
@RestController
@RolesAllowed({Role.GUEST, Role.USER})
@RequestMapping(value = "/api/messages")
public class MessageController {
    private final int LAST_COUNT = 10;
    private final int REQUEST_TIMEOUT = 180;

    private final Map<Integer,DeferredUnreadMessages<HttpEntity<List<Message>>>> userRequests = new ConcurrentHashMap<>();

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private AccessService accessService;

    /**
     * Save message send by user
     * @param message
     * @return HttpEntity<Message>
     */
    @RequestMapping(method = RequestMethod.POST)
    public  HttpEntity<Message> createMessage(@RequestBody Message message){
        message.setCreationTime( new Date() );
        messageService.create(message);
        List<Request> requests = requestService.findAllByRoomId( message.getRoom().getId() );
        if ( requests != null ){
            requestService.deleteByUserIds(requests);
            SingleThreadTaskExecutor.getInstance().add( new MessageTask(message, requests) );
        }

        return new ResponseEntity(message, HttpStatus.OK);
    }

    /**
     * Get several last messages from one room
     * @param roomId
     * @return HttpEntity<List<Message>> - list of messages if they exist
     */
    @RequestMapping(value="/last/{roomId}", method = RequestMethod.GET)
    public HttpEntity<List<Message>> getSomeLastMessages(@PathVariable("roomId") int roomId){
        // ищем текущего пользователя и проставляем ему время запроса сообщений
        UserResource userResource = accessService.getCurrentUser();
        User user = userService.findOne(userResource.getId());
        user.setLastRequest(new Date());
        List<Message> messages = messageService.findLastMessages( roomId, LAST_COUNT );
        if ( messages.size() > 0 ){
            int userLastReadMessage = user.getLastReadMessage();
            int messagesMaxId = messages.get(0).getId();
            user.setLastReadMessage( Integer.max( userLastReadMessage, messagesMaxId ) );
        }
        userService.update(user);
        return new ResponseEntity(messages, HttpStatus.OK);
    }

    /**
     * Find unread messages for user
     * @return DeferredUnreadMessages<HttpEntity<List<Message>>> - promise that resolves when
     * messages are found in database or timeout is expired
     */
    @RequestMapping(value="/unread", method = RequestMethod.GET)
    public DeferredUnreadMessages<HttpEntity<List<Message>>> getUnreadMessages(){
        UserResource userResource = accessService.getCurrentUser();
        final User user = userService.findUserWithRooms(userResource.getId());
        int lastReadMessage = user.getLastReadMessage();
        System.out.println("--unread messages "+ user.getLogin());
        System.out.println("--unread messages "+ user.getLastReadMessage());
        List<Integer> roomIds = new ArrayList<>();
        for( Room room: user.getRooms() ){
            roomIds.add( room.getId() );
        }

        final DeferredUnreadMessages<HttpEntity<List<Message>>> deferredResult = new DeferredUnreadMessages<>( REQUEST_TIMEOUT * 1000L, new ResponseEntity( Collections.emptyList(), HttpStatus.OK ), user, roomIds );
        userRequests.put( user.getId(), deferredResult );
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                userRequests.remove( user.getId() );
                user.setLastRequest( new Date() );
                userService.update(user);
                System.out.println("--complete unread messages " + user.getLastRequest());
            }
        });

        List<Message> messages = messageService.findUnreadMessages( lastReadMessage, roomIds );
        if( messages.isEmpty() ){
            requestService.add(user.getId(), roomIds);
        } else {
            System.out.println("--unread NOT EMPTY ");
            requestService.deleteByUserId(user.getId());
            user.setLastReadMessage( messages.get(0).getId() );
            deferredResult.setResult( new ResponseEntity( messages, HttpStatus.OK ) );
        }

        return deferredResult;
    }

    private class MessageTask implements Runnable{
        private Message message;
        private List<Request> requests;

        public MessageTask(Message message, List<Request> requests){
            this.message = message;
            this.requests = requests;
        }

        @Override
        public void run(){
            List<Message> messages = new ArrayList<>();
            messages.add( message );

            for( Request request: requests){
                DeferredUnreadMessages<HttpEntity<List<Message>>> deferred = userRequests.get(request.getUserId());
                if ( deferred != null ){
                    User user = deferred.getUser();
                    user.setLastReadMessage( messages.get(0).getId() );
                    userService.update( user );
                    deferred.setResult( new ResponseEntity( messages, HttpStatus.OK) );
                }
            }
        }
    }
}
