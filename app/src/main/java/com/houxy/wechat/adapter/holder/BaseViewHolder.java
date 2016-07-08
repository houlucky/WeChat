package com.houxy.wechat.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.houxy.wechat.adapter.OnRecyclerViewListener;
import com.houxy.wechat.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * @author :  houxy
 * @date :  2016/4/6 9:32
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public OnRecyclerViewListener onRecyclerViewListener;

    public BaseViewHolder(Context context,ViewGroup root, int res, OnRecyclerViewListener onRecyclerViewListener) {
        super(LayoutInflater.from(context).inflate(res, root, false));
        this.onRecyclerViewListener = onRecyclerViewListener;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public abstract void bindData(T t);

    @Override public void onClick(View v) {
        if (onRecyclerViewListener != null){
            onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
    }

    @Override public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null){
            onRecyclerViewListener.onItemLongClick(getAdapterPosition());
        }
        return true;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    private Toast toast;
    public void toast(final Object obj) {
        try {
            ((BaseActivity)getContext()).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(getContext(),"", Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
