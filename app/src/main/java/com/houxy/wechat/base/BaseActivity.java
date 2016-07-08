package com.houxy.wechat.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @author :  houxy
 * @date :  2016/4/6 20:59
 */
public class BaseActivity extends SwipeBackActivity{

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        hideSoftInputView();
        super.onStop();
    }

    @Subscribe
    public void onEvent(Boolean empty){
    }


    public void startActivity(Class<? extends Activity> target, Bundle bundle,boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }


    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static String NULL = "";
    private Toast toast;
    public void toast(final Object o){
        try {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    if( toast == null)
                        toast = Toast.makeText(BaseActivity.this, NULL, Toast.LENGTH_SHORT);
                    toast.setText(o.toString());
                    toast.show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void runOnMain(Runnable runnable){
        runOnUiThread(runnable);
    }

    public Bundle getBudle(){
        if(getIntent() != null && getIntent().hasExtra(getPackageName())){
            return getIntent().getBundleExtra(getPackageName());
        }else {
            return null;
        }
    }

    public void updateUserInfos(){

        BmobQuery<User> query = new BmobQuery<>();
        User currentUser = UserModel.getInstance().getCurrentUser();
        //contacts是User表中的字段，用来存储所有该用户的好友
        query.addWhereRelatedTo("contacts", new BmobPointer(currentUser));
        query.findObjects(this, new FindListener<User>()  {
            @Override
            public void onSuccess(List<User> users) {
                if(null != BmobIMApplication.INSTANCE().getContactList()) {
                    BmobIMApplication.INSTANCE().getContactList().clear();
                }
                BmobIMApplication.INSTANCE().setContactList(users);
                Logger.d("查询个数：" + users.size());
            }
            @Override
            public void onError(int code, String msg) {
                Logger.d("查询失败：" + code + "-" + msg);
            }
        });
    }
}
