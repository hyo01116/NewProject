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
import com.example.newproject.Class.FeedInfo;
import com.example.newproject.R;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private ArrayList<FeedInfo> mDataset;
    private Context context;

    public interface OnListItemSelectedInterface{
        void onItemSelected(View v, int position);
    }
    private FeedAdapter.OnListItemSelectedInterface mListener;

    @NonNull
    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        FeedAdapter.FeedViewHolder holder = new FeedAdapter.FeedViewHolder(v);
        return holder;
    }
    public FeedAdapter(ArrayList<FeedInfo> feedinfo, Context context, FeedAdapter.OnListItemSelectedInterface listener){
        mDataset = feedinfo;
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.FeedViewHolder holder, int position) {
        FeedInfo feedInfo = mDataset.get(position);
        Glide.with(holder.itemView)
                .load(mDataset.get(position).getLocalurl())
                .into(holder.localurl);
        Glide.with(holder.itemView.getContext())
                .load(mDataset.get(position).getPicture())
                .into(holder.picture);
        holder.localname.setText(feedInfo.getLocalname());
        holder.extratext.setText(feedInfo.getExtratext());
        if (!feedInfo.getAddress().equals("1")) {
            holder.address.setText(feedInfo.getAddress());     //일반사용자(level = 1)은 주소와 전화번호 표시안함
            holder.phone.setText(feedInfo.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView localurl;
        ImageView picture;
        TextView localname;
        TextView extratext;
        TextView address;
        TextView phone;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            this.localurl = itemView.findViewById(R.id.localurl);
            this.picture = itemView.findViewById(R.id.picture);
            this.localname = itemView.findViewById(R.id.localname);
            this.extratext = itemView.findViewById(R.id.extratext);
            this.address = itemView.findViewById(R.id.address);
            this.phone = itemView.findViewById(R.id.phone);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());    //클릭시에 해당 채팅으로 이동
                }
            });
        }
    }
}
