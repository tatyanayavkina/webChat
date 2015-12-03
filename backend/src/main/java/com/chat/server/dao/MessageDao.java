package com.chat.server.dao;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Message;

import java.util.Date;
import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface MessageDao  extends IOperations<Message> {
    List<Message> findLastMessages(int roomId, int count);
    List<Message> findUnreadMessages(int userId, Date lastRequest, List<Integer> roomIds);
}
