package com.houxy.wechat.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houxy.wechat.base.FragmentBase;
import com.houxy.wechat.R;
import com.houxy.wechat.ui.fragment.ContactsFragment;
import com.houxy.wechat.ui.fragment.ConversationFragment;
import com.houxy.wechat.ui.fragment.SettingFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

;

/**
 * @author :  houxy
 * @date :  2016/4/2 17:29
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String[] titles = new String[]{"会话", "联系人", "设置"};
    private FragmentBase[] fragmentBase = {ConversationFragment.newInstance(),
            ContactsFragment.newInstance(),
            SettingFragment.newInstance()};

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ConversationFragment();
            case 1:
                return new ContactsFragment();
            case 2:
                return new SettingFragment();
            default:break;
        }
        return null;
        //return fragmentBase[position];

    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    int[] imageResId = {R.drawable.tab_message, R.drawable.tab_contact, R.drawable.tab_setting};

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(titles[position]);
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);
        iv.setImageResource(imageResId[position]);
        if (position == 0) {
            v.setSelected(true);
        }
        return v;
    }

}
