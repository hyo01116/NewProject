package com.example.newproject;

public class ChatInfo {
    private String uid;
    private String data;

    public ChatInfo(String uid, String data){
        this.uid = uid;
        this.data = data;
    }
    public ChatInfo(){

    }
    public String getUid(){ return this.uid; }
    public void setUid(String uid){ this.uid = uid; }
    public String getData(){ return this.data; }
    public void setData(String data){ this.data = data;}
}
