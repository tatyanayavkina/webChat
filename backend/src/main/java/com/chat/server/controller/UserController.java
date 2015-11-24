package com.chat.server.controller;

import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.service.RoomService;
import com.chat.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 26.10.2015.
 */
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<User>> getUsers(){
        List<User> users = userService.findAll();
        if (users != null){
            return new ResponseEntity(users, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<User> getUser(@PathVariable("id") int id){
        User user = userService.findOne(id);
        if (user != null){
            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // Метод - вступление в открытую комнату
    @RequestMapping(value="/{id}/join", method = RequestMethod.POST)
    public HttpEntity<Room> joinRoom(@PathVariable("id") int userId , @RequestBody int roomId){
        Room room = roomService.findOne(roomId);
        User user = userService.findOne( userId );
        if( user == null || room == null || room.getType() == Room.CLOSE_TYPE ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        user.getRooms().add( room );
        userService.update(user);

        if ( room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        room.getOwner();
        return new ResponseEntity( room, HttpStatus.OK );
    }

    // Метод - выход из комнаты
    @RequestMapping(value="/{id}/leave", method = RequestMethod.POST)
    public HttpEntity<Room> leaveRoom(@PathVariable("id") int userId, @RequestBody int roomId){
        Room room = roomService.findOne( roomId );
        User user = userService.findOne( userId );
        if( user == null || room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        boolean contained = user.getRooms().remove(room);
        if ( contained ){
            userService.update( user );
            return new ResponseEntity( HttpStatus.NO_CONTENT );

        } else {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }
}
