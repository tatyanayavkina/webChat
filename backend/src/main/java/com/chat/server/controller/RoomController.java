package com.chat.server.controller;

import com.chat.server.exception.AlreadyExistsException;
import com.chat.server.exception.ObjectNotFoundException;
import com.chat.server.model.Role;
import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.oauth2.domain.UserResource;
import com.chat.server.oauth2.service.AccessService;
import com.chat.server.service.RoomService;
import com.chat.server.service.UserService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.annotation.security.RolesAllowed;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private AccessService accessService;

    private static final Logger logger = Logger.getLogger(RoomController.class);

    /**
     * Get rooms
     * @return HttpEntity<List<Room>> - all rooms that are in system
     */
    @RolesAllowed({Role.ADMIN})
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
        if (rooms != null){
            return new ResponseEntity( rooms, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    /**
     * Get rooms by userId. Only room owner can do this request.
     * @param userId
     * @return HttpEntity<List<Room>> - all rooms that user takes part in
     */
    @RequestMapping(value="/byUserId/{userId}", method = RequestMethod.GET)
    public HttpEntity<List<Room>> getRoomsByUserId(@PathVariable("userId") int userId) throws ObjectNotFoundException{
        UserResource currentUser = accessService.getCurrentUser();
        if( currentUser == null || currentUser.getId() != userId ){
            return new ResponseEntity( HttpStatus.FORBIDDEN );
        }

        List<Room> rooms = userService.findRoomsWithOwnersByUserId(userId);
        return new ResponseEntity( rooms , HttpStatus.OK );
    }

    /**
     * Get room by its id. Only room owner can do this request.
     * @param id
     * @return HttpEntity<Room> - room
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Room> getRoom(@PathVariable("id") int id){
        Room room = roomService.findOne(id);
        if (room != null){
            UserResource currentUser = accessService.getCurrentUser();
            if( currentUser == null || currentUser.getId() != room.getOwner().getId() ){
                return new ResponseEntity( HttpStatus.FORBIDDEN );
            }

            return new ResponseEntity( room, HttpStatus.OK );
        }

        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    /**
     * Get room's users by its id. Only room owner can do this request.
     * @param id
     * @return HttpEntity<List<User>> - list of users
     */
    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    public HttpEntity<List<User>> getRoomUsers(@PathVariable("id") int id) throws ObjectNotFoundException{
        Room room = roomService.findOne( id );
        if( room != null ){
            UserResource currentUser = accessService.getCurrentUser();
            if( currentUser == null || currentUser.getId() != room.getOwner().getId() ){
                return new ResponseEntity( HttpStatus.FORBIDDEN );
            }

            List<User> users = roomService.getRoomUsers( id );
            return new ResponseEntity( users, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    //todo: протестировать
    /**
     * Update room. Only room owner can do this request.
     * @param room
     * @return HttpEntity<Room> - updated room
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity<Room> updateRoom(@RequestBody Room room){
        UserResource currentUser = accessService.getCurrentUser();
        if( currentUser == null || currentUser.getId() != room.getOwner().getId() ){
            return new ResponseEntity( HttpStatus.FORBIDDEN );
        }

        roomService.update( room );
        if (room != null){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    /**
     * Delete room. Only room owner can do this request.
     * @param id
     * @return HttpEntity<String> - if all is ok then HttpStatus.NO_CONTENT else HttpStatus.BAD_REQUEST
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<String> deleteRoom(@PathVariable("id") int id){
        Room room = roomService.findOne( id );
        if ( room != null ){
            UserResource currentUser = accessService.getCurrentUser();
            if( currentUser == null || currentUser.getId() != room.getOwner().getId() ){
                return new ResponseEntity( HttpStatus.FORBIDDEN );
            }

            roomService.delete( room );
            return new ResponseEntity( HttpStatus.NO_CONTENT );
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
    public HttpEntity<Room> joinRoom(@PathVariable("id") int roomId , @RequestBody int userId) throws AlreadyExistsException {
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
    public HttpEntity<Room> leaveRoom(@PathVariable("id") int roomId , @RequestBody int userId) throws ObjectNotFoundException{
        User user = userService.findOne(userId);
        roomService.removeUserFromRoom( roomId, user );
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    //todo: протестировать
    /**
     * Remove a list of users from room's users. Only room owner can do this request.
     * @param roomId
     * @param usersToRemove
     * @return HttpEntity<Room> - HttpStatus.NO_CONTENT when all is  ok
     */
    @RequestMapping(value="/removeUsers/{id}", method = RequestMethod.POST)
    public HttpEntity<Room> removeUsers(@PathVariable("id") int roomId, @RequestBody List<User> usersToRemove) throws ObjectNotFoundException{
        Room room = roomService.findOne(roomId);
        if( room != null ){
            UserResource currentUser = accessService.getCurrentUser();
            if( currentUser == null || currentUser.getId() != room.getOwner().getId() ){
                return new ResponseEntity( HttpStatus.FORBIDDEN );
            }
            roomService.removeUsersFromRoom(roomId, usersToRemove);
            return new ResponseEntity( HttpStatus.NO_CONTENT );
        }

        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

}
