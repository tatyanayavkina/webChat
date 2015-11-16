package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Role;
import com.chat.server.model.User;

/**
 * Created on 28.10.2015.
 */
public interface UserService extends IOperations<User>{
    //API
    User createUserByNickname(String nickname, Role role);
}
