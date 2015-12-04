package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.exception.ObjectNotFoundException;
import com.chat.server.model.Role;
import com.chat.server.model.Room;
import com.chat.server.model.User;

import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface UserService extends IOperations<User>{
    //API
    User createUserByNickname(String nickname, Role role);
    User findUserByLogin(String login);
    List<User> findUsersByLogin(List<String> logins);
    User findUserWithRooms(int id);
    List<Room> findRoomsWithOwnersByUserId(int id) throws ObjectNotFoundException;
}
