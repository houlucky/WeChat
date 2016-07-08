package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.a.a.V;
import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.util.TimeUtil;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Houxy on 2016/4/16.
 */
public class SendTextHolder extends BaseViewHolder {

    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.iv_fail_resend)
    ImageView ivFailResend;
    @Bind(R.id.tv_send_status)
    TextView tvSendStatus;
    @Bind(R.id.progress_load)
    ProgressBar progressLoad;

    BmobIMConversation c;

    public SendTextHolder(Context context, ViewGroup root, BmobIMConversation c,OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_chat_sent_message, listener);
        this.c = c;
    }

    @Override
    public void bindData(Object o) {

        final BmobIMMessage message = (BmobIMMessage)o;
        String content = message.getContent();
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        ViewUtil.setAvatar(info != null ? info.getAvatar() : null, R.mipmap.head, ivAvatar);
        tvMessage.setText(content);
        tvTime.setText(TimeUtil.getChatTime(false,message.getCreateTime()));
        int status = message.getSendStatus();
        if(BmobIMSendStatus.SENDFAILED.getStatus() == status){
            ivFailResend.setVisibility(View.VISIBLE);
            progressLoad.setVisibility(View.GONE);
        }else if(BmobIMSendStatus.SENDING.getStatus() == status){
            progressLoad.setVisibility(View.VISIBLE);
            ivFailResend.setVisibility(View.GONE);
        }else {
            ivFailResend.setVisibility(View.GONE);
            progressLoad.setVisibility(View.GONE);
        }

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(info.getName());
            }
        });

        ivFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.resendMessage(message, new MessageSendListener() {
                    @Override
                    public void onStart(BmobIMMessage bmobIMMessage) {
                        ivFailResend.setVisibility(View.GONE);
                        progressLoad.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                        if(e == null){
                            progressLoad.setVisibility(View.GONE);
                            ivFailResend.setVisibility(View.GONE);
                        }else {
                            progressLoad.setVisibility(View.GONE);
                            ivFailResend.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRecyclerViewListener != null){
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        tvMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onRecyclerViewListener != null){
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return false;
            }
        });
    }

    public void showTime(boolean isShow){
        tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
