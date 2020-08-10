package com.example.newproject;

import com.google.firebase.firestore.GeoPoint;

public class LocalUserInfo {
    private String email;
    private String name;
    private String phone;
    private String imageurl;
    private String first;
    private String second;
    private String third;
    private GeoPoint geoPoint;

    public LocalUserInfo(){

    }
    public LocalUserInfo(String email, String name, String phone, String imageurl, GeoPoint geoPoint, String first, String second, String third){
        this.email =email;
        this.name = name;
        this.phone = phone;
        this.imageurl = imageurl;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public String getEmail() { return email; }
    public void setEmail(String email){ this.email = email;}

    public String getName(){ return name;}
    public void setName(String name){this.name = name;}

    public String getPhone(){ return phone;}
    public void setPhone(String phone){ this.phone = phone;}

    public String getImageurl(){ return imageurl;}
    public void setImageurl(String imageurl){ this.imageurl = imageurl;}

    public GeoPoint getGeoPoint(){ return geoPoint; }
    public void setGeoPoint(GeoPoint geoPoint){ this.geoPoint = geoPoint; }

    public String getFirst() { return first; }
    public void setFirst(String first){ this.first = first;}

    public String getSecond(){ return second; }
    public void setSecond(String second){ this.second= second;}

    public String getThird(){ return third;}
    public void setThird(String third){ this.third = third;}
}
