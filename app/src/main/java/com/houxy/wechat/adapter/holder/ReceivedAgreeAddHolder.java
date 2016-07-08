package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;
import com.houxy.wechat.model.i.QueryUserListener;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;
import com.nostra13.universalimageloader.utils.L;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Houxy on 2016/5/1.
 */
public class ReceivedAgreeAddHolder extends BaseViewHolder {

    @Bind(R.id.iv_userAvatar)
    CircleImageView ivUserAvatar;
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    @Bind(R.id.tv_msg)
    TextView tvMsg;


    public ReceivedAgreeAddHolder(Context context, ViewGroup group, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, group, R.layout.item_received_agreeadd, onRecyclerViewListener);

    }

    @Override
    public void bindData(Object o) {
        BmobIMMessage message = (BmobIMMessage)o;

        UserModel.getInstance().queryUserInfo(message.getConversationId(), new QueryUserListener() {
            @Override
            public void done(final User u, BmobException e) {
                User currentUser = UserModel.getInstance().getCurrentUser();
                ViewUtil.setAvatar(u.getAvatar(), R.mipmap.default_head, ivUserAvatar);
                tvUserName.setText(u.getUsername());
                //将好友关系同步到云端
                BmobRelation relation = new BmobRelation();
                relation.add(u);
                currentUser.setRelation(relation);
                currentUser.update(getContext(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Logger.i("多对多关联添加成功");
                        //将该用户添加到我的本地好友列表中方便查询
                        BmobIMApplication.INSTANCE().setContactList(u);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Logger.i("多对多关联添加失败" + s);
                    }
                });

            }
        });

        tvMsg.setText(message.getContent());

    }
}
