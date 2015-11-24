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
@RolesAllowed({Role.USER})
@RequestMapping(value = "/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;

    private static final Logger logger = Logger.getLogger(RoomController.class);

    // ����� - ���������� ��� �������
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Room>> getRooms(){
        List<Room> rooms = roomService.findAll();
        if (rooms != null){
            return new ResponseEntity( rooms, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    // ����� - ���������� ������ �������� �������
    @RolesAllowed({Role.GUEST, Role.USER})
    @RequestMapping(value="/open", method = RequestMethod.GET)
    public HttpEntity<List<Room>> getOpenRooms(){
        List<Room> rooms = roomService.findByType(Room.OPEN_TYPE);
        return new ResponseEntity( rooms, HttpStatus.OK );
    }

    //����� - ���������� ��� ������� ������������
    @RequestMapping(value="/byUserId/{userId}", method = RequestMethod.GET)
    public HttpEntity<List<Room>> getRoomsByUserId(@PathVariable("userId") int userId){
        User user = userService.findOne(userId);
        if ( user == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        List<Room> rooms = user.getRooms();
        for(Room room: rooms){
            room.getOwner();
        }
        return new ResponseEntity( rooms, HttpStatus.OK );

    }

    // ����� -  ����� ������� �� id + ������ �� ����������
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Room> getRoom(@PathVariable("id") int id){
        Room room = roomService.findOne(id);
        if (room != null){
            room.getUsers();
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }

    //todo: ������, ��� ��������� ������ ���������� ������ �� id � ��� put �������...
    // ����� -  ��������� ������ � �������
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity<Room> updateRoom(@RequestBody Room room){
       roomService.update( room );
        if (room != null){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    // todo: ���������, ��� ������� ���� �����, ���� �������� �������!
    //����� - �������� �������
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public HttpEntity<String> deleteRoom(@PathVariable("id") int id){
        Room room = roomService.findOne( id );
        if ( room != null ){
            roomService.delete( room );
            new ResponseEntity( HttpStatus.NO_CONTENT );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    // ����� - �������� �������
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Room> createRoom(@RequestBody Room room){
        roomService.create(room);
        if ( room != null ){
            return new ResponseEntity( room, HttpStatus.OK );
        }
        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }


    // ����� - ���������� � �������� �������
    @RequestMapping(value="/join/{id}", method = RequestMethod.POST)
    public HttpEntity<Room> joinRoom(@PathVariable("id") int roomId , @RequestBody int userId){
        Room room = roomService.findOne(roomId);
        User user = userService.findOne( userId );
        if( user == null || room == null || room.getType() == Room.CLOSE_TYPE ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        room.getUsers().add( user );
        roomService.update( room );

        if ( room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        room.getOwner();
        return new ResponseEntity( room, HttpStatus.OK );
    }

    // ����� - ����� �� �������
    @RequestMapping(value="/leave/{id}", method = RequestMethod.POST)
    public HttpEntity<Room> leaveRoom(@PathVariable("id") int roomId , @RequestBody int userId){
        Room room = roomService.findOne( roomId );
        User user = userService.findOne( userId );
        if( user == null || room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        boolean contained = room.getUsers().remove( user );
        if ( contained ){
            roomService.update( room );
            return new ResponseEntity( HttpStatus.NO_CONTENT );

        } else {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

}
