package com.chat.server.service.impl;

import com.chat.server.dao.MessageDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Message;
import com.chat.server.service.MessageService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MessageServiceImpl extends AbstractService<Message> implements MessageService {

    @Autowired
    private MessageDao dao;

    public MessageServiceImpl(){
        super();
    }

    //API
    @Transactional
    public List<Message> findLastMessages(int roomId, int count){
        return dao.findLastMessages( roomId, count );
    }

    @Transactional
    public List<Message> findUnreadMessages(int lastReadMessage, List<Integer> roomIds){
        return dao.findUnreadMessages( lastReadMessage, roomIds );
    }

    @Override
    protected IOperations<Message> getDao() {
        return dao;
    }
}
