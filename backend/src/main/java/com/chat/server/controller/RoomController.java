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

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created on 03.11.2015.
 */
@RestController
@RolesAllowed({Role.USER})
@RequestMapping(value = "/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    private static final Logger logger = Logger.getLogger(RoomController.class);

    /**
     * Get rooms
     * @return HttpEntity<List<Room>> - all rooms that are in system
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Room>> getRooms(){
        List<Room> rooms = roomService.findAll();
        if (rooms != null){
            return new ResponseEntity( rooms, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    /**
     * Get rooms with type=Room.OPEN_TYPE
     * @return HttpEntity<List<Room>> - open rooms
     */
    @RolesAllowed({Role.GUEST, Role.USER})
    @RequestMapping(value="/open", method = RequestMethod.GET)
    public HttpEntity<List<Room>> getOpenRooms(){
        List<Room> rooms = roomService.findByType(Room.OPEN_TYPE);
        return new ResponseEntity( rooms, HttpStatus.OK );
    }

    /**
     * Get rooms by userId
     * @param userId
     * @return HttpEntity<List<Room>> - all rooms that user takes part in
     */
    @RequestMapping(value="/byUserId/{userId}", method = RequestMethod.GET)
    public HttpEntity<List<Room>> getRoomsByUserId(@PathVariable("userId") int userId){
//        User user = userService.findOne(userId);
//        if ( user == null ){
//            return new ResponseEntity( HttpStatus.BAD_REQUEST );
//        }
//
//        List<Room> rooms = user.getRooms();
//        for(Room room: rooms){
//            room.getOwner();
//        }
//        return new ResponseEntity( rooms, HttpStatus.OK );
        List<Room> rooms = userService.findRoomsWithOwnersByUserId(userId);
        return new ResponseEntity( rooms , HttpStatus.OK );
    }

    /**
     * Get room by its id
     * @param id
     * @return HttpEntity<Room> - room
     */
    // todo: проверить, что комнату запрашивает ее владелец
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Room> getRoom(@PathVariable("id") int id){
        Room room = roomService.findOne(id);
        if (room != null){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    /**
     * Get room's users by its id
     * @param id
     * @return HttpEntity<List<User>> - list of users
     */
    // todo: проверить, что пользователей запрашивает владелец комнаты
    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    public HttpEntity<List<User>> getRoomUsers(@PathVariable("id") int id){
        List<User> users = roomService.getRoomUsers( id );
        if ( users == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        return new ResponseEntity( users, HttpStatus.OK );
    }

    /**
     * Update room
     * @param room
     * @return HttpEntity<Room> - updated room
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity<Room> updateRoom(@RequestBody Room room){
       roomService.update( room );
        if (room != null){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    /**
     * Delete room
     * @param id
     * @return HttpEntity<String> - if all is ok then HttpStatus.NO_CONTENT else HttpStatus.BAD_REQUEST
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<String> deleteRoom(@PathVariable("id") int id){
        Room room = roomService.findOne( id );
        if ( room != null ){
            roomService.delete( room );
            new ResponseEntity( HttpStatus.NO_CONTENT );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    /**
     * Create new room
     * @param room
     * @return HttpEntity<Room> - new room
     */
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Room> createRoom(@RequestBody Room room){
        roomService.create(room);
        if ( room != null ){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }


    /**
     * Add user to room's users
     * @param roomId
     * @param userId
     * @return HttpEntity<Room> - room that user joined to
     */
    @RequestMapping(value="/join/{id}", method = RequestMethod.POST)
    public HttpEntity<Room> joinRoom(@PathVariable("id") int roomId , @RequestBody int userId){
//        Room room = roomService.findOne(roomId);
//        User user = userService.findOne( userId );
//        if( user == null || room == null || room.getType() == Room.CLOSE_TYPE ){
//            return new ResponseEntity( HttpStatus.BAD_REQUEST );
//        }
//        room.getUsers().add( user );
//        roomService.update(room);
//
//        if ( room == null ){
//            return new ResponseEntity( HttpStatus.BAD_REQUEST );
//        }
//        room.getOwner();
//        return new ResponseEntity( room, HttpStatus.OK );
        Room room = roomService.findOne( roomId );
        User user = userService.findOne( userId );
        if( user == null || room == null || room.getType() == Room.CLOSE_TYPE ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        room = roomService.joinRoom( roomId, user);
        return new ResponseEntity( room, HttpStatus.OK );
    }

    /**
     * Remove user from room's users
     * @param roomId
     * @param userId
     * @return HttpEntity<Room> - HttpStatus.NO_CONTENT when all is  ok
     */
    @RequestMapping(value="/leave/{id}", method = RequestMethod.POST)
    public HttpEntity<Room> leaveRoom(@PathVariable("id") int roomId , @RequestBody int userId){
        User user = userService.findOne( userId );
        if( user == null){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        Room room = roomService.removeUserFromRoom( roomId, user );
        if ( room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    /**
     * Remove a list of users from room's users
     * @param roomId
     * @param usersToRemove
     * @return HttpEntity<Room> - HttpStatus.NO_CONTENT when all is  ok
     */
    @RequestMapping(value="/removeUsers/{id}", method = RequestMethod.POST)
    public HttpEntity<Room> removeUsers(@PathVariable("id") int roomId, @RequestBody List<User> usersToRemove){
        Room room = roomService.removeUsersFromRoom( roomId, usersToRemove );
        if ( room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

}
