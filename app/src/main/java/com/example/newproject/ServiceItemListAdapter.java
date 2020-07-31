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

public class ServiceItemListAdapter extends RecyclerView.Adapter<ServiceItemListAdapter.ServiceItemListViewHolder> implements OnServiceItemListClickListener  {
    private ArrayList<ServiceItemInfo> arrayList;
    private Context context;
    private OnServiceItemListClickListener listener;

    @Override
    public void onItemClick(ServiceItemListViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(ServiceItemListAdapter.ServiceItemListViewHolder serviceitemListViewHolder, View v, int position);
    }
    public void setOnItemClickListener(OnServiceItemListClickListener listener) {
        this.listener = listener;
    }
    public ServiceItemListAdapter(ArrayList<ServiceItemInfo> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public ServiceItemListAdapter.ServiceItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_serviceitem, parent, false);
        ServiceItemListAdapter.ServiceItemListViewHolder holder = new ServiceItemListAdapter.ServiceItemListViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ServiceItemListAdapter.ServiceItemListViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImageurl())
                .into(holder.et_localurl);
        holder.et_localname.setText(arrayList.get(position).getLocalname());
        holder.et_textname.setText(arrayList.get(position).getTextname());
    }
    public int getItemCount() {
        return arrayList.size();
    }

    public class ServiceItemListViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView et_localurl;
        TextView et_localname;
        TextView et_textname;

        public ServiceItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_localurl = itemView.findViewById(R.id.et_localurl);
            this.et_localname = itemView.findViewById(R.id.et_localname);
            this.et_textname = itemView.findViewById(R.id.et_textname);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int  pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ServiceItemListAdapter.ServiceItemListViewHolder.this, v, pos);
                    }
                }
            });
        }
    }

}
