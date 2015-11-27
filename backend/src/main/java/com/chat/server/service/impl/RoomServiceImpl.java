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

import javax.jws.soap.SOAPBinding;
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
    public Room joinRoom(int id, User user){
        Room room = dao.findOne( id );
        if ( room != null ){
            List<User> users = room.getUsers();
            users.add(user);
            dao.update( room );

            room = dao.findOne( id );
            room.getOwner();
        }

        return room;
    }

    @Transactional
    public List<Room> findByType(int type){
        List<Room> rooms = dao.findByType(type);
        if ( rooms != null ){
            for( Room room: rooms ){
                room.getOwner();
            }
        }
        return rooms;
    }

    @Transactional
    public List<User> getRoomUsers(int id){
        Room room = dao.findOne(id);
        if ( room != null ){
            List<User> users = room.getUsers();
            return users;
        }

        return null;
    }

    @Transactional
    public Room removeUserFromRoom(int roomId, User user){
        Room room = dao.findOne( roomId );
        if ( room != null ){
            List<User> users = room.getUsers();
            boolean contained = users.remove( user );
            if ( contained ){
                dao.update( room );
                return room;
            }
        }

        return null;
    }

    @Transactional
    public Room removeUsersFromRoom(int roomId, List<User> usersToRemove){
        Room room = dao.findOne( roomId );
        if( room != null ){
            List<User> users = room.getUsers();
            int counter = 0;
            for( User user: usersToRemove ){
                boolean contained = users.remove( user );
                if ( contained ){
                    counter ++;
                }
            }
            if ( counter > 0 ){
                room.setUsers( users );
                dao.update( room );
                return room;
            }
        }

        return null;
    }

    @Override
    protected IOperations<Room> getDao() {
        return dao;
    }
}
