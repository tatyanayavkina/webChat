package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Message;
import com.chat.server.model.User;

import java.util.Date;
import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface MessageService extends IOperations<Message> {
    List<Message> findLastMessages(int roomId, int count);
    List<Message> findUnreadMessages(int lastReadMessage, List<Integer> roomIds);
}
