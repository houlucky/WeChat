package com.houxy.wechat.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.houxy.wechat.base.BaseActivity;
import com.houxy.wechat.R;

/**
 * @author :  houxy
 * @date :  2016/4/7 11:04
 */
public abstract class ParentWithNaviActivity extends BaseActivity{

    private ImageView tvLeft;
    private TextView tvRight;
    private TextView tvTitle;
    private ToolBarListener toolBarListener;
    public Object left(){
        return null;
    }

    public abstract String title();

    public Object right(){
        return null;
    }

    public void initNaviView(){
        tvLeft = getView(R.id.tv_left);
        tvRight = getView(R.id.tv_right);
        tvTitle = getView(R.id.tv_title);
        setNaviListener(setToolBarListener());
        tvLeft.setOnClickListener(onClickListener);
        tvRight.setOnClickListener(onClickListener);
        refreshTop();
    }

    private void refreshTop() {
        setLeftView(left() == null ? R.drawable.base_action_bar_back_bg_selector : left());
        setRightView(R.id.tv_right, right());
        tvTitle.setText(title());
    }

    private void setLeftView(Object obj) {
        if(obj !=null && !obj.equals("")){
            tvLeft.setVisibility(View.VISIBLE);
            if(obj instanceof Integer){
                tvLeft.setImageResource(Integer.parseInt(obj.toString()));
            }else{
                tvLeft.setImageResource(R.drawable.base_action_bar_back_bg_selector);
            }
        }else{
            tvLeft.setVisibility(View.INVISIBLE);
        }
    }

    private void setRightView(int id, Object obj) {
        if (obj != null && !obj.equals("")) {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
            if (obj instanceof String) {
                ((TextView) getView(id)).setText(obj.toString());
            } else if (obj instanceof Integer) {
                getView(id).setBackgroundResource(Integer.parseInt(obj.toString()));
            }
        } else {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void setNaviListener(ToolBarListener toolBarListener) {
        this.toolBarListener = toolBarListener;
    }

    public ToolBarListener setToolBarListener() {
        return null;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_left:
                    if(toolBarListener == null){
                        finish();
                    }else {
                       toolBarListener.onClickLeft();
                    }
                    break;
                case R.id.tv_right:
                    if (toolBarListener != null){
                        toolBarListener.onClickRight();
                    }
                    break;
                default:break;
            }
        }
    };



    private <T extends View> T getView(int id){
        return (T)findViewById(id);
    }

    public interface ToolBarListener{
         void onClickLeft();
         void onClickRight();
    }
}
