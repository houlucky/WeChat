package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houxy.wechat.util.TimeUtil;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;
import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;

/**
 * @author :  houxy
 * @date :  2016/4/6 14:04
 */
public class ConversationViewHolder extends BaseViewHolder {

    @Bind(R.id.iv_userAvatar)
    CircleImageView ivUserAvatar;
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    @Bind(R.id.tv_msg)
    TextView tvMsg;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_unRead)
    TextView tvUnRead;

    public ConversationViewHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_conversation, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        BmobIMConversation conversation = (BmobIMConversation)o;
        List<BmobIMMessage> msgs = conversation.getMessages();
        if(msgs != null && msgs.size()>0){
            BmobIMMessage lastMsg = msgs.get(0);
            String content = lastMsg.getContent();
            if(lastMsg.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
                tvMsg.setText(content);
            }else if(lastMsg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
                tvMsg.setText("[图片]");
            }else if(lastMsg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())){
                tvMsg.setText("[位置]" + content);
            }else if(lastMsg.getMsgType().equals(BmobIMMessageType.VOICE.getType())){
                tvMsg.setText("[语音]");
            } else {
                tvMsg.setText("未知");//用户自定义的消息类型
            }
            tvTime.setText(TimeUtil.getConversationTime(false,lastMsg.getCreateTime()));
        }
        //设置聊天头像
        ViewUtil.setAvatar(conversation.getConversationIcon(), R.mipmap.head, ivUserAvatar);
        //设置会话标题
        tvUserName.setText(conversation.getConversationTitle());
        long unread = BmobIM.getInstance().getUnReadCount(conversation.getConversationId());
        if(unread>0){
            tvUnRead.setVisibility(View.VISIBLE);
            tvUnRead.setText(String.valueOf(unread));
        }else {
            tvUnRead.setVisibility(View.GONE);
        }
    }
}
