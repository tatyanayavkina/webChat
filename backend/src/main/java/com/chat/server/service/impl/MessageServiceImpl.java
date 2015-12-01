package com.chat.server.service.impl;

import com.chat.server.dao.MessageDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Message;
import com.chat.server.service.MessageService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 28.10.2015.
 */
@Service
public class MessageServiceImpl extends AbstractService<Message> implements MessageService {

    @Autowired
    private MessageDao dao;

    public MessageServiceImpl(){
        super();
    }

    @Override
    protected IOperations<Message> getDao() {
        return dao;
    }
}
