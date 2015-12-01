package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.exception.ObjectNotFoundException;
import com.chat.server.model.Invitation;
import com.chat.server.model.Room;
import com.chat.server.model.User;

import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface InvitationService extends IOperations<Invitation> {
    Invitation createInvitation(User user, Room room);
    List<Invitation> findAllByUserId(int userId);
}
