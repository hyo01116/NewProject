package com.example.newproject;

public class UserLocationInfo {
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
