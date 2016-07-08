package com.houxy.wechat.model;

import android.text.TextUtils;
import android.util.Log;

import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.i.QueryUserListener;
import com.houxy.wechat.model.i.UpdateCacheListener;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.XListener;

/**
 * Created by asus on 2016/4/11.
 */
public class UserModel extends BaseModel{

    private static UserModel userModel = new UserModel();

    public static UserModel getInstance(){
        return userModel;
    }

    private UserModel(){}


    public void login(String userName, String password, final LogInListener listener){
        if(TextUtils.isEmpty(userName)){
            listener.internalDone(new BmobException(CODE_NULL, "请输入用户名"));
            return;
        }
        if(TextUtils.isEmpty(password)){
            listener.internalDone(new BmobException(CODE_NULL, "请输入密码"));
            return;
        }
        final User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(getCurrentUser(), null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(user, new BmobException(i, s));
            }
        });
    }

    public void register(String userName, String password, String pwdAgain, final LogInListener listener){
        if( TextUtils.isEmpty(userName)){
            listener.internalDone(CODE_NULL, new BmobException("请输入用户名"));
            return;
        }
        if( TextUtils.isEmpty(password)){
            listener.internalDone(CODE_NULL, new BmobException("请输入密码"));
            return;
        }
        if( TextUtils.isEmpty(pwdAgain)){
            listener.internalDone(CODE_NULL, new BmobException("请确认密码"));
            return;
        }
        if( !password.equals(pwdAgain)){
            listener.internalDone(CODE_NOT_EQUAL, new BmobException("两次输入的密码不一致"));
            return;
        }
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setSex(true);
        user.setNickName("");
        user.setRelation(new BmobRelation());
        user.signUp(getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.done(null, null);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.done(null, new BmobException(i,s));
            }
        });
    }

    public void queryUserInfo(String objectId, final QueryUserListener listener){
        BmobQuery<User> query = new BmobQuery();
        query.addWhereEqualTo("objectId", objectId);// Logger.d("query"+objectId);
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list != null && list.size()>0 ){
                    //listener.internalDone(list.get(0), null);
                    //此处为超级大BUG 开始我的username一直不正确就是因为这个
                    //因为Bomb内部不在集成与用户相关的逻辑所以需要使用自己写的done来完成用户信息的更新
                    //而不能使用listener.internalDone(list.get(0), null);
                    listener.done(list.get(0), null);
                }else {
                    listener.internalDone(new BmobException(000, "查无此人"));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.internalDone(new BmobException(i, s));
            }
        });
    }

    public void updateUserInfo(MessageEvent event,final UpdateCacheListener updateCacheListener){
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        String username = info.getName();
        String title = conversation.getConversationTitle();
        Logger.d("info", "username"+ ":"+ username + "," + title);
        //在sdk内部  username和title都是使用objectId表示的
        if(!username.equals(title)){
            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(User user, BmobException e) {
                    if(e == null){
                        String userName = user.getUsername();
                        String avatar = user.getAvatar();
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(userName);
                        info.setAvatar(avatar);
                        info.setName(userName);
                        info.setUserId(user.getObjectId());
                        Logger.d(userName + "|" + avatar);
                        BmobIM.getInstance().updateUserInfo(info);
                        BmobIM.getInstance().updateConversation(conversation);
                    }else {
                        Logger.e(e.getErrorCode() + e.getMessage());
                    }
                    updateCacheListener.done(null);
                }
            });
        }else {
            //Logger.d("false");
           //updateCacheListener.internalDone(null);
            updateCacheListener.done(null);
        }
    }

    public User getCurrentUser(){
            return BmobUser.getCurrentUser(getContext(), User.class);
    }

    public void logout() {
        BmobUser.logOut(getContext());
    }

    public void queryUsers(String username, int defaultLimit, final FindListener<User> findListener) {
        BmobQuery<User> query = new BmobQuery<>();
        try {
            BmobUser user = BmobUser.getCurrentUser(getContext());
            query.addWhereNotEqualTo("username", user.getUsername());
        }catch (Exception e){
            Logger.e(e);
        }
        query.addWhereContains("username",username);
        query.setLimit(defaultLimit);
        query.order("-createdAt");
        query.findObjects(getContext(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list != null && list.size()>0){
                    findListener.onSuccess(list);
                }else {
                    findListener.onError(CODE_NULL, "查无此人或您查找的为您自己");
                }
            }

            @Override
            public void onError(int i, String s) {
                findListener.onError(i,s);
            }
        });
    }
}
