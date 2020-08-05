package com.example.newproject;

public class FeedInfo {
    String userid;
    String localurl;
    String localname;
    String picture;
    String extratext;

    public FeedInfo(String userid, String localurl, String localname, String picture, String extratext) {
        this.userid = userid;
        this.localurl = localurl;
        this.localname = localname;
        this.picture = picture;
        this.extratext = extratext;
    }
    public FeedInfo(String userid, String localurl, String localname, String extratext){

    }

    public FeedInfo() {

    }

    public String getUserid(){ return userid; }
    public void setUserid(String userid){ this.userid = userid; }

    public String getLocalurl(){ return localurl; }
    public void setLocalurl(String localurl){ this.localurl = localurl; }

    public String getLocalname(){ return localname; }
    public void setLocalname(String localname){ this.localname = localname;}

    public String getPicture(){ return picture; }
    public void setPicture(String picture){ this.picture = picture; }

    public String getExtratext(){ return extratext;}
    public void setExtratext(String extratext ){ this.extratext = extratext; }
}
