package com.houxy.wechat.event;

import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by Houxy on 2016/4/23.
 */
public class ChatEvent {
    private BmobIMUserInfo info;
    public ChatEvent(BmobIMUserInfo info){
        this.info = info;
    }

    public BmobIMUserInfo getInfo() {
        return info;
    }
}
