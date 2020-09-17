package com.example.newproject.Class;

import android.net.Uri;

import java.io.File;

public class FeedInfo {
    String userid;
    String localurl;
    String localname;
    String picture;
    String extratext;
    String address;
    String phone;

    public FeedInfo(String userid, String localurl, String localname, String address, String phone, String picture, String extratext) {
        this.userid = userid;
        this.localurl = localurl;
        this.localname = localname;
        this.picture = picture;
        this.extratext = extratext;
        this.address = address;
        this.phone = phone;

    }
    public FeedInfo(String userid, File localurl, String localname, String address, String phone, String extratext){

    }

    public FeedInfo() {

    }

    public String getUserid(){ return userid; }
    public void setUserid(String userid){ this.userid = userid; }

    public String  getLocalurl(){ return localurl; }
    public void setLocalurl(String localurl){ this.localurl = localurl; }

    public String getLocalname(){ return localname; }
    public void setLocalname(String localname){ this.localname = localname;}

    public String getAddress() {return address;}
    public void setAddress(String address){ this.address = address; }

    public String getPhone(){ return phone;}
    public void setPhone(String phone){ this.phone = phone; }

    public String getPicture(){ return picture; }
    public void setPicture(String picture){ this.picture = picture; }

    public String getExtratext(){ return extratext;}
    public void setExtratext(String extratext ){ this.extratext = extratext; }

}
