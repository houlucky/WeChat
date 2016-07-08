package com.houxy.wechat.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.houxy.wechat.R;
import com.houxy.wechat.adapter.NewFriendAdapter;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.base.BaseActivity;
import com.houxy.wechat.event.AddEvent;


import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by Houxy on 2016/5/2.
 */
public class NewFriendActivity extends ParentWithNaviActivity {

    @Bind(R.id.rc_view)
    RecyclerView rcView;

    NewFriendAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfriend);
        ButterKnife.bind(this);
        adapter = new NewFriendAdapter();
        initRecyclerView();
        initListener();
        initNaviView();
    }

    private void initRecyclerView() {
        rcView.setAdapter(adapter);
        rcView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void initListener() {
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        if(null != getIntent().getSerializableExtra("message") ){
            BmobIMMessage message = (BmobIMMessage) getIntent().getSerializableExtra("message");
            adapter.addMessage(message);
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        super.onStart();
    }

    @Override
    public String title() {
        return "新的朋友";
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
}
