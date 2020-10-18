package com.example.newproject.Class;

import android.widget.TextView;

import java.io.Serializable;

public class ChatInfo implements Serializable {
    //채팅시에는 유저 아이디, 정보, 시간, 사진 불러옴
    private String data;
    private String profile;
    private String date;
    private String uid;

    public ChatInfo(String uid, String data, String profile, String date){
        this.data = data;
        this.uid = uid;
        this.profile = profile;
        this.date = date;
    }
    public ChatInfo(){

    }
    public String getUid() {return this.uid; }
    public void setUid(String uid){ this.uid = uid;}
    public String getData(){ return this.data; }
    public void setData(String data){ this.data = data;}
    public String getProfile(){ return this.profile; }
    public void setProfile(String profile){ this.profile = profile; }
    public String getDate(){ return this.date; }
    public void setDate(String date){ this.date = date; }
}
