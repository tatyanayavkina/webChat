package com.chat.server.service.impl;

import com.chat.server.dao.RoomDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.service.RoomService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public Room joinRoom(int roomId, User user){
        Room room = dao.findOne( roomId );
        if ( room != null ){
            List<User> users = room.getUsers();
            users.add( user );
            dao.update( room );

            room = dao.findOne( roomId );
            room.getOwner();
        }

        return room;
    }

    @Transactional
    public List<Room> findByType(int type){
        List<Room> rooms = dao.findByType( type );
        if ( rooms != null ){
            for( Room room: rooms ){
                room.getOwner();
            }
        }
        return rooms;
    }

    @Override
    protected IOperations<Room> getDao() {
        return dao;
    }
}
