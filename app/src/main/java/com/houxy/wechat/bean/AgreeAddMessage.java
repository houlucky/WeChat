package com.houxy.wechat.bean;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by Houxy on 2016/5/1.
 */
public class AgreeAddMessage extends BmobIMExtraMessage {

    @Override
    public String getMsgType() {
        return "agreeAdd";
    }

    @Override
    public boolean isTransient() {//true 为暂态消息
        return true;
    }

    public  AgreeAddMessage(){}
}
