package com.houxy.wechat.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import com.houxy.wechat.R;
import com.houxy.wechat.event.FinishEvent;
import com.houxy.wechat.model.BaseModel;
import com.houxy.wechat.model.UserModel;
import org.greenrobot.eventbus.EventBus;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * @author :  houxy
 * @date :  2016/4/6 21:34
 */

public class RegisterActivity extends ParentWithNaviActivity {
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_password_again)
    EditText etPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initNaviView();
    }

    @Override
    public String title() {
        return "注册";
    }

    @Override
    public ToolBarListener setToolBarListener() {
        return new ToolBarListener() {
            @Override
            public void onClickLeft() {
                finish();
            }

            @Override
            public void onClickRight() {

            }
        };
    }

    @OnClick(R.id.btn_register)
    public void onRegisterClick() {
        UserModel.getInstance().register(etUsername.getText().toString(), etPassword.getText().toString(),
                etPasswordAgain.getText().toString(), new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if( e == null){
                            EventBus.getDefault().post(new FinishEvent());
                            startActivity(MainActivity.class, null, true);
                        }else {
                            if( e.getErrorCode() == BaseModel.CODE_NOT_EQUAL){
                                etPasswordAgain.setText("");
                            }
                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }
                });
    }
}
