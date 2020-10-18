package com.example.newproject.Interface;

import android.view.View;

import com.example.newproject.Adapter.FeedStuffAdapter;
import com.example.newproject.Adapter.StuffNotiItemAdapter;

public interface OnFeedStuffClickListener {
    public void onItemClick(FeedStuffAdapter.FeedStuffViewHolder holder, View view, int position);
}
