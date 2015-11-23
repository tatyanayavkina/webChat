package com.chat.server.dao;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Room;

import java.util.List;

/**
 * Created by Татьяна on 28.10.2015.
 */
public interface RoomDao extends IOperations<Room> {

    List<Room> findByType(int type);
}
