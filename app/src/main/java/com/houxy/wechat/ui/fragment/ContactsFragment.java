package com.houxy.wechat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.R;
import com.houxy.wechat.adapter.ContactsAdapter;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.base.FragmentBase;
import com.houxy.wechat.bean.User;
import com.houxy.wechat.event.AddEvent;
import com.houxy.wechat.ui.ChatActivity;
import com.houxy.wechat.ui.NewFriendActivity;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * @author :  houxy
 * @date :  2016/4/5 16:35
 */
public class ContactsFragment extends FragmentBase {

    @Bind(R.id.rc_view)
    RecyclerView rcView;
    ContactsAdapter adapter;
    @Bind(R.id.iv_newfriend)
    ImageView ivNewfriend;
    @Bind(R.id.rl_newfriend)
    RelativeLayout rlNewfriend;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout swRefresh;

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);
        adapter = new ContactsAdapter();
        initRecyclerView();
        initListener();
        return view;
    }

    private void initRecyclerView() {
        rcView.setAdapter(adapter);
        rcView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initListener() {
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                User user =adapter.getItem(position);
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation c, BmobException e) {
                        if (e == null) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("c", c);
                            startActivity(ChatActivity.class, bundle);
                        } else {
                            toast("(" + e.getErrorCode() + ")" + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.addUsers(BmobIMApplication.INSTANCE().getContactList());
                adapter.notifyDataSetChanged();
                swRefresh.setRefreshing(false);
            }
        });
    }

    BmobIMMessage message;
    @Subscribe
    public void OnEventMainThread(AddEvent event) {
        message = event.getMessage();
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.rl_newfriend)
    public void onClick() {
        Intent intent = new Intent(getContext(), NewFriendActivity.class);
        if (null != message) {
            intent.putExtra("message", message);
            message = null;
        }
        startActivity(intent);
    }
}
