package com.chat.server.controller;

import com.chat.server.model.Invitation;
import com.chat.server.model.Role;
import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.service.InvitationService;
import com.chat.server.service.RoomService;
import com.chat.server.service.UserService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;


/**
 * Created on 03.11.2015.
 */
@RestController
@RolesAllowed({Role.USER})
@RequestMapping(value = "/api/invitations")
public class InvitationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private InvitationService invitationService;

    private static final Logger logger = Logger.getLogger(InvitationController.class);

    //todo: спросить у Димы, как поступать, если не все приглашения получилось создать
    //todo: узнать, нужно ли отправлять на клиент созданные приглашения в ответ?
    //todo: сделать проверку на то, что приглашения рассылает владелец комнаты
    //todo: что делать, если пользователи не найдены...
    /**
     * Create invitations to join the room
     * @param roomId
     * @param logins
     * @return HttpEntity<String> - HttpStatus.OK if all is ok
     */
    @RequestMapping(value = "/send/{roomId}", method = RequestMethod.POST)
    public HttpEntity<String> send(@PathVariable("roomId") int roomId, @RequestBody List<String> logins){
        List<Invitation> invitations = new ArrayList<Invitation>();
        List<User> users = userService.findUsersByLogin( logins );
        Room room = roomService.findOne( roomId );
        if ( room == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        for( User user: users ){
            Invitation invitation = invitationService.createInvitation( user, room );
            invitations.add( invitation );
        }
        if( invitations.size() == logins.size() ){
            return new ResponseEntity( HttpStatus.OK );
        } else {
            return new ResponseEntity( HttpStatus.NO_CONTENT );
        }

    }

    /**
     * User accepts invitation to join the room
     * @param id
     * @return HttpEntity<Room> - room if all is ok
     */
    @RequestMapping(value = "/{id}/accept", method = RequestMethod.POST)
    public HttpEntity<Room> accept(@PathVariable("id") int id){
        Invitation invitation = invitationService.findOne(id);
        if ( invitation == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

        User user = invitation.getUser();
        Room room = invitation.getRoom();
        room = roomService.joinRoom( room.getId(), user );
        invitationService.delete( invitation );

        return new ResponseEntity( room, HttpStatus.OK );
    }

    /**
     * User rejects invitation to join the room
     * @param id
     * @return HttpEntity<String> - HttpStatus.NO_CONTENT if all is ok
     */
    @RequestMapping(value = "/{id}/reject", method = RequestMethod.POST)
    public HttpEntity<String> reject(@PathVariable("id") int id){
        Invitation invitation = invitationService.findOne(id);
        if ( invitation == null ){
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
        invitationService.delete( invitation );

        return new ResponseEntity( HttpStatus.NO_CONTENT );
    }
}
