package com.houxy.wechat.model.i;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

/**
 * Created by asus on 2016/4/13.
 */
public abstract class UpdateCacheListener extends BmobListener {

    public abstract void done(BmobException e);

    @Override
    protected void postDone(Object o, BmobException e) {

    }
}
