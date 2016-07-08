package com.houxy.wechat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.houxy.wechat.adapter.holder.BaseViewHolder;
import com.houxy.wechat.adapter.holder.ReceivedAgreeAddHolder;
import com.houxy.wechat.adapter.holder.SendAgreeAddHolder;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by Houxy on 2016/5/2.
 */
public class NewFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPR_RECEIVERD_AGREE = 0;
    private final int TYPE_RECEIVERD_ADD = 1;

    public List<BmobIMMessage> messages = new ArrayList<>();
    private OnRecyclerViewListener listener;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if( TYPE_RECEIVERD_ADD == viewType){
            Logger.d("received add");
            return new SendAgreeAddHolder(parent.getContext(), parent, listener);
        }else if(TYPR_RECEIVERD_AGREE == viewType){
            Logger.d("agree add");
            return new ReceivedAgreeAddHolder(parent.getContext(), parent, listener);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if("addFriend".equals(messages.get(position).getMsgType())){
            return TYPE_RECEIVERD_ADD;
        }else if("agreeAdd".equals(messages.get(position).getMsgType())){
            return TYPR_RECEIVERD_AGREE;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener){
        listener = onRecyclerViewListener;
    }

    public void addMessage(BmobIMMessage message){
        messages.add(message);
    }
}
