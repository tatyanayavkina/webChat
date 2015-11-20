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

/**
 * Created on 28.10.2015.
 */
@Service
public class InvitationServiceImpl extends AbstractService<Invitation> implements InvitationService {

    @Autowired
    private InvitationDao dao;

    public InvitationServiceImpl(){
        super();
    }

    //API
    public Invitation createInvitation(User user, Room room, int type){
        Invitation invitation = new Invitation();
        invitation.setType(type);
        invitation.setUser(user);
        invitation.setRoom(room);
        create(invitation);

        return invitation;
    }

    @Override
    protected IOperations<Invitation> getDao() {
        return dao;
    }
}
