package com.chat.server.dao.impl;

import com.chat.server.dao.MessageDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
    public  List<Message> findUnreadMessages(int lastReadMessage, List<Integer> roomIds){
        List<Message> messages = new ArrayList<>();
        messages = getCurrentSession()
                .createQuery("from Message where id>:last_read_message and room_id in (:room_id) order by id desc")
                .setParameter("last_read_message", lastReadMessage)
                .setParameterList("room_id", roomIds)
                .list();

        return messages;
    }
}
