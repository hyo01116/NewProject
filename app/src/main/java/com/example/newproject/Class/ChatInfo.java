package com.example.newproject.Class;

import android.widget.TextView;

import java.io.Serializable;

public class ChatInfo implements Serializable {
    //채팅시에는 유저 아이디, 정보, 시간, 사진 불러옴
    private String data;
    private String uid;

    public ChatInfo(String uid, String data){
        this.data = data;
        this.uid = uid;
    }
    public ChatInfo(){

    }
    public String getUid() {return this.uid; }
    public void setUid(String uid){ this.uid = uid;}
    public String getData(){ return this.data; }
    public void setData(String data){ this.data = data;}

}
