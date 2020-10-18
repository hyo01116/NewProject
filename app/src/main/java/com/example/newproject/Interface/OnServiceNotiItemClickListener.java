package com.example.newproject.Interface;

import android.view.View;
import com.example.newproject.Adapter.ServiceNotiItemAdapter;

public interface OnServiceNotiItemClickListener {
    public void onItemClick(ServiceNotiItemAdapter.ServiceNotiItemViewHolder holder, View view, int position);
}
