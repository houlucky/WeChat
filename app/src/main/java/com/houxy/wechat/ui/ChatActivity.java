package com.houxy.wechat.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.houxy.wechat.R;
import com.houxy.wechat.adapter.ChatAdapter;
import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

/**
 * @author :  houxy
 * @date :  2016/4/6 22:27
 */
public class ChatActivity extends ParentWithNaviActivity implements ObseverListener {


    BmobRecordManager recordManager;
    BmobIMConversation c;
    LinearLayoutManager layoutManager;
    ChatAdapter chatAdapter;
    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.rc_view)
    RecyclerView rcView;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout swRefresh;
    @Bind(R.id.iv_record)
    ImageView ivRecord;
    @Bind(R.id.tv_voice_tips)
    TextView tvVoiceTips;
    @Bind(R.id.layout_record)
    RelativeLayout layoutRecord;
    @Bind(R.id.edit_msg)
    EditText editMsg;
    @Bind(R.id.btn_chat_send)
    Button btnChatSend;
    @Bind(R.id.btn_chat_record)
    Button btnChatRecord;
    @Bind(R.id.btn_caht_ptv)
    Button btnCahtPtv;
    @Bind(R.id.btn_chat_pic)
    Button btnChatPic;
    @Bind(R.id.btn_chat_camera)
    Button btnChatCamera;
    @Bind(R.id.btn_chat_hongbao)
    Button btnChatHongbao;
    @Bind(R.id.btn_chat_emo)
    Button btnChatEmo;
    @Bind(R.id.btn_chat_plus)
    Button btnChatPlus;
    @Bind(R.id.pager_emo)
    ViewPager pagerEmo;
    @Bind(R.id.layout_emo)
    LinearLayout layoutEmo;
    @Bind(R.id.tv_picture)
    TextView tvPicture;
    @Bind(R.id.tv_camera)
    TextView tvCamera;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.layout_more)
    LinearLayout layoutMore;
    @Bind(R.id.ll_chat)
    LinearLayout llChat;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;

    @Override
    public String title() {
        return c.getConversationTitle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        c = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getBudle().getSerializable("c"));
        initNaviView();
        initSwipLayoutView();
        initBottomView();
    }

    private void initBottomView() {
        btnChatSend.setBackgroundResource(R.mipmap.send_button_disabled);
        editMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
                btnChatSend.setBackgroundResource(R.mipmap.send_button_disabled);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    btnChatSend.setBackgroundResource(R.mipmap.send_button_disabled);
                } else {
                    btnChatSend.setBackgroundResource(R.drawable.btn_chat_send_selector);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    scrollToBottom();
                }
                return false;
            }
        });
    }

    private void initSwipLayoutView() { //点击空白处隐藏输入法和layoutMore；
        layoutManager = new LinearLayoutManager(this);
        rcView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(this, c);
        rcView.setAdapter(chatAdapter);
        llChat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llChat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                swRefresh.setRefreshing(true);
                //自动刷新
                queryMessage(null);
            }
        });
        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage message = chatAdapter.getFirstMessage();
                queryMessage(message);
            }
        });

        chatAdapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        rcView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                hideSoftInputView();//隐藏输入法
                return false;
            }
        });
    }

    private void queryMessage(BmobIMMessage message) {
        swRefresh.setRefreshing(false);
        c.queryMessages(message, 10, new MessagesQueryListener() {//10表示查询10条消息
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        chatAdapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });

    }

    MessageSendListener sendListener = new MessageSendListener() {

        @Override
        public void onProgress(int i) {
            super.onProgress(i);
        }

        @Override
        public void onStart(BmobIMMessage bmobIMMessage) {
            super.onStart(bmobIMMessage);
            chatAdapter.addMessage(bmobIMMessage);
            editMsg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
            chatAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    };

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layoutMore.getVisibility() == View.VISIBLE) {
                layoutMore.setVisibility(View.GONE);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        Logger.i("接收到消息：" + msg.getContent());
        if (c != null && event != null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (chatAdapter.findPosition(msg) < 0) {//如果未添加到界面中
                chatAdapter.addMessage(msg);
                runOnMain(new Runnable() {
                    @Override
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                        scrollToBottom();
                    }
                });
                //更新该会话下面的已读状态
                c.updateReceiveStatus(msg);
            }

        } else {
            Logger.i("不是与当前聊天对象的消息");
        }
    }

    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                addMessage2Chat(cache.get(i));
            }
        }
        scrollToBottom();
    }

    @OnClick(R.id.btn_chat_send)
    public void onSendClick() {
        SendMessage();
    }

    private void SendMessage() {
        String msg = editMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        BmobIMTextMessage message = new BmobIMTextMessage();
        message.setContent(msg);
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        message.setExtraMap(map);
        c.sendMessage(message, sendListener);
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        addMessage2Chat(event);
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(chatAdapter.getItemCount() - 1, 0);
    }

    @Override
    protected void onResume() {
        addUnReadMessage();
        BmobNotificationManager.getInstance(this).addObserver(this);
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    @Override
    protected void onPause() {
        BmobNotificationManager.getInstance(this).removeObserver(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // recordManager.clear();//清理资源
        c.updateLocalCache();//更新此会话的所有消息为已读
        super.onDestroy();
    }

}
