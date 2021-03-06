package com.chat.server.service.impl;

import com.chat.server.dao.InvitationDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Invitation;
import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.service.InvitationService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationServiceImpl extends AbstractService<Invitation> implements InvitationService {

    @Autowired
    private InvitationDao dao;

    public InvitationServiceImpl(){
        super();
    }

    //API
    @Transactional
    public Invitation createInvitation(User user, Room room){
        Invitation invitation = new Invitation();
        invitation.setUser(user);
        invitation.setRoom(room);
        create(invitation);

        return invitation;
    }

    @Transactional
    public List<Invitation> findAllByUserId(int userId){
        return dao.findAllByUserId(userId);
    }

    @Transactional
    public List<User> findAlreadyInvitedUsers(int roomId, List<User> users){
        List<Integer> userIds = new ArrayList<>();
        for( User user: users ){
            userIds.add( user.getId() );
        }

        List<Invitation> invitations = dao.findAllByRoomIdAndUsers( roomId, userIds );
        List<User> alreadyInvitedUsers = new ArrayList<>();

        for( Invitation invitation: invitations){
            User user = invitation.getUser();
            if( users.contains( user ) ){
                alreadyInvitedUsers.add( user );
            }
        }

        return alreadyInvitedUsers;
    }

    @Override
    protected IOperations<Invitation> getDao() {
        return dao;
    }
}
