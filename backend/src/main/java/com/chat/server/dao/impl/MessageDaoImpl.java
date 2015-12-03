package com.chat.server.dao.impl;

import com.chat.server.dao.MessageDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 28.10.2015.
 */
@Repository
public class MessageDaoImpl extends AbstractDao<Message> implements MessageDao {
    public MessageDaoImpl(){
        super();
        setClazz(Message.class);
    }

    //API
    @SuppressWarnings("unchecked")
    public List<Message> findLastMessages(int roomId, int count){
        List<Message> messages = new ArrayList<>();
        messages = getCurrentSession()
                .createQuery("from Message where room_id=:room_id order by id desc")
                .setParameter("room_id", roomId)
                .setMaxResults(count)
                .list();

        return messages;

    }

    @SuppressWarnings("unchecked")
    public  List<Message> findUnreadMessages(Date lastRequest, List<Integer> roomIds){
        List<Message> messages = new ArrayList<>();
        messages = getCurrentSession()
                .createQuery("from Message where creation_time>=:last_request and room_id in (:room_id)")
                .setParameter("last_request", lastRequest)
                .setParameterList("room_id", roomIds)
                .list();

        return messages;
    }
}
