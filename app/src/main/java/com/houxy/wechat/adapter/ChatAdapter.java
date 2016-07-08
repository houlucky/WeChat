package com.houxy.wechat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.houxy.wechat.adapter.holder.BaseViewHolder;
import com.houxy.wechat.adapter.holder.ReceivedTextHolder;
import com.houxy.wechat.adapter.holder.SendTextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;

/**
 * Created by asus on 2016/4/15.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //文本
    private final int TYPR_RECEIVERD_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    //图片
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;
    //位置
    private final int TYPE_SEND_LOCATION = 4;
    private final int TYPE_RECEIVER_LOCATION = 5;
    //语音
    private final int TYPE_SEND_VOICE =6;
    private final int TYPE_RECEIVER_VOICE = 7;
    //视频
    private final int TYPE_SEND_VIDEO =8;
    private final int TYPE_RECEIVER_VIDEO = 9;



    /**
     * 显示时间间隔:5分钟
     */
    private final long TIME_INTERVAL = 5 * 60 * 1000;
    BmobIMConversation c;
    List<BmobIMMessage> msgs = new ArrayList<>();
    String currentUID = "";

    public ChatAdapter(Context context, BmobIMConversation c){
        try {
            currentUID = BmobUser.getCurrentUser(context).getObjectId();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.c = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_SEND_TXT){
            return new SendTextHolder(parent.getContext(), parent, c, listener);
        }else if(viewType == TYPR_RECEIVERD_TXT){
            return new ReceivedTextHolder(parent.getContext(), parent, listener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(msgs.get(position));
        if(holder instanceof SendTextHolder){
            ((SendTextHolder) holder).showTime(shouldShowTime(position));
        }else if (holder instanceof ReceivedTextHolder){
            ((ReceivedTextHolder) holder).showTime(shouldShowTime(position));
        }
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = msgs.get(position);
        if(message.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
            return message.getFromId().equals(currentUID) ? TYPE_SEND_TXT : TYPR_RECEIVERD_TXT;
        }else if(message.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
            return message.getFromId().equals(currentUID) ? TYPE_SEND_LOCATION : TYPE_RECEIVER_LOCATION;
        }else if (message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
            return message.getFromId().equals(currentUID) ? TYPE_SEND_IMAGE : TYPE_RECEIVER_IMAGE;
        }else if (message.getMsgType().equals(BmobIMMessageType.VOICE.getType())){
            return message.getFromId().equals(currentUID) ? TYPE_SEND_VOICE : TYPE_RECEIVER_VOICE;
        } else {
            return message.getFromId().equals(currentUID) ? TYPE_SEND_VIDEO : TYPE_RECEIVER_VIDEO;
        }
    }

    private OnRecyclerViewListener listener;
    public void setOnRecyclerViewListener(OnRecyclerViewListener listener){
        this.listener = listener;
    }

    private boolean shouldShowTime(int postion){
        if(postion == 0){
            return true;
        }
        long lastTime = msgs.get(postion-1).getCreateTime();
        long curTime = msgs.get(postion).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }



    public int findPosition(BmobIMMessage msg) {
        int index = this.getCount();      //getCount 和getItemCount的区别
        int position = -1;
        while (index-- > 0 ){ //???????????????
            if(msg.equals(getItem(index))){
                position = index;
            }
        }
        return position;
    }

    private BmobIMMessage getItem(int position) {
        return this.msgs == null ? null:(position >= this.msgs.size()?null:this.msgs.get(position));
    }

    private int getCount() {
        return this.msgs == null?0:this.msgs.size();
    }

    public void addMessage(BmobIMMessage msg) {
        msgs.addAll(Arrays.asList(msg));
    }

    public void addMessages(List<BmobIMMessage> messages){
        msgs.addAll(0, messages);
    }

    public BmobIMMessage getFirstMessage() {
        if(null != msgs && msgs.size() > 0){
            return msgs.get(0);
        }else
            return null;
    }
}
