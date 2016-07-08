package com.houxy.wechat.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a.a.V;
import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.R;
import com.houxy.wechat.base.FragmentBase;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;
import com.houxy.wechat.ui.LoginActivity;
import com.houxy.wechat.ui.SetMyInfoActivity;
import com.houxy.wechat.util.SharePreferenceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;

/**
 * @author :  houxy
 * @date :  2016/4/2 17:13
 */
public class SettingFragment extends FragmentBase {


    @Bind(R.id.tv_set_name)
    TextView tvSetName;
    @Bind(R.id.layout_info)
    RelativeLayout layoutInfo;
    @Bind(R.id.layout_blacklist)
    RelativeLayout layoutBlacklist;
    @Bind(R.id.iv_open_notification)
    ImageView ivOpenNotification;
    @Bind(R.id.iv_close_notification)
    ImageView ivCloseNotification;
    @Bind(R.id.rl_switch_notification)
    RelativeLayout rlSwitchNotification;
    @Bind(R.id.iv_open_voice)
    ImageView ivOpenVoice;
    @Bind(R.id.iv_close_voice)
    ImageView ivCloseVoice;
    @Bind(R.id.rl_switch_voice)
    RelativeLayout rlSwitchVoice;
    @Bind(R.id.iv_open_vibrate)
    ImageView ivOpenVibrate;
    @Bind(R.id.iv_close_vibrate)
    ImageView ivCloseVibrate;
    @Bind(R.id.rl_switch_vibrate)
    RelativeLayout rlSwitchVibrate;
    @Bind(R.id.btn_logout)
    Button btnLogout;

    SharePreferenceUtil mSpUtil;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.view2)
    View view2;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        String userName = UserModel.getInstance().getCurrentUser().getUsername();
        tvSetName.setText(TextUtils.isEmpty(userName) ? "" : userName);
        mSpUtil = BmobIMApplication.INSTANCE().getSpUtil();
        initView();
        return view;
    }

    private void initView() {
        if (mSpUtil.isAllowPushNotify()) {
            ivOpenNotification.setVisibility(View.VISIBLE);
            if (mSpUtil.isAllowVibrate()) {
                ivOpenVibrate.setVisibility(View.VISIBLE);
                ivCloseVibrate.setVisibility(View.GONE);
            } else {
                ivOpenVibrate.setVisibility(View.GONE);
                ivCloseVibrate.setVisibility(View.VISIBLE);
            }
            if (mSpUtil.isAllowVoice()) {
                ivOpenVoice.setVisibility(View.VISIBLE);
                ivCloseVoice.setVisibility(View.GONE);
            } else {
                ivOpenVoice.setVisibility(View.GONE);
                ivCloseVoice.setVisibility(View.VISIBLE);
            }
        } else {
            ivOpenNotification.setVisibility(View.GONE);
            ivCloseNotification.setVisibility(View.VISIBLE);
            ivOpenVoice.setVisibility(View.GONE);
            ivCloseVoice.setVisibility(View.GONE);
            ivOpenVibrate.setVisibility(View.GONE);
            ivCloseVibrate.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_logout)
    public void onLoginOutClick() {
        UserModel.getInstance().logout();
        BmobIM.getInstance().disConnect();
        getActivity().finish();
        startActivity(LoginActivity.class, null);
    }

    @OnClick(R.id.rl_switch_notification)
    public void onNotifiSwitchClick() {
        if (ivOpenNotification.getVisibility() == View.VISIBLE) {
            ivOpenNotification.setVisibility(View.GONE);
            ivCloseNotification.setVisibility(View.VISIBLE);
            rlSwitchVibrate.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            rlSwitchVoice.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            mSpUtil.setPushNotifyEnable(false);
        } else {
            ivOpenNotification.setVisibility(View.VISIBLE);
            ivCloseNotification.setVisibility(View.GONE);
            rlSwitchVibrate.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            rlSwitchVoice.setVisibility(View.VISIBLE);
            mSpUtil.setPushNotifyEnable(true);
            if (mSpUtil.isAllowVibrate()) {
                ivOpenVibrate.setVisibility(View.VISIBLE);
                ivCloseVibrate.setVisibility(View.GONE);
            } else {
                ivOpenVibrate.setVisibility(View.GONE);
                ivCloseVibrate.setVisibility(View.VISIBLE);
            }
            if (mSpUtil.isAllowVoice()) {
                ivOpenVoice.setVisibility(View.VISIBLE);
                ivCloseVoice.setVisibility(View.GONE);
            } else {
                ivOpenVoice.setVisibility(View.GONE);
                ivCloseVoice.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.rl_switch_voice)
    public void onVoiceSwitchClick() {
        if (ivOpenNotification.getVisibility() == View.VISIBLE) {
            if (ivOpenVoice.getVisibility() == View.VISIBLE) {
                ivOpenVoice.setVisibility(View.GONE);
                ivCloseVoice.setVisibility(View.VISIBLE);
                mSpUtil.setVioceEnable(false);
            } else {
                ivOpenVoice.setVisibility(View.VISIBLE);
                ivCloseVoice.setVisibility(View.GONE);
                mSpUtil.setVioceEnable(true);
            }
        }
    }

    @OnClick(R.id.rl_switch_vibrate)
    public void onVibrateSwitchClick() {
        if (ivOpenNotification.getVisibility() == View.VISIBLE) {
            if (ivOpenVibrate.getVisibility() == View.VISIBLE) {
                ivOpenVibrate.setVisibility(View.GONE);
                ivCloseVibrate.setVisibility(View.VISIBLE);
                mSpUtil.setVibrateEnable(false);
            } else {
                ivOpenVibrate.setVisibility(View.VISIBLE);
                ivCloseVibrate.setVisibility(View.GONE);
                mSpUtil.setVibrateEnable(true);
            }
        }
    }

    @OnClick(R.id.layout_blacklist)
    public void onBlacklistLayoutClick() {
    }

    @OnClick(R.id.layout_info)
    public void onMyInfoClick() {
        Bundle bundle = new Bundle();
        bundle.putString("from", "me");
        User user = UserModel.getInstance().getCurrentUser();
        bundle.putSerializable("user",user);
        startActivity(SetMyInfoActivity.class,bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
