package com.chat.server.controller;

import com.chat.server.model.Message;
import com.chat.server.model.Role;
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
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created on 03.11.2015.
 */
@RestController
@RolesAllowed({Role.GUEST})
@RequestMapping(value = "/api/messages")
public class MessageController {
    private final int LAST_COUNT = 10;

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
            return new ResponseEntity(message, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value="/last/{roomId}", method = RequestMethod.GET)
    public HttpEntity<List<Message>> getSomeLastMessages(@PathVariable("roomId") int roomId){
        // ищем текущего пользователя и проставляем ему время запроса сообщений
        UserResource userResource = accessService.getCurrentUser();
        User user = userService.findOne( userResource.getId() );
        user.setLastRequest( new Date() );
        userService.update(user);
        List<Message> messages = messageService.findLastMessages( roomId, LAST_COUNT );
        return new ResponseEntity(messages, HttpStatus.OK);
    }

    @RequestMapping(value="/unread", method = RequestMethod.GET)
    public HttpEntity<DeferredResult<List<Message>>> getUnreadMessages(){
        UserResource userResource = accessService.getCurrentUser();

        final DeferredResult<List<Message>> deferredResult = new DeferredResult<List<Message>>(null, Collections.emptyList());
//        List<Message> messages = messageService.findUnreadMessages();

    }
}
