package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.util.TimeUtil;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;

import java.security.PublicKey;
import java.text.SimpleDateFormat;

import javax.crypto.spec.IvParameterSpec;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by Houxy on 2016/4/19.
 */
public class ReceivedTextHolder extends BaseViewHolder {

    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @Bind(R.id.tv_message)
    TextView tvMessage;

    public ReceivedTextHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_message, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        BmobIMMessage message = (BmobIMMessage)o;
        String content = message.getContent();
        BmobIMUserInfo info = message.getBmobIMUserInfo();
        ViewUtil.setAvatar(info!=null ? info.getAvatar() : null, R.mipmap.head, ivAvatar);
        tvMessage.setText(content);
        tvTime.setText(TimeUtil.getChatTime(false,message.getCreateTime()));
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
                onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                return false;
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("photo");
            }
        });


    }

    public void showTime(boolean isShow) {
        tvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
