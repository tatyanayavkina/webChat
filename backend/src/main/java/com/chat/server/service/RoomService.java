package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.exception.AlreadyExistsException;
import com.chat.server.exception.ObjectNotFoundException;
import com.chat.server.model.Room;
import com.chat.server.model.User;

import java.util.List;

public interface RoomService extends IOperations<Room> {
    Room joinRoom(int roomId, User user) throws AlreadyExistsException;

    List<Room> findByType(int type);

    List<User> getRoomUsers(int id) throws ObjectNotFoundException;

    Room removeUserFromRoom(int roomId, User user) throws ObjectNotFoundException;

    Room removeUsersFromRoom(int roomId, List<User> users)throws ObjectNotFoundException;

    List<User> findAlreadyInRoomUsers(int id, List<User> users);
}
