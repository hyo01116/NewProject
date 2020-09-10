package com.example.newproject.Interface;

import android.view.View;

import com.example.newproject.Adapter.ServiceItemListAdapter;

public interface OnServiceItemListClickListener {
    public void onItemClick(ServiceItemListAdapter.ServiceItemListViewHolder holder, View view, int position);
}
