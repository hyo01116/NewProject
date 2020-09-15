package com.example.newproject.Class;

import java.io.Serializable;

public class ChatInfo implements Serializable {
    //채팅시에는 유저 아이디, 정보, 시간, 사진 불러옴
    private String data;
    private String nickname;
    private String uid;

    public ChatInfo(String uid, String nickname, String data){
        this.data = data;
        this.uid = uid;
        this.nickname = nickname;
    }
    public ChatInfo(){

    }
    public String getUid() {return this.uid; }
    public void setUid(String uid){ this.uid = uid;}
    public String getData(){ return this.data; }
    public void setData(String data){ this.data = data;}
    public String getNickname(){ return this.nickname; }
    public void setNickname(String nickname){ this.nickname = nickname; }
}
