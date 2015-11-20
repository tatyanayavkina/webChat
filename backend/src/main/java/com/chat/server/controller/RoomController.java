package com.chat.server.controller;

import com.chat.server.model.Role;
import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.service.RoomService;
import com.chat.server.service.UserService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created on 03.11.2015.
 */
@RestController
@RequestMapping(value = "/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    private static final Logger logger = Logger.getLogger(RoomController.class);

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Room>> getRooms(){
        List<Room> rooms = roomService.findAll();
        if (rooms != null){
            return new ResponseEntity( rooms, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Room> getRoom(@PathVariable("id") int id){
        Room room = roomService.findOne(id);
        if (room != null){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    // todo: проверить, что удаляет либо админ, либо владелец комнаты!
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<String> deleteRoom(@PathVariable("id") int id){
        Room room = roomService.findOne( id );
        if ( room != null ){
            roomService.delete( room );
            new ResponseEntity( HttpStatus.NO_CONTENT );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    @RolesAllowed({Role.USER})
    @RequestMapping(method = RequestMethod.POST)
    public synchronized HttpEntity<Room> createRoom(@RequestBody Room room){
        roomService.create( room );
        if ( room != null ){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    // todo: проверить, что в ответ уходит чисто комната без связок
    // todo: или сделать, чтобы было именно так
    @RequestMapping(value="/join/{id}", method = RequestMethod.POST)
    public HttpEntity<Room> joinRoom(@RequestParam("id") int roomId, @RequestBody User user){
        Room room = roomService.findOne( roomId );
        if( room.getType() == Room.CLOSE_TYPE ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        room.getUsers().add( user );
        room = roomService.update( room );

        if ( room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        return new ResponseEntity( room, HttpStatus.OK );
    }

}
