package com.houxy.wechat.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bmob.d.a.V;
import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.R;
import com.houxy.wechat.bean.AddFriendMessage;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;
import com.houxy.wechat.util.ViewUtil;
import com.houxy.wechat.view.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Houxy on 2016/4/23.
 */
public class SetMyInfoActivity extends ParentWithNaviActivity {

    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.iv_set_avatar)
    CircleImageView ivSetAvatar;
    @Bind(R.id.tv_set_nick)
    TextView tvSetNick;
    @Bind(R.id.rl_nick)
    RelativeLayout rlNick;
    @Bind(R.id.tv_set_name)
    TextView tvSetName;
    @Bind(R.id.tv_set_gender)
    TextView tvSetGender;
    @Bind(R.id.btn_add_friend)
    Button btnAddFriend;
    @Bind(R.id.btn_chat)
    Button btnChat;

    @Bind(R.id.layout_black_tips)
    RelativeLayout layoutBlackTips;


    @Bind(R.id.btn_blacklist)
    Button btnBlacklist;
    @Bind(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @Bind(R.id.rl_account)
    RelativeLayout rlAccount;
    @Bind(R.id.rl_gender)
    RelativeLayout rlGender;
    @Bind(R.id.fl_popwinbg)
    FrameLayout flPopWinbg;


    @Override
    public String title() {
        if ("add".equals(from)) {
            return "详细资料";
        } else {
            return "个人资料";
        }
    }

    String from;
    User user;
    BmobIMUserInfo info;
    User currentUser;
    PopupWindow modifyAvatarPW;
    public final static int SELECT_PIC = 0;
    public final static int CAPTURE_PIC = 1;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setinfo);
        ButterKnife.bind(this);
        from = getBudle().getString("from");
        user = (User) getBudle().getSerializable("user");
        currentUser = UserModel.getInstance().getCurrentUser();
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        modifyAvatarPW = getPopWindow(getLayoutInflater(), R.layout.modifyavatar_popwindow);
        initNaviView();
        initView();
    }

    private void initView() {
        tvSetName.setText(user.getUsername());
        ViewUtil.setAvatar(user.getAvatar(), R.mipmap.default_head, ivSetAvatar);
        tvSetNick.setText(user.getNickName());
        tvSetGender.setText(user.getSex() ? "男" : "女");
        List<User> users = BmobIMApplication.INSTANCE().getContactList();
        if (null == users)
            users = new ArrayList<>();
        if ("add".equals(from)) {
            int i;
            for (i = 0; i < users.size(); i++) {
                Logger.d(users.get(i).getObjectId() + "   :   " + user.getObjectId());
                if (user.getObjectId().equals(users.get(i).getObjectId())) {//判断字符串相等应该用equals
                    break;
                }
            }
            if (i < users.size()) {//说明是我的好友
                Logger.d("yes friend");
                btnAddFriend.setVisibility(View.GONE);
                btnChat.setVisibility(View.VISIBLE);
                btnBlacklist.setVisibility(View.VISIBLE);
            } else {
                btnAddFriend.setVisibility(View.VISIBLE);
                btnChat.setVisibility(View.VISIBLE);
                btnBlacklist.setVisibility(View.GONE);
            }
            ivSetAvatar.setClickable(false);
            rlNick.setClickable(false);
            rlGender.setClickable(false);
        }
    }

    @OnClick(R.id.btn_add_friend)
    public void onAddClick() {
        final AddFriendMessage msg = new AddFriendMessage();
        msg.setContent(currentUser.getUsername() + " : 请求添加你为好友");
        msg.setExtra("addFriend");
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                BmobIMConversation cc = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                cc.sendMessage(msg, new MessageSendListener() {
                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        Logger.i("othermsg:" + msg.toString());
                        if (e == null) {//发送成功
                            toast("发送成功");
                        } else {//发送失败
                            toast("发送失败:" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.btn_chat)
    public void onChatClick() {

        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle, false);
                } else {
                    toast("(" + e.getErrorCode() + ")" + e.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.btn_blacklist)
    public void onBlackListClick() {
    }

    @OnClick({R.id.iv_set_avatar, R.id.rl_nick, R.id.rl_gender})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_set_avatar:
                modifyAvatar(view);
                break;
            case R.id.rl_nick:
                modifyNickName();
                break;
            case R.id.rl_gender:
                modifyGender();
                break;
        }
    }

    private void modifyAvatar(View view) {
        modifyAvatarPW.showAtLocation(view, Gravity.CENTER, 0, 0);
        flPopWinbg.setVisibility(View.VISIBLE);
    }

    private void modifyNickName() {
        new MaterialDialog.Builder(this)
                .title("修改昵称")
                .positiveText("确定")
                .negativeText("取消")
                .cancelable(true)
                .input("", currentUser.getNickName(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                        if (!TextUtils.isEmpty(input)) {
                            User user = new User();
                            user.setNickName(input.toString());
                            updateUserData(user, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    tvSetNick.setText(input.toString());
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    toast(i + s);
                                }
                            });
                        }
                    }
                }).show();
    }

    private void modifyGender() {
        String gender[] = {"男", "女"};
        new MaterialDialog.Builder(this)
                .title("性别")
                .positiveText("选择")
                .negativeText("取消")
                .cancelable(true)
                .items(gender)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, final CharSequence text) {
                        dialog.setSelectedIndex(which);
                        User user = new User();
                        updateUserData(user, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                tvSetGender.setText(text);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                toast(i + s);
                            }
                        });
                        return true;
                    }
                }).show();
    }

    private void updateUserData(User user, UpdateListener listener) {
        user.setObjectId(currentUser.getObjectId());
        user.update(this, listener);
    }

    private PopupWindow getPopWindow(LayoutInflater inflater, int res) {
        View pwView;
        PopupWindow popupWindow;
        pwView = inflater.inflate(res, null);
        popupWindow = new PopupWindow(pwView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                flPopWinbg.setVisibility(View.GONE);
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        TextView tvCamera = (TextView)pwView.findViewById(R.id.tv_camera);
        TextView tvPhotoGallery = (TextView)pwView.findViewById(R.id.tv_PhotoGallery);
        tvCamera.setOnClickListener(listener);
        tvPhotoGallery.setOnClickListener(listener);
        return popupWindow;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_camera:
                    Intent intentCapPic = new Intent();
                    // 指定开启系统相机的Action
                    intentCapPic.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCapPic.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(intentCapPic, CAPTURE_PIC);
                    modifyAvatarPW.dismiss();
                    break;

                case R.id.tv_PhotoGallery:
                    Intent intentPic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentPic, SELECT_PIC);
                    modifyAvatarPW.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SELECT_PIC:
                ViewUtil.setAvatar(data, ivSetAvatar);
                break;
            case CAPTURE_PIC:
                ViewUtil.setAvatar(data, ivSetAvatar);
                break;
            default:
                break;
        }
    }
}
