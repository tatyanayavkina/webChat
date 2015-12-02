package com.chat.server.model;

import java.util.List;

/**
 * Created by Татьяна on 02.12.2015.
 */
public class InvitationsInfo {
    private List<User> alreadyInRoomUsers;
    private List<User> alreadyInvitedUsers;
    private List<User> invitedUsers;

    public List<User> getAlreadyInRoomUsers(){
        return alreadyInRoomUsers;
    }

    public void setAlreadyInRoomUsers(List<User> alreadyInRoomUsers){
        this.alreadyInRoomUsers = alreadyInRoomUsers;
    }

    public List<User> getAlreadyInvitedUsers(){
        return alreadyInvitedUsers;
    }

    public void setAlreadyInvitedUsers(List<User> alreadyInvitedUsers){
        this.alreadyInvitedUsers = alreadyInvitedUsers;
    }

    public List<User> getInvitedUsers(){
        return invitedUsers;
    }

    public void setInvitedUsers(List<User> invitedUsers){
        this.invitedUsers = invitedUsers;
    }

}
