package com.houxy.wechat.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.houxy.wechat.BmobIMApplication;
import com.houxy.wechat.base.FragmentBase;
import com.houxy.wechat.R;
import com.houxy.wechat.adapter.ConversationAdapter;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.ui.ChatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * 联系人Fragemnt
 *
 * @author :  houxy
 * @date :  2016/4/2 17:05
 */
public class ConversationFragment extends FragmentBase {

    @Bind(R.id.rc_view)
    RecyclerView rcView;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout swRefresh;
    private ConversationAdapter mAdapter;
    private View view;

    public static ConversationFragment newInstance() {
        ConversationFragment conversationFragment = new ConversationFragment();
        return conversationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        setListener();
        Log.d("hhh", "ConversationFragment");
        return view;
    }

    private void initRecyclerView() {
        //初始化RecyclerView
        mAdapter = new ConversationAdapter();
        rcView.setAdapter(mAdapter);
        rcView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setListener() {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                swRefresh.setRefreshing(true);
                query();
            }
        });
        //设置recyclerView item的点击事件
        mAdapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                BmobIMConversation c = mAdapter.getItem(position);
//                BmobIMMessage message = c.getMessages().get(0);
//                Logger.d(message.getMsgType());
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", c);
                startActivity(ChatActivity.class, bundle);
            }

            @Override
            public boolean onItemLongClick(int position) {

                return true;
            }
        });
        //设置下拉刷新
        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });

    }

    public void query(){
        mAdapter.bindDatas(BmobIM.getInstance().loadAllConversation());
        mAdapter.notifyDataSetChanged();
        swRefresh.setRefreshing(false);
    }


    @Subscribe
    public void OnEventMainThread(MessageEvent event){
        mAdapter.bindDatas(BmobIM.getInstance().loadAllConversation());
        runOnMain(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe
    public void OnEventMainThread(OfflineMessageEvent event){
        mAdapter.bindDatas(BmobIM.getInstance().loadAllConversation());
        runOnMain(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        swRefresh.setRefreshing(true);
        query();
    }
}
