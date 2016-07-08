package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.event.ChatEvent;
import com.houxy.wechat.event.SetInfoEvent;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by Houxy on 2016/4/23.
 */
public class SearchUserHolder extends BaseViewHolder {

    @Bind(R.id.iv_userAvatar)
    CircleImageView ivUserAvatar;
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    @Bind(R.id.btn_addFriend)
    Button btnAddFriend;

    public SearchUserHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_searchuser, listener);
    }

    @Override
    public void bindData(Object o) {
        final User user = (User)o;
        tvUserName.setText(user.getUsername());
        ViewUtil.setAvatar(user.getAvatar(), R.mipmap.head, ivUserAvatar);
        final BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChatEvent(info));
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetInfoEvent(user));
            }
        });
    }
}
