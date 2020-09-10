package com.example.newproject.Interface;

import android.view.View;

import com.example.newproject.Adapter.MyFeedAdapter;

public interface OnMyFeedItemListClickListener {
    //interface는 추상메소드 모음(그래서 상속받는 클래스에서 구체화 하도록)
    //다중 상속 가능
    public void onItemClick(MyFeedAdapter.MyFeedViewHolder holder, View view, int position);
}
