package com.chat.server.service.impl;

import com.chat.server.dao.RoomDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.exception.AlreadyExistsException;
import com.chat.server.exception.ObjectNotFoundException;
import com.chat.server.model.Room;
import com.chat.server.model.User;
import com.chat.server.service.RoomService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl extends AbstractService<Room> implements RoomService {

    @Autowired
    private RoomDao dao;

    public RoomServiceImpl(){
        super();
    }

    //API
    @Transactional
    public Room joinRoom(int id, User user) throws AlreadyExistsException {
        Room room = dao.findOne( id );
        if ( room != null ){

            List<User> users = room.getUsers();
            if ( users.contains( user ) ){
                throw new AlreadyExistsException( User.class );
            }

            users.add( user );
            dao.update( room );
            room = dao.findOne( id );
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
    public List<User> getRoomUsers(int id) throws ObjectNotFoundException{
        Room room = dao.findOne(id);
        if ( room != null ){
            List<User> users = room.getUsers();
            // to make users loaded
            users.size();
            return users;
        } else {
            throw new ObjectNotFoundException( Room.class, id );
        }
    }

    @Transactional
    public Room removeUserFromRoom(int roomId, User user) throws ObjectNotFoundException{
        Room room = dao.findOne( roomId );
        if ( room != null ){
            List<User> users = room.getUsers();
            boolean contained = users.remove( user );
            if ( contained ){
                dao.update( room );
            }
            return room;

        } else {
            throw new ObjectNotFoundException( Room.class, roomId );
        }

    }

    @Transactional
    public Room removeUsersFromRoom(int roomId, List<User> usersToRemove) throws ObjectNotFoundException{
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
            }
            return room;
        } else {
            throw new ObjectNotFoundException( Room.class, roomId );
        }
    }

    @Transactional
    public List<User> findAlreadyInRoomUsers(int id, List<User> users){
        Room room = dao.findOne( id );
        List<User> roomUsers = room.getUsers();
        List<User> alreadyInRoomUsers = new ArrayList<>();
        for( User user: users ){
            if ( roomUsers.contains( user ) ){
                alreadyInRoomUsers.add( user );
            }
        }

        return alreadyInRoomUsers;
    }

    @Override
    protected IOperations<Room> getDao() {
        return dao;
    }
}
