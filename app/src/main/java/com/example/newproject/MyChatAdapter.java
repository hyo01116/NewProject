package com.example.newproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.MyChatViewHolder> {
    private ArrayList<ChatProfile> mDataset;
    private Context context;

    public interface OnListItemSelectedInterface{
        //한 아이템 클릭시 가게와 메뉴에 대한 상세한 정보를 나타내는 페이지로 이동
        void onItemSelected(View v, int position);
    }
    private MyChatAdapter.OnListItemSelectedInterface mListener;
    @NonNull
    @Override
    public MyChatAdapter.MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mychat, parent, false);
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
        //chat profile 세팅
        holder.et_chatid.setText(mychat.getName());
        //holder.et_textname.setText(mychat);
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
