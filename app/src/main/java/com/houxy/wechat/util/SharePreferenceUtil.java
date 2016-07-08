package com.houxy.wechat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Houxy on 2016/5/3.
 */
public class SharePreferenceUtil {

    private String SHARED_KEY_VOICE = "shared_key_voice";
    private String SHARED_KEY_NOTIFY = "shared_key_notify";
    private String SHARED_KEY_VIBRATE = "shared_key_vibrate";
    private static SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public SharePreferenceUtil(Context context, String name){
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Boolean isAllowPushNotify(){
        return sharedPreferences.getBoolean(SHARED_KEY_NOTIFY, true);
    }

    public void setPushNotifyEnable(Boolean isPush){
        editor.putBoolean(SHARED_KEY_NOTIFY, isPush);
        editor.commit();
    }

    public Boolean isAllowVoice(){
        return sharedPreferences.getBoolean(SHARED_KEY_VOICE, true);
    }

    public  void setVioceEnable(Boolean isAllowVoice){
        editor.putBoolean(SHARED_KEY_VOICE, isAllowVoice);
        editor.commit();
    }

    public Boolean isAllowVibrate(){
        return sharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
    }

    public void setVibrateEnable(Boolean isAllowVibrate){
        editor.putBoolean(SHARED_KEY_VIBRATE, isAllowVibrate);
        editor.commit();
    }
}
