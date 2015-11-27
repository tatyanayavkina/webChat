package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
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
    List<User> findUsersByLogin(List<String> logins);
    List<Room> findRoomsWithOwnersByUserId(int id);
}
