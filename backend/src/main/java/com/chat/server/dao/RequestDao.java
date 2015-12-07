package com.chat.server.dao;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Request;

import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface RequestDao extends IOperations<Request> {
    List<Request> findAllByRoomId(int roomId);
    void deleteByUserIds(List<Integer> userIds);
}
