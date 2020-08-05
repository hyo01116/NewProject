package com.example.newproject;

public class ChatInfo {
    private String uid;
    private String data;
    private String time;
    private String profile;

    public ChatInfo(String uid, String time, String profile, String data){
        this.uid = uid;
        this.data = data;
        this.profile = profile;
        this.time = time;
    }
    public ChatInfo(){

    }
    public String getUid(){ return this.uid; }
    public void setUid(String uid){ this.uid = uid; }
    public String getData(){ return this.data; }
    public void setData(String data){ this.data = data;}
    public String getTime(){ return this.time;}
    public void setTime(String time){ this.time = time;}
    public String getProfile(){ return this.profile;}
    public void setProfile(String profile){ this.profile = profile;}
}
