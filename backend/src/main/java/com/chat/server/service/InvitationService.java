package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Invitation;
import com.chat.server.model.Room;
import com.chat.server.model.User;

import java.util.List;

public interface InvitationService extends IOperations<Invitation> {
    Invitation createInvitation(User user, Room room);
    List<Invitation> findAllByUserId(int userId);
    List<User> findAlreadyInvitedUsers(int roomId, List<User> users);
}
