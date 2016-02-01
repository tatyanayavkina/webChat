package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Message;

import java.util.List;

public interface MessageService extends IOperations<Message> {
    List<Message> findLastMessages(int roomId, int count);
    List<Message> findUnreadMessages(int lastReadMessage, List<Integer> roomIds);
}
