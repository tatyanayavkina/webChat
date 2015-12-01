package com.chat.server.dao;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Invitation;

import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface InvitationDao extends IOperations<Invitation> {
    List<Invitation> findAllByUserId(int userId);
}
