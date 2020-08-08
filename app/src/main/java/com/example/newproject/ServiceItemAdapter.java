package com.example.newproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ServiceItemViewHolder> implements OnServiceItemClickListener {
    private ArrayList<ServiceItemInfo> arrayList;
    private Context context;
    private OnServiceItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ServiceItemAdapter.ServiceItemViewHolder serviceitemViewHolder, View v, int position);
    }

    public void setOnItemClickListener(OnServiceItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public ServiceItemAdapter(ArrayList<ServiceItemInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceItemAdapter.ServiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_serviceitem, parent, false);
        ServiceItemAdapter.ServiceItemViewHolder holder = new ServiceItemAdapter.ServiceItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceItemAdapter.ServiceItemViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getLocalurl())
                .into(holder.et_localurl);
        holder.et_localname.setText(arrayList.get(position).getLocalname());
        holder.et_textname.setText(arrayList.get(position).getTextname());
    }

    public int getItemCount() {
        return arrayList.size();
    }

    public class ServiceItemViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView et_localurl;
        TextView et_localname;
        TextView et_textname;

        public ServiceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_localurl = itemView.findViewById(R.id.et_localurl);
            this.et_localname = itemView.findViewById(R.id.et_localname);
            this.et_textname = itemView.findViewById(R.id.et_textname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ServiceItemAdapter.ServiceItemViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
