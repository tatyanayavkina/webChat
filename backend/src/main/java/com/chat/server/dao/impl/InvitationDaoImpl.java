package com.chat.server.dao.impl;

import com.chat.server.dao.InvitationDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Invitation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
    @SuppressWarnings("unchecked")
    public List<Invitation> findAllByUserId(int userId){
        List<Invitation> invitations = new ArrayList<>();
        invitations = getCurrentSession()
                .createQuery("from Invitation where user_id=:user_id")
                .setParameter("user_id", userId)
                .list();

        return invitations;
    }
}
