package com.example.newproject;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;

public class MyApplication extends Application {

    private String user_level;
    public String getUser_level(){
        return user_level;
    }

// 타 class에서 변경한 valuable을 MyApplication 에 저장

    public void setUser_level(String mValue){
        this.user_level = mValue;
    }

    private static volatile MyApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static MyApplication getMyApplicationContext() {
        return obj;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        MyApplication.currentActivity = currentActivity;
    }
}
