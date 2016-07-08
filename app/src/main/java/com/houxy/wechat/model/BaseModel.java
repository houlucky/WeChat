package com.houxy.wechat.model;

import android.content.Context;

import com.houxy.wechat.BmobIMApplication;

import java.security.PublicKey;

/**
 * Created by asus on 2016/4/11.
 */
public class BaseModel {

    public static int DEFAULT_LIMIT =20;

    public static int CODE_NULL =1000;

    public static int CODE_NOT_EQUAL = 1001;

    public Context getContext(){
        return BmobIMApplication.INSTANCE();
    }
}
