package com.chat.server.dao.impl;

import com.chat.server.dao.InvitationDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Invitation;
import org.springframework.stereotype.Repository;

/**
 * Created on 28.10.2015.
 */
@Repository
public class InvitationDaoImpl extends AbstractDao<Invitation> implements InvitationDao {
    public InvitationDaoImpl(){
        super();
        setClazz(Invitation.class);
    }

    //API
}
