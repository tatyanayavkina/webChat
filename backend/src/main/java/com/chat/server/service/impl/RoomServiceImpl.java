package com.chat.server.service.impl;

import com.chat.server.dao.RoomDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Room;
import com.chat.server.service.RoomService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 28.10.2015.
 */
@Service
public class RoomServiceImpl extends AbstractService<Room> implements RoomService {

    @Autowired
    private RoomDao dao;

    public RoomServiceImpl(){
        super();
    }

    //API

    @Override
    protected IOperations<Room> getDao() {
        return dao;
    }
}
