package com.houxy.wechat.util;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.logger.Logger;

/**
 * Created by asus on 2016/4/13.
 */
public class ViewUtil {

    public static void setAvatar(String avatar, int defaultRes, ImageView iv){
        if(!TextUtils.isEmpty(avatar)){
            if(!avatar.equals(iv.getTag())){
                iv.setTag(avatar);
                ImageAware imageAware = new ImageViewAware(iv, false);
                ImageLoader.getInstance().displayImage(avatar, imageAware, DisplayConfig.getDefaultOptions(false, defaultRes));

            }
        }else {
            iv.setImageResource(defaultRes);
        }
    }

    public static void setPicture(String url,int defaultRes,ImageView iv,ImageLoadingListener listener){
        if(!TextUtils.isEmpty(url)) {
            if (!url.equals(iv.getTag())) {//增加tag标记，减少UIL的display次数
                iv.setTag(url);
                //不直接display imageview改为ImageAware，解决ListView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(iv, false);
                if(listener!=null){
                    ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfig.getDefaultOptions(false, defaultRes), listener);
                }else{
                    ImageLoader.getInstance().displayImage(url, imageAware, DisplayConfig.getDefaultOptions(false, defaultRes));
                }
            }
        }else{
            iv.setImageResource(defaultRes);
        }
    }



    public static void setAvatar(Intent data,ImageView ivSetAvatar) {
        try {
            Logger.d(data.getData().toString());
            ivSetAvatar.setImageURI(data.getData());
        }catch (Exception e){
            Logger.e(e);
        }
    }
}
