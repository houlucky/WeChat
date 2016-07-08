package com.houxy.wechat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.houxy.wechat.R;
import com.houxy.wechat.base.BaseActivity;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;

/**
 * @author :  houxy
 * @date :  2016/4/6 20:58
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if ( user == null) {
                    startActivity(LoginActivity.class, null, true);
                } else {
                    updateUserInfos();
                    Log.d("hhh", "updateUserInfos");
                    startActivity(MainActivity.class, null, true);
                }
            }
        }, 1000);
    }
}
