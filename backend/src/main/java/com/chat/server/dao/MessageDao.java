package com.chat.server.dao;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Message;

import java.util.List;


public interface MessageDao  extends IOperations<Message> {
    List<Message> findLastMessages(int roomId, int count);
    List<Message> findUnreadMessages(int lastReadMessage, List<Integer> roomIds);
}
