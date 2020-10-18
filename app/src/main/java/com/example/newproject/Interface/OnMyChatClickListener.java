package com.example.newproject.Interface;

import android.view.View;

import com.example.newproject.MyChatAdapter;

public interface OnMyChatClickListener {

    public void onItemClick(MyChatAdapter.MyChatViewHolder holder, View view, int position);
}
