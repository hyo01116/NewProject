package com.example.newproject;

import android.app.Application;

public class MyApplication extends Application {

    private String user_level;
    public String getUser_level(){
        return user_level;
    }

// 타 class에서 변경한 valuable을 MyApplication 에 저장

    public void setUser_level(String mValue){
        this.user_level = mValue;
    }
}
