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
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.Interface.OnStuffNotiItemClickListener;
import com.example.newproject.R;

import java.util.ArrayList;

public class StuffNotiItemAdapter extends RecyclerView.Adapter<StuffNotiItemAdapter.StuffNotiItemViewHolder> implements OnStuffNotiItemClickListener{
    private ArrayList<StuffItemInfo> mDataset;
    private Context context;
    private OnStuffNotiItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(StuffNotiItemAdapter.StuffNotiItemViewHolder stuffNotiItemViewHolder, View v, int position);
    }
    public void setOnItemClickListener(OnStuffNotiItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onItemClick(StuffNotiItemAdapter.StuffNotiItemViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public StuffNotiItemAdapter(ArrayList<StuffItemInfo> mDataset, Context context){
        //context : 어플리케이션이나 객체의 현재 상태를 나타내는 것 (activity는 context를 상속받음)
        this.mDataset = mDataset;
        this.context = context;
    }
    @NonNull
    @Override
    public StuffNotiItemAdapter.StuffNotiItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        StuffNotiItemAdapter.StuffNotiItemViewHolder holder = new StuffNotiItemAdapter.StuffNotiItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StuffNotiItemAdapter.StuffNotiItemViewHolder holder, int position) {
        StuffItemInfo stuffItemInfo =  mDataset.get(position);
        Glide.with(holder.itemView)
                .load(mDataset.get(position).getLocalurl())
                .into(holder.imageView2);
        holder.user_name.setText(stuffItemInfo.getLocalname());
        holder.et_textname.setText(stuffItemInfo.getTextname());
        holder.et_day.setText(stuffItemInfo.getDatelimit());
    }

    public int getItemCount() {
        return mDataset.size();
    }

    public class StuffNotiItemViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView imageView2;
        TextView user_name;
        TextView et_textname;
        TextView et_day;

        public StuffNotiItemViewHolder(@NonNull View itemView) {
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
                        listener.onItemClick(StuffNotiItemAdapter.StuffNotiItemViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
