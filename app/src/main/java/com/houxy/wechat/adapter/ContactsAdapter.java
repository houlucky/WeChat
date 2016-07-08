package com.houxy.wechat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.houxy.wechat.adapter.holder.BaseViewHolder;
import com.houxy.wechat.adapter.holder.ContactViewHolder;
import com.houxy.wechat.adapter.holder.ReceivedAgreeAddHolder;
import com.houxy.wechat.adapter.holder.SendAgreeAddHolder;
import com.houxy.wechat.bean.User;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Houxy on 2016/5/1.
 */
public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<User> users = new ArrayList<>();
    private OnRecyclerViewListener listener;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ContactViewHolder(parent.getContext(), parent, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener){
        listener = onRecyclerViewListener;
    }

    public void addUsers(List<User> users){
        this.users.clear();
        if(null != users){
            this.users.addAll(users);
        }
    }

    public User getItem(int position) {
        return users.get(position);
    }
}
