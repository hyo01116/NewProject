package com.example.newproject.Class;

public class ChatProfile {
    //현재 나와 채팅을 진행하고 있는 사람의 리스트를 나타낼때, 사진과 이름 보여줌
    String profile;
    String name;
    String lasttext;

    //생성자로써 초기화 시켜줌
    public ChatProfile(String profile, String name, String lasttext) {
        this.name = name;
        this.profile = profile;
        this.lasttext = lasttext;
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
    public String getLasttext(){ return lasttext; }
    public void setLasttext(String lasttext){ this.lasttext = lasttext;}
}
