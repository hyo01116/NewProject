package com.example.newproject;

public class ChatProfile {
    String profile;
    String name;

    public ChatProfile(String profile, String name) {
        this.name =name;
        this.profile = profile;
    }

    public ChatProfile() {

    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
