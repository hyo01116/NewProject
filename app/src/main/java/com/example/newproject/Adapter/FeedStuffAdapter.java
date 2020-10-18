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
import com.example.newproject.Interface.OnFeedStuffClickListener;
import com.example.newproject.Interface.OnStuffItemClickListener;
import com.example.newproject.R;

import java.util.ArrayList;

public class FeedStuffAdapter extends RecyclerView.Adapter<FeedStuffAdapter.FeedStuffViewHolder> implements OnFeedStuffClickListener{
    private ArrayList<StuffItemInfo> mDataset;
    private Context context;
    private OnFeedStuffClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(FeedStuffAdapter.FeedStuffViewHolder holder,  View v, int position);
    }
    public void setOnItemClickListener(OnFeedStuffClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onItemClick(FeedStuffAdapter.FeedStuffViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public FeedStuffAdapter(ArrayList<StuffItemInfo> mDataset, Context context){
        //context : 어플리케이션이나 객체의 현재 상태를 나타내는 것 (activity는 context를 상속받음)
        this.mDataset = mDataset;
        this.context = context;
    }
    @NonNull
    @Override
    public FeedStuffAdapter.FeedStuffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_feedstuff, parent, false);
        FeedStuffAdapter.FeedStuffViewHolder holder = new FeedStuffAdapter.FeedStuffViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedStuffAdapter.FeedStuffViewHolder holder, int position) {
        StuffItemInfo stuffItemInfo =  mDataset.get(position);
        Glide.with(holder.itemView)
                .load(mDataset.get(position).getLocalurl())    //복지시설 이미지
                .into(holder.et_localurl);
        holder.et_localname.setText(stuffItemInfo.getLocalname());    //복지시설 이름
        holder.et_textname.setText(stuffItemInfo.getTextname());     // 활동명
        holder.et_datelimit.setText(stuffItemInfo.getDatelimit());   //모집기한
    }

    public int getItemCount() {
        return mDataset.size();
    }

    public class FeedStuffViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView et_localurl;
        TextView et_localname;
        TextView et_textname;
        TextView et_datelimit;

        public FeedStuffViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_localurl = itemView.findViewById(R.id.imageView);
            this.et_localname = itemView.findViewById(R.id.localname);
            this.et_textname = itemView.findViewById(R.id.textname);
            this.et_datelimit = itemView.findViewById(R.id.datelimit);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int  pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(FeedStuffAdapter.FeedStuffViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
