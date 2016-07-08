package com.houxy.wechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.houxy.wechat.R;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.adapter.SearchUserAdapter;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.event.ChatEvent;
import com.houxy.wechat.event.SetInfoEvent;
import com.houxy.wechat.model.UserModel;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Houxy on 2016/4/23.
 */
public class SearchUserActivity extends ParentWithNaviActivity {

    @Bind(R.id.et_find_name)
    EditText etFindName;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.rc_view)
    RecyclerView rcView;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout swRefresh;

    SearchUserAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    public String title() {
        return "查找好友";
    }

    @Override
    public ToolBarListener setToolBarListener() {
        return new ToolBarListener() {
            @Override
            public void onClickLeft() {
                finish();
            }

            @Override
            public void onClickRight() {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchuser);
        ButterKnife.bind(this);
        initNaviView();
        adapter = new SearchUserAdapter();
        rcView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rcView.setLayoutManager(layoutManager);
        initListener();
    }

    private void initListener() {
        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });

        rcView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                hideSoftInputView();//隐藏输入法
                return true;
            }
        });
    }

    @Subscribe
    public void onEventMainThread(ChatEvent event) {
        BmobIMUserInfo info = event.getInfo();
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if(e == null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle, false);
                }else {
                    toast("(" + e.getErrorCode() + ")"+ e.getMessage());
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(SetInfoEvent event) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", event.getUser()); //Logger.d(info.getName() + ":" + info.getUserId());
        bundle.putString("from", "add");
        startActivity(SetMyInfoActivity.class, bundle, false);
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick() {
        swRefresh.setRefreshing(true);
        query();
    }

    private void query() {
        String username = etFindName.getText().toString();
        if(TextUtils.isEmpty(username)){
            toast("请输入姓名");
            swRefresh.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(username, UserModel.DEFAULT_LIMIT, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                swRefresh.setRefreshing(false);
                adapter.setDatas(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                swRefresh.setRefreshing(false);
                toast(i + "(" + s + ")");
            }
        });
    }
}
