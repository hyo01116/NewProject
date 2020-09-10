package com.example.newproject.Interface;

import android.view.View;

import com.example.newproject.Adapter.StuffItemListAdapter;

public interface OnStuffItemListClickListener {
    public void onItemClick(StuffItemListAdapter.StuffItemListViewHolder holder, View view, int position);
}
