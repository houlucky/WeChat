package com.houxy.wechat;

import android.content.Context;
import android.content.Intent;

import com.houxy.wechat.event.AddEvent;
import com.houxy.wechat.model.UserModel;
import com.houxy.wechat.model.i.UpdateCacheListener;
import com.houxy.wechat.ui.MainActivity;
import com.houxy.wechat.ui.NewFriendActivity;
import com.houxy.wechat.util.SharePreferenceUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by asus on 2016/4/10.
 */
public class MyMessageHandler extends BmobIMMessageHandler{

    private Context context;
    public MyMessageHandler(Context context){
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent messageEvent) {
        SharePreferenceUtil mSpUtil= BmobIMApplication.INSTANCE().getSpUtil();
        Boolean isAllowPushNotify = mSpUtil.isAllowPushNotify();
        Logger.i(messageEvent.getConversation().getConversationTitle() + "," + messageEvent.getMessage().getMsgType() + "," + messageEvent.getMessage().getContent());
        if(isAllowPushNotify){
            Boolean isAllowVoice = mSpUtil.isAllowVoice();
            if(isAllowVoice){
                BmobIMApplication.INSTANCE().getMediaPlayer().start();
            }
            UserModel.getInstance().updateUserInfo(messageEvent, new UpdateCacheListener() {
                @Override
                public void done(BmobException e) {
                    BmobIMMessage message = messageEvent.getMessage();
                    if(BmobIMMessageType.getMessageTypeValue(message.getMsgType()) == 0){
                        //如果为0，则表示是用户自定义的msg
                        Logger.i(message.getMsgType() + ","+ message.getContent() + ","+ message.getExtra());
                        if(BmobNotificationManager.getInstance(context).isShowNotification()){
                            Intent pendingIntent = new Intent(context, NewFriendActivity.class);
                            pendingIntent.putExtra("message", message);
                            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            BmobNotificationManager.getInstance(context).showNotification(messageEvent, pendingIntent);
                        }else {
                            Logger.i("当前处于应用中, 直接发送好友请求");
                            Logger.i("自定义, 添加好友" + messageEvent.getMessage() );
                            EventBus.getDefault().post(new AddEvent(messageEvent.getConversation(), messageEvent.getMessage()));
                        }
                    }else {
                        if(BmobNotificationManager.getInstance(context).isShowNotification()){
                            Intent pendingIntent = new Intent(context, MainActivity.class);
                            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            BmobNotificationManager.getInstance(context).showNotification(messageEvent, pendingIntent);
                        }else {
                            Logger.i("当前处于应用中, 直接发送聊天消息");
                            EventBus.getDefault().post(messageEvent);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent offlineMessageEvent) {
        Map<String,List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        Logger.i("共有" + map.size() + "个人发来了消息");
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()){
            List<MessageEvent> list = entry.getValue();
            UserModel.getInstance().updateUserInfo(list.get(0), new UpdateCacheListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        EventBus.getDefault().post(offlineMessageEvent);
                    }
                }
            });
        }
    }
}
