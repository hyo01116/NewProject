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

import com.bumptech.glide.Glide;
import com.example.newproject.Class.ChatInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.TEXT_ALIGNMENT_TEXT_END;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private ArrayList<ChatInfo> mDataset;
    public String userid;
    private Context context;
    private int cnt = 0;

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messagebox, parent, false);
        ChatViewHolder holder = new ChatViewHolder(v);
        return holder;
    }
    public ChatAdapter(ArrayList<ChatInfo> chatInfo, Context context, String userid){
        mDataset = chatInfo;
        this.userid = userid;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatInfo chat = mDataset.get(position);
        holder.et_data.setText(chat.getData());
        if(chat.getUid().equals(userid)){
            System.out.println("same");
            holder.et_data.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else{
            System.out.println("not same");
            System.out.println(cnt);
            if(cnt == 0 || !(chat.getDate().equals(mDataset.get(position - 1).getDate()))){
                System.out.println("first");
                Glide.with(holder.itemView)
                        .load(mDataset.get(position).getProfile())
                        .into(holder.profile);
                holder.et_data.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                cnt++;
            }
            else{
                cnt++;
                System.out.println("second");
                if(chat.getDate().equals(mDataset.get(position - 1).getDate())){
                    holder.et_data.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView et_data;
        ImageView profile;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_data = itemView.findViewById(R.id.et_data);
            this.profile = itemView.findViewById(R.id.profile);
        }
    }
    public void addChat(ChatInfo chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size() -1);
    }
}
