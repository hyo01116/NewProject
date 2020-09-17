package com.example.newproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newproject.Class.ChatProfile;

import java.io.InputStream;
import java.util.ArrayList;

public class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.MyChatViewHolder> {
    private ArrayList<ChatProfile> mDataset;
    private Context context;

    public interface OnListItemSelectedInterface{
        void onItemSelected(View v, int position);
    }
    private MyChatAdapter.OnListItemSelectedInterface mListener;
    @NonNull
    @Override
    public MyChatAdapter.MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mychat, parent, false);
        MyChatAdapter.MyChatViewHolder holder = new MyChatAdapter.MyChatViewHolder(v);
        return holder;
    }

    public MyChatAdapter(ArrayList<ChatProfile> mychat, Context context, OnListItemSelectedInterface listener){
        mDataset = mychat;
        this.mListener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull MyChatAdapter.MyChatViewHolder holder, int position) {
        ChatProfile mychat = mDataset.get(position);
        Glide.with(holder.itemView)
                .load(mDataset.get(position).getProfile())
                .into(holder.et_chatprofile);
        holder.et_chatid.setText(mychat.getName());
        //holder.et_textname.setText(mychat.getLasttext());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder {
        ImageView et_chatprofile;
        TextView et_chatid;
        //TextView et_textname;

        public MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            //채팅자의 프로필 사진
            this.et_chatprofile = itemView.findViewById(R.id.et_chatprofile);
            this.et_chatid = itemView.findViewById(R.id.et_chatid);
            //this.et_textname = itemView.findViewById(R.id.et_textname);
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
