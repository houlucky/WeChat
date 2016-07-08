package com.houxy.wechat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.houxy.wechat.adapter.holder.BaseViewHolder;
import com.houxy.wechat.adapter.holder.SearchUserHolder;
import com.houxy.wechat.bean.User;
import com.nostra13.universalimageloader.utils.L;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Houxy on 2016/4/23.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<User> users = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchUserHolder(parent.getContext(), parent, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setDatas(List<User> list) {
        users.clear();
        if(list != null && list.size()>0){
            users.addAll(list);
        }
    }

    public OnRecyclerViewListener onRecyclerViewListener;
    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener){
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

}
