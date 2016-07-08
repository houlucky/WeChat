package com.houxy.wechat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.View;


import com.houxy.wechat.bean.User;
import com.houxy.wechat.model.UserModel;
import com.houxy.wechat.util.SharePreferenceUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by asus on 2016/4/10.
 */
public class BmobIMApplication extends Application {
    private static BmobIMApplication INSTANCE = null;
    private static List<User> users = new ArrayList<>();
    public static BmobIMApplication INSTANCE(){
        return INSTANCE;
    }

    public void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        Logger.init("houxy");
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(new MyMessageHandler(this));
        initImageLoader(this);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.memoryCache(new WeakMemoryCache());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public  List<User> getContactList(){
        return users;
    }

    public  void setContactList(List<User> users) {
        this.users = users;
    }

    public  void setContactList(User user) {
        this.users.add(user);
    }

    private SharePreferenceUtil mSpUtil;
    private static final String PREFERENCE_NAME = "_shareInfo";
    public synchronized SharePreferenceUtil getSpUtil(){
        if(null == mSpUtil){
            String objectId = UserModel.getInstance().getCurrentUser().getObjectId();
            String sharedName = objectId + PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    private MediaPlayer mediaPlayer;
    public synchronized MediaPlayer getMediaPlayer(){
        if(null == mediaPlayer){
            mediaPlayer = MediaPlayer.create(this, R.raw.notify);
        }
        return mediaPlayer;
    }
}
