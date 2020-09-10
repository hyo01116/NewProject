package com.example.newproject.Class;

public class UserLocationInfo {
    //extends userlevelinfo를 하게 된다면 위치 정보에 레벨 정보까지 더해진것
    public String first;
    public String second;
    public String third;

    public UserLocationInfo(String first, String second, String third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
    public UserLocationInfo(){

    }

    public String getFirst() { return first; }
    public void setFirst(String first){ this.first = first;}

    public String getSecond(){ return second;}
    public void setSecond(String second){ this.second = second;}

    public String getThird(){ return third;}
    public void setThird(String third){ this.third= third; }
}
