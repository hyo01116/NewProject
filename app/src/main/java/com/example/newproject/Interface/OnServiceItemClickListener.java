package com.example.newproject.Interface;

import android.view.View;

import com.example.newproject.Adapter.ServiceItemAdapter;

public interface OnServiceItemClickListener {
    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder holder, View view, int position);
}
