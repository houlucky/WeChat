package com.houxy.wechat.event;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by Houxy on 2016/5/2.
 */

//同意添加好友 和 收到添加好友的请求 的消息都由此Event处理
public class AddEvent {

    BmobIMConversation c;
    BmobIMMessage message;

    public AddEvent(BmobIMConversation c, BmobIMMessage message){
        this.c = c;
        this.message = message;
    }

    public BmobIMConversation getConversation() {
        return c;
    }

    public BmobIMMessage getMessage() {
        return message;
    }

}
