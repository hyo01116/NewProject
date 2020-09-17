package com.example.newproject.Class;

import android.app.Service;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class ServiceItemInfo implements Parcelable {
    //parcelable사용시에는 writetoparcel, describecontent를 정의
    //봉사활동에 대한 정보를 데이터베이스에서 받아올 때 사용하는 객체
    //클래스는 다른 액티비티에서 정의해서 사용함
    private String userid;        //cloud 접근해서 (사진, 회사명, 주소)
    private String localname;     //회사명
    private String localurl;      //cloud이미지
    private String imageurl;      //이미지 (없으면 cloud이미지)
    private String textname;      //제목
    private String extratext;     //상세설명
    private String state;         //open, close
    private String key;           //키 -> 사용자별 작성글에서 key저장
    private String noti;
    private String datelimit;
    private String day;

    public ServiceItemInfo(String userid,String day, String noti, String datelimit, String localname, String localurl, String imageurl, String textname, String extratext, String state, String key) {
        this.userid = userid;
        this.localname = localname;
        this.day = day;
        this.noti = noti;
        this.datelimit = datelimit;
        this.localurl = localurl;
        this.imageurl = imageurl;
        this.textname = textname;
        this.extratext = extratext;
        this.state = state;
        this.key = key;
    }

    //생성자 : 필드의 값을 초기화시켜주는 것 (this는 클래스 내부의 필드의 값, 매개변수로 받아온 값을 this를 통해 객체의 필드 값을 초기화 시켜줌)
    public ServiceItemInfo() {

    }

    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDay(){ return day;}
    public void setDay(String day){ this.day = day; }

    public String getNoti(){ return noti; }
    public void setNoti(String noti){ this.noti = noti; }

    public String getDatelimit(){ return datelimit; }
    public void setDatelimit(String datelimit){ this.datelimit = datelimit; }


    public String getLocalname() {
        return localname;
    }
    public void setLocalname(String localname) {
        this.localname = localname;
    }


    public String getLocalurl() {
        return localurl;
    }
    public void setLocalurl(String localurl) {
        this.localurl = localurl;
    }

    public String getTextname() {
        return textname;
    }
    public void setTextname(String textname) {
        this.textname = textname;
    }


    public String getExtratext() {
        return extratext;
    }
    public void setExtratext(String extratext) {
        this.extratext = extratext;
    }

    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userid);
        dest.writeString(this.localname);
        dest.writeString(this.localurl);
        dest.writeString(this.imageurl);
        dest.writeString(this.textname);
        dest.writeString(this.extratext);
        dest.writeString(this.state);
        dest.writeString(this.key);
    }

    protected ServiceItemInfo(Parcel in) {
        this.userid = in.readString();
        this.localname= in.readString();
        this.localurl = in.readString();
        this.imageurl = in.readString();
        this.textname =in.readString();
        this.extratext = in.readString();
        this.state = in.readString();
        this.key = in.readString();
    }

    public static final Parcelable.Creator<ServiceItemInfo> CREATOR = new Parcelable.Creator<ServiceItemInfo>() {
        @Override
        public ServiceItemInfo createFromParcel(Parcel source) {
            return new ServiceItemInfo(source);
        }

        @Override
        public ServiceItemInfo[] newArray(int size) {
            return new ServiceItemInfo[size];
        }
    };
}
