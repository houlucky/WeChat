package com.houxy.wechat.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.base.BaseActivity;
import com.houxy.wechat.R;
import com.houxy.wechat.adapter.MyFragmentPagerAdapter;
import com.houxy.wechat.bean.User;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class MainActivity extends BaseActivity implements ObseverListener{

    @Bind(R.id.iv_find)
    ImageView ivFind;
    @Bind(R.id.iv_tianjia)
    ImageView ivTianjia;
    @Bind(R.id.main_viewPager)
    ViewPager viewPager;
    @Bind(R.id.main_tablayout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSwipeBackLayout().setEnableGesture(false);//设置主活动默认不能滑动退出
        ButterKnife.bind(this);
        initTabLayout();
        User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Logger.i("connect success");
                }else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });

        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus connectionStatus) {
                Logger.i(connectionStatus.getMsg());
            }
        });
        Log.d("hhh", "MainActivity");
    }

    private void initTabLayout() {  //Fragment+ViewPager+FragmentViewPager组合的使用
        MyFragmentPagerAdapter mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mAdapter.getTabView(i));
            }
        }
    }

    @OnClick({R.id.iv_find, R.id.iv_tianjia}) //使用ButterKnife后我并没有setOnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_find:
                Toast.makeText(getApplicationContext(), "find", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_tianjia:
                startActivity(SearchUserActivity.class, null,false);
                break;
        }
    }

    @Override
    protected void onResume() {

        BmobNotificationManager.getInstance(this).addObserver(this);
        BmobNotificationManager.getInstance(this).cancelNotification();

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BmobNotificationManager.getInstance(this).removeObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BmobIM.getInstance().clear();
        BmobNotificationManager.getInstance(this).clearObserver();
    }

    private static long firstTime;
    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (firstTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            toast("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }
}
