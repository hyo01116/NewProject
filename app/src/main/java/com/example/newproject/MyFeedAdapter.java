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
import com.example.newproject.Class.FeedInfo;

import java.util.ArrayList;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder> implements OnMyFeedItemListClickListener{
    private ArrayList<FeedInfo> arrayList;
    private Context context;
    private OnMyFeedItemListClickListener listener;

    @Override
    public void onItemClick(MyFeedViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(MyFeedAdapter.MyFeedViewHolder myFeedViewHolder, View v, int position);
    }
    public void setOnItemClickListener(OnMyFeedItemListClickListener listener) {
        this.listener = listener;
    }
    public MyFeedAdapter(ArrayList<FeedInfo> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyFeedAdapter.MyFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        MyFeedAdapter.MyFeedViewHolder holder = new MyFeedAdapter.MyFeedViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyFeedAdapter.MyFeedViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getLocalurl())
                .into(holder.localurl);
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPicture())
                .into(holder.picture);
        holder.localname.setText(arrayList.get(position).getLocalname());
        holder.extratext.setText(arrayList.get(position).getExtratext());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }    //Feed Adapter : (홈에서)피드보여줌, MyFeed adapter: (마이페이지)피드 보여줌 + 수정, 삭제 가능

    public class MyFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView localurl;
        ImageView picture;
        TextView localname;
        TextView extratext;

        public MyFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            this.localurl = itemView.findViewById(R.id.localurl);
            this.localname = itemView.findViewById(R.id.localname);
            this.picture = itemView.findViewById(R.id.picture);
            this.extratext = itemView.findViewById(R.id.extratext);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int  pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(MyFeedAdapter.MyFeedViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
