package com.houxy.wechat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.houxy.wechat.R;
import com.houxy.wechat.adapter.holder.BaseViewHolder;
import com.houxy.wechat.adapter.holder.ConversationViewHolder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * @author :  houxy
 * @date :  2016/4/5 11:01
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int TYPE_CONVERSATION_NORMAL = 1;
    private final int TYPE_CONVERSATION_ADDFRIEND = 2;

    private List<BmobIMConversation> conversations = new ArrayList<>();

    public ConversationAdapter(){
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationViewHolder(parent.getContext(), parent, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(conversations.get(position));
    }


    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public BmobIMConversation getItem(int postion){
       try {
           return conversations.get(postion);
       }catch (Exception e){
           Logger.d(""+ postion);
       }
        return conversations.get(0);
    }


    public void remove(int postion){
        conversations.remove(postion);
        notifyDataSetChanged();
    }

    public void bindDatas(List<BmobIMConversation> list){
        conversations.clear();
        if (list != null){
            conversations.addAll(list);
        }
    }

    public void bindData(BmobIMConversation conversation){
        if (conversation != null){
            conversations.add(conversation);
        }
    }


    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener){
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

}
