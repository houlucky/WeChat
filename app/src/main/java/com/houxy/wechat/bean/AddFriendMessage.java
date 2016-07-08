package com.houxy.wechat.bean;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * Created by Houxy on 2016/4/25.
 */
public class AddFriendMessage extends BmobIMExtraMessage{

    @Override
    public String getMsgType() {
        return "addFriend";
    }

    @Override
    public boolean isTransient() {//true 为暂态消息
        return true;
    }

    public AddFriendMessage(){}
}
