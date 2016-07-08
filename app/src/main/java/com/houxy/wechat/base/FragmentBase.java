package com.houxy.wechat.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * @author
 * @da
 */
public class FragmentBase extends Fragment{
    private static String NULL = "";
    private Toast toast;
    public void toast(final Object o){
        try {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    if( toast == null)
                        toast = Toast.makeText(getContext(), NULL, Toast.LENGTH_SHORT);
                    toast.setText(o.toString());
                    toast.show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void runOnMain(Runnable runnable){
        getActivity().runOnUiThread(runnable);
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), target);
        if (bundle != null)
            intent.putExtra(getActivity().getPackageName(), bundle);
        getActivity().startActivity(intent);
    }

    public void updateUserInfos(){

        BmobQuery<User> query = new BmobQuery<>();
        User currentUser = UserModel.getInstance().getCurrentUser();
        //contacts是User表中的字段，用来存储所有该用户的好友
        query.addWhereRelatedTo("contacts", new BmobPointer(currentUser));
        query.findObjects(getContext(), new FindListener<User>()  {
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
