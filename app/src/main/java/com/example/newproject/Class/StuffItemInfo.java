package com.example.newproject.Class;

import java.io.Serializable;

public class StuffItemInfo implements Serializable {
    private String userid;        //cloud 접근해서 (사진, 회사명, 주소)
    private String localname;     //회사명
    private String phone;
    private String address;
    private String localurl;      //cloud이미지
    private String imageurl;      //이미지 (없으면 cloud이미지)
    private String textname;      //제목
    private String extratext;     //상세설명
    private String state;         //open, close
    private String key;           //키 -> 사용자별 작성글에서 key저장
    private String noti;
    private String datelimit;
    private String day;

    public StuffItemInfo(String userid, String day, String noti, String datelimit, String phone, String address, String localname, String localurl, String imageurl, String textname, String extratext, String state, String key) {
        this.userid = userid;
        this.day = day;
        this.noti = noti;
        this.datelimit = datelimit;
        this.localname = localname;
        this.address =address;
        this.phone = phone;
        this.localurl = localurl;
        this.imageurl = imageurl;
        this.textname = textname;
        this.extratext = extratext;
        this.state = state;
        this.key = key;
    }

    public StuffItemInfo(){

    }

    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

    public String getDay(){ return day;}
    public void setDay(String day){ this.day = day; }

    public String getNoti(){ return noti; }
    public void setNoti(String noti){ this.noti = noti; }

    public String getDatelimit(){ return datelimit; }
    public void setDatelimit(String datelimit){ this.datelimit = datelimit; }

    public String getAddress(){ return address;}
    public void setAddress(String address){ this.address = address;}

    public String getLocalname(){ return localname; }
    public void setLocalname(String localname){ this.localname = localname;}

    public String getPhone(){ return phone; }
    public void setPhone(String phone){this.phone = phone;}

    public String getLocalurl() { return localurl; }
    public void setLocalurl(String localurl) { this.localurl = localurl; }

    public String getTextname() { return textname; }
    public void setTextname(String textname) { this.textname = textname;}


    public String getExtratext() { return extratext; }
    public void setExtratext(String extratext){ this.extratext = extratext;}

    public String getImageurl() { return imageurl; }
    public void setImageurl(String imageurl){ this.imageurl = imageurl;}

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state;}
}
