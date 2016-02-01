package com.chat.server.controller;

import com.chat.server.controller.utils.InvitationsInfo;
import com.chat.server.exception.AlreadyExistsException;
import com.chat.server.model.*;
import com.chat.server.oauth2.domain.UserResource;
import com.chat.server.oauth2.service.AccessService;
import com.chat.server.service.InvitationService;
import com.chat.server.service.RoomService;
import com.chat.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;


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
    @Autowired
    private AccessService accessService;

    /**
     * Create invitations to join the room. Only room owner can do this request
     * @param roomId
     * @param logins
     * @return HttpEntity<String> - HttpStatus.OK if all is ok
     */
    @RequestMapping(value = "/send/{roomId}", method = RequestMethod.POST)
    public HttpEntity<InvitationsInfo> send(@PathVariable("roomId") int roomId, @RequestBody List<String> logins){
        Room room = roomService.findOne( roomId );
        if ( room != null && room.getType() == Room.CLOSE_TYPE ){
            UserResource currentUser = accessService.getCurrentUser();
            if ( currentUser == null || currentUser.getId() != room.getOwner().getId() ){
                return new ResponseEntity( HttpStatus.FORBIDDEN );
            }

            List<User> users = userService.findUsersByLogin(logins);
            List<User> alreadyInRoomUsers = roomService.findAlreadyInRoomUsers(roomId, users);
            List<User> alreadyInvitedUsers = invitationService.findAlreadyInvitedUsers( roomId, users );
            List<User> invitedUsers = new ArrayList<>();
            for( User user: users ){
                if ( !alreadyInRoomUsers.contains( user ) && ! alreadyInvitedUsers.contains( user )){
                    invitationService.createInvitation( user, room );
                    invitedUsers.add( user );
                }
            }

            InvitationsInfo invitationsInfo = new InvitationsInfo();
            invitationsInfo.setAlreadyInRoomUsers( alreadyInRoomUsers );
            invitationsInfo.setAlreadyInvitedUsers(alreadyInvitedUsers);
            invitationsInfo.setInvitedUsers(invitedUsers);
            return new ResponseEntity( invitationsInfo, HttpStatus.OK );

        }

        return new ResponseEntity( HttpStatus.BAD_REQUEST );
    }

    /**
     * User accepts invitation to join the room
     * @param id
     * @return HttpEntity<Room> - room if all is ok
     */
    @RequestMapping(value = "/{id}/accept", method = RequestMethod.POST)
    public HttpEntity<Room> accept(@PathVariable("id") int id) throws AlreadyExistsException{
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

    /**
     * Find all invitations that send to user. User can request only its own invitations
     * @param userId
     * @return HttpEntity<List<Invitation>> - list of invitations
     */
    @RequestMapping(value = "/byUserId/{userId}", method = RequestMethod.GET)
    public HttpEntity<List<Invitation>> findAllByUserId(@PathVariable("userId") int userId){
        UserResource currentUser = accessService.getCurrentUser();
        if( currentUser == null || currentUser.getId() != userId ){
            return new ResponseEntity( HttpStatus.FORBIDDEN );
        }

        List<Invitation> invitations = invitationService.findAllByUserId( userId );
        return new ResponseEntity( invitations, HttpStatus.OK );
    }
}
