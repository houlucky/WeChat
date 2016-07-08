package com.houxy.wechat.event;

import com.houxy.wechat.bean.User;


/**
 * Created by Houxy on 2016/4/23.
 */
public class SetInfoEvent {
    private User user;

    public SetInfoEvent(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
