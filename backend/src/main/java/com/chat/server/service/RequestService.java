package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Request;

import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface RequestService extends IOperations<Request> {

    void add(int userId, List<Integer> roomIds);
    List<Request> findAllByRoomId(int roomId);
    void deleteByUserIds(List<Request> requests);
}
