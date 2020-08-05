package com.example.newproject;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private ArrayList<ChatInfo> mDataset;
    private Context context;
    private String nickname;
    private FirebaseUser user;

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messagebox, parent, false);
        ChatViewHolder holder = new ChatViewHolder(v);
        return holder;
    }
    public ChatAdapter(ArrayList<ChatInfo> chatInfo, Context context){
        mDataset = chatInfo;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatInfo chat = mDataset.get(position);
        /*Glide.with(holder.itemView)
                .load(mDataset.get(position).getProfile())
                .into(holder.et_profile);*/
        holder.et_time.setText(chat.getTime());
        holder.et_data.setText(chat.getData());
        user = FirebaseAuth.getInstance().getCurrentUser();
        nickname = user.getUid();
        if(chat.getUid().equals(nickname)){
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
        TextView et_data;
        TextView et_time;
        ImageView et_profile;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_time= itemView.findViewById(R.id.et_time);
            this.et_data= itemView.findViewById(R.id.et_data);
        }
    }
    public void addChat(ChatInfo chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size() -1);
    }

}
