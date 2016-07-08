package com.houxy.wechat.model.i;

import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

/**
 * Created by asus on 2016/4/13.
 */
public abstract class QueryUserListener extends BmobListener<User> {

    public abstract void done(User user, BmobException e);

    @Override
    protected void postDone(User user, BmobException e) {
    }
}
