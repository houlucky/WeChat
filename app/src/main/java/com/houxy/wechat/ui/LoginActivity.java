package com.houxy.wechat.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.houxy.wechat.base.BaseActivity;
import com.houxy.wechat.R;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.event.FinishEvent;
import com.houxy.wechat.model.UserModel;
import org.greenrobot.eventbus.Subscribe;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * @author :  houxy
 * @date :  2016/4/6 20:56
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        UserModel.getInstance().login(etUsername.getText().toString(), etPassword.getText().toString(),
                new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if( e == null){
                            User user = (User)o;
                            //更新当前用户的资料
                            BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                    user.getUsername(), user.getAvatar()));
                            updateUserInfos();
                            startActivity(MainActivity.class, null, true);
                        }else {
                            toast(e.getMessage() + "(" + e.getErrorCode() +")");
                        }
                    }
                });
    }

    @OnClick(R.id.tv_register)
    public void onRegisterClick() {
        startActivity(RegisterActivity.class, null, false);
    }

    @Subscribe
    public void onEventMainThread(FinishEvent finishEvent){
        finish();
    }
}
