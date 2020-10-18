package com.example.newproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newproject.Class.ServiceItemInfo;
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.Interface.OnServiceNotiItemClickListener;
import com.example.newproject.Interface.OnStuffNotiItemClickListener;
import com.example.newproject.R;

import java.util.ArrayList;

public class ServiceNotiItemAdapter extends RecyclerView.Adapter<ServiceNotiItemAdapter.ServiceNotiItemViewHolder> implements OnServiceNotiItemClickListener {
    private ArrayList<ServiceItemInfo> mDataset;
    private Context context;
    private OnServiceNotiItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(ServiceNotiItemAdapter.ServiceNotiItemViewHolder serviceNotiItemViewHolder, View v, int position);
    }
    public void setOnItemClickListener(OnServiceNotiItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onItemClick(ServiceNotiItemAdapter.ServiceNotiItemViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public ServiceNotiItemAdapter(ArrayList<ServiceItemInfo> mDataset, Context context){
        //context : 어플리케이션이나 객체의 현재 상태를 나타내는 것 (activity는 context를 상속받음)
        this.mDataset = mDataset;
        this.context = context;
    }
    @NonNull
    @Override
    public ServiceNotiItemAdapter.ServiceNotiItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        ServiceNotiItemAdapter.ServiceNotiItemViewHolder holder = new ServiceNotiItemAdapter.ServiceNotiItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceNotiItemAdapter.ServiceNotiItemViewHolder holder, int position) {
        ServiceItemInfo serviceItemInfo =  mDataset.get(position);
        Glide.with(holder.itemView)
                .load(mDataset.get(position).getLocalurl())
                .into(holder.imageView2);
        holder.user_name.setText(serviceItemInfo.getLocalname());
        holder.et_textname.setText(serviceItemInfo.getTextname());
        holder.et_day.setText(serviceItemInfo.getDatelimit());
    }

    public int getItemCount() {
        return mDataset.size();
    }

    public class ServiceNotiItemViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView imageView2;
        TextView user_name;
        TextView et_textname;
        TextView et_day;

        public ServiceNotiItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView2 = itemView.findViewById(R.id.imageView2);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.et_textname = itemView.findViewById(R.id.et_textname);
            this.et_day= itemView.findViewById(R.id.et_day);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int  pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ServiceNotiItemAdapter.ServiceNotiItemViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
