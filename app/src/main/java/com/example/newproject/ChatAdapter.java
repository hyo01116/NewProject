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

import static android.view.View.TEXT_ALIGNMENT_TEXT_END;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private ArrayList<ChatInfo> mDataset;
    public String userid;
    private Context context;

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messagebox, parent, false);
        ChatViewHolder holder = new ChatViewHolder(v);
        return holder;
    }
    public ChatAdapter(ArrayList<ChatInfo> chatInfo, Context context, String userid){
        mDataset = chatInfo;
        this.context = context;
        this.userid = userid;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatInfo chat = mDataset.get(position);
        if(chat.getUid().equals(userid)){
            System.out.println("same");
            holder.et_data.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
            holder.et_data.setText(chat.getData());
        }
        else{
            holder.et_data.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.et_data.setText(chat.getData());
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView et_data;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_data = itemView.findViewById(R.id.et_data);
        }
    }
    public void addChat(ChatInfo chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size() -1);
    }
}
