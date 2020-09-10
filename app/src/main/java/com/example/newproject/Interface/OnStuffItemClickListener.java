package com.example.newproject.Interface;

import android.view.View;

import com.example.newproject.Adapter.StuffItemAdapter;

public interface OnStuffItemClickListener {
    public void onItemClick(StuffItemAdapter.StuffItemViewHolder holder, View view, int position);
}
