package com.chat.server.dao.impl;

import com.chat.server.dao.RoomDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDaoImpl extends AbstractDao<Room> implements RoomDao {
    public RoomDaoImpl(){
        super();
        setClazz(Room.class);
    }

    //API
    public List<Room> findByType(int type){
        String hql = "from Room r where r.type=" + type;
        return getCurrentSession().createQuery(hql).list();
    }
}
