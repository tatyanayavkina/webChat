package com.chat.server.dao.impl;

import com.chat.server.dao.MessageDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Message;
import org.springframework.stereotype.Repository;

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
}
