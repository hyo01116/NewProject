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

import com.example.newproject.Class.ChatInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private ArrayList<ChatInfo> mDataset;

    private Context context;
    public String mynickname;
    private String nickname;
    private FirebaseUser user;

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messagebox, parent, false);
        ChatViewHolder holder = new ChatViewHolder(v);
        return holder;
    }
    public ChatAdapter(ArrayList<ChatInfo> chatInfo, Context context, String mynickname){
        mDataset = chatInfo;
        this.mynickname = mynickname;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatInfo chat = mDataset.get(position);
        /*Glide.with(holder.itemView)
                .load(mDataset.get(position).getProfile())
                .into(holder.et_profile);*/
        holder.et_nickname.setText(chat.getNickname());
        holder.et_data.setText(chat.getData());

        if(chat.getUid() == null){
            System.out.println("null");
        }
        if(chat.getUid().equals(this.mynickname)){
            holder.et_data.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else{
            holder.et_data.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView et_data;
        public TextView et_nickname;
        public View rootview;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_nickname= itemView.findViewById(R.id.et_nickname);
            this.et_data= itemView.findViewById(R.id.et_data);
            rootview = itemView;
        }
    }
    public void addChat(ChatInfo chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size() -1);
    }

}
