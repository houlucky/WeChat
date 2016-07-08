package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.bean.AgreeAddMessage;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;
import com.houxy.wechat.model.i.QueryUserListener;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Houxy on 2016/5/1.
 */
public class SendAgreeAddHolder extends BaseViewHolder {

    @Bind(R.id.iv_userAvatar)
    CircleImageView ivUserAvatar;
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    @Bind(R.id.tv_msg)
    TextView tvMsg;
    @Bind(R.id.btn_add)
    Button btnAdd;
    final AgreeAddMessage msg = new AgreeAddMessage();
    final User currentUser = UserModel.getInstance().getCurrentUser();
    User user;
    @Bind(R.id.tv_add)
    TextView tvAlreadyAdd;

    public SendAgreeAddHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_send_addfriend, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        msg.setContent(currentUser.getUsername() + "同意添加你为好友");
        msg.setExtra("agreeAdd");
        Logger.i(message.getConversationId());
        UserModel.getInstance().queryUserInfo(message.getConversationId(), new QueryUserListener() {
            @Override
            public void done(User u, BmobException e) {
                ViewUtil.setAvatar(u.getAvatar(), R.mipmap.default_head, ivUserAvatar);
                tvUserName.setText(u.getUsername());
                user = u;
                Logger.i(u == null ? "tttttttttt" : "ffffffffffffff");
            }
        });
        tvMsg.setText(message.getContent());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(user);
            }

        });
    }

    private void sendMessage(final User user) {
        BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (null == e) {
                    BmobIMConversation cc = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                    cc.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                            Logger.i("othermsg:" + msg.toString());
                            if (e == null) {//发送成功
                                toast("发送成功");
                                BmobRelation relation = new BmobRelation();
                                relation.add(user);
                                currentUser.setRelation(relation);
                                currentUser.update(getContext(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        Logger.i("多对多关联添加成功");
                                        btnAdd.setVisibility(View.GONE);
                                        tvAlreadyAdd.setVisibility(View.VISIBLE);
                                        //将该用户添加到我的本地好友列表中方便查询
                                        BmobIMApplication.INSTANCE().setContactList(user);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Logger.i("多对多关联添加失败");
                                    }
                                });
                            } else {//发送失败
                                toast("发送失败:" + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}
