package com.chat.server.dao.impl;

import com.chat.server.dao.InvitationDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Invitation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressWarnings("unchecked")
    public List<Invitation> findAllByRoomIdAndUsers(int roomId, List<Integer> userIds){
        List<Invitation> invitations = new ArrayList<>();
        invitations = getCurrentSession()
                .createQuery("from Invitation where room_id=:room_id and user_id in (:user_ids)")
                .setParameter("room_id", roomId)
                .setParameterList("user_ids", userIds)
                .list();

        return invitations;
    }
}
