package com.chat.server.controller;

import com.chat.server.controller.utils.DeferredUnreadMessages;
import com.chat.server.model.Message;
import com.chat.server.model.Role;
import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.oauth2.domain.UserResource;
import com.chat.server.oauth2.service.AccessService;
import com.chat.server.service.MessageService;
import com.chat.server.service.UserService;
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
    private final Map<DeferredUnreadMessages<HttpEntity<List<Message>>>, User> userRequests = new ConcurrentHashMap<DeferredUnreadMessages<HttpEntity<List<Message>>>, User>();

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;

    @RequestMapping(method = RequestMethod.POST)
    public  HttpEntity<Message> createMessage(@RequestBody Message message){
        message.setCreationTime( new Date() );
        messageService.create(message);
        if ( message != null ){
            new MessageExecutor().run();
            return new ResponseEntity(message, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

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

        final DeferredUnreadMessages<HttpEntity<List<Message>>> deferredResult = new DeferredUnreadMessages<>( 25 * 1000L, new ResponseEntity( Collections.emptyList(), HttpStatus.OK ), lastReadMessage, roomIds );
        userRequests.put( deferredResult, user );
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                userRequests.remove( deferredResult );
                user.setLastRequest( new Date() );
                userService.update(user);
                System.out.println("--complete unread messages " + user.getLastRequest());
            }
        });

        List<Message> messages = messageService.findUnreadMessages( lastReadMessage, roomIds );
        if( !messages.isEmpty() ){
            System.out.println("--unread NOT EMPTY ");
            user.setLastReadMessage( messages.get(0).getId() );
            deferredResult.setResult( new ResponseEntity( messages, HttpStatus.OK ) );
        }

        return deferredResult;
    }

    private class MessageExecutor implements Runnable{
        @Override
        public void run(){
            for (Map.Entry<DeferredUnreadMessages<HttpEntity<List<Message>>>, User> entry : userRequests.entrySet()) {
                List<Message> messages = messageService.findUnreadMessages( entry.getKey().getLastReadMessage(), entry.getKey().getRoomIds());
                if ( messages == null ){
                    messages = Collections.emptyList();
                } else {
                    User user = entry.getValue();
                    user.setLastReadMessage( messages.get(0).getId() );
                    userService.update( user );
                }
                entry.getKey().setResult( new ResponseEntity( messages, HttpStatus.OK ) );
            }
        }
    }
}
