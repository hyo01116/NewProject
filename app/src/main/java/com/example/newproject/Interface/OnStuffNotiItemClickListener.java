package com.example.newproject.Interface;

import android.view.View;
import com.example.newproject.Adapter.StuffNotiItemAdapter;

public interface OnStuffNotiItemClickListener {
    public void onItemClick(StuffNotiItemAdapter.StuffNotiItemViewHolder holder, View view, int position);
}
