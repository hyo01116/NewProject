package com.example.newproject;

public class UserWriteInfo {
    private String key;
    public UserWriteInfo(String key){
        this.key = key;
    }
    public UserWriteInfo(){

    }
    public String getKey(){ return key;}
    public void setKey(String key){ this.key = key;}
}
