package com.chat.server.service.impl;

import com.chat.server.dao.RequestDao;
import com.chat.server.dao.RoleDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Request;
import com.chat.server.model.Role;
import com.chat.server.service.RequestService;
import com.chat.server.service.RoleService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28.10.2015.
 */
@Service
public class RequestServiceImpl extends AbstractService<Request> implements RequestService {

    @Autowired
    private RequestDao dao;

    public RequestServiceImpl(){
        super();
    }

    //API
    @Transactional
    public void add(int userId, List<Integer> roomIds){
        for( int roomId: roomIds){
            Request request = new Request( userId, roomId );
            dao.create( request );
        }
    }

    @Transactional
    public List<Request> findAllByRoomId(int roomId){
        return dao.findAllByRoomId(roomId);
    }

    @Transactional
    public void deleteByUserIds(List<Request> requests){
        List<Integer> userIds = new ArrayList<>();
        for( Request request: requests ){
            userIds.add( request.getUserId() );
        }
        dao.deleteByUserIds( userIds );
    }

    @Override
    protected IOperations<Request> getDao() {
        return dao;
    }
}
