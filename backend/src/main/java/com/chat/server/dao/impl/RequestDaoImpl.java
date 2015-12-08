package com.chat.server.dao.impl;

import com.chat.server.dao.RequestDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Request;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28.10.2015.
 */
@Repository
public class RequestDaoImpl extends AbstractDao<Request> implements RequestDao {
    public RequestDaoImpl(){
        super();
        setClazz(Request.class);
    }

    //API
    @SuppressWarnings("unchecked")
    public List<Request> findAllByRoomId(int roomId){
        List<Request> requests = new ArrayList<>();
        requests = getCurrentSession()
                .createQuery("from Request where room_id=:room_id")
                .setParameter("room_id", roomId)
                .list();
        return requests;
    }

    @SuppressWarnings("unchecked")
    public void deleteByUserIds(List<Integer> userIds){
         getCurrentSession()
        .createQuery("delete from Request where user_id in (:user_ids)")
        .setParameterList("user_ids", userIds)
        .executeUpdate();
    }
}
