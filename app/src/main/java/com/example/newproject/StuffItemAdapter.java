package com.example.newproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StuffItemAdapter extends RecyclerView.Adapter<StuffItemAdapter.StuffItemViewHolder> implements OnStuffItemClickListener {
    private ArrayList<StuffItemInfo> arrayList;
    private Context context;
    private OnStuffItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(StuffItemViewHolder stuffitemViewHolder, View v, int position);
    }
    public void setOnItemClickListener(OnStuffItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onItemClick(StuffItemAdapter.StuffItemViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public StuffItemAdapter(ArrayList<StuffItemInfo> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public StuffItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_stuffitem, parent, false);
        StuffItemViewHolder holder = new StuffItemViewHolder(view);
        return holder;
    }
   
    @Override
    public void onBindViewHolder(@NonNull final StuffItemViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getLocalurl())
                .into(holder.et_localurl);
        holder.et_localname.setText(arrayList.get(position).getLocalname());
        holder.et_textname.setText(arrayList.get(position).getTextname());
    }

    public int getItemCount() {
        return arrayList.size();
    }

    public class StuffItemViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView et_localurl;
        TextView et_localname;
        TextView et_textname;

        public StuffItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_localurl = itemView.findViewById(R.id.et_localurl);
            this.et_localname = itemView.findViewById(R.id.et_localname);
            this.et_textname = itemView.findViewById(R.id.et_textname);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int  pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(StuffItemViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
