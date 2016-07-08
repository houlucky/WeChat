package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;

import butterknife.Bind;

/**
 * Created by Houxy on 2016/5/2.
 */
public class ContactViewHolder extends BaseViewHolder {

    @Bind(R.id.iv_userAvatar)
    CircleImageView ivUserAvatar;
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    @Bind(R.id.tv_msg)
    TextView tvMsg;

    public ContactViewHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_contact, listener);
    }

    @Override
    public void bindData(Object o) {
        User user = (User) o;
        ViewUtil.setAvatar(user.getAvatar(), R.mipmap.head, ivUserAvatar);
        tvUserName.setText(user.getUsername());
    }
}
