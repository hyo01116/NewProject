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

import java.util.ArrayList;

public class StuffItemListAdapter extends RecyclerView.Adapter<StuffItemListAdapter.StuffItemListViewHolder> implements OnStuffItemListClickListener  {
    private ArrayList<StuffItemInfo> arrayList;
    private Context context;
    private OnStuffItemListClickListener listener;

    @Override
    public void onItemClick(StuffItemListViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(StuffItemListAdapter.StuffItemListViewHolder stuffitemListViewHolder, View v, int position);
    }
    public void setOnItemClickListener(OnStuffItemListClickListener listener) {
        this.listener = listener;
    }
    public StuffItemListAdapter(ArrayList<StuffItemInfo> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public StuffItemListAdapter.StuffItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_stuffitem, parent, false);
        StuffItemListAdapter.StuffItemListViewHolder holder = new StuffItemListAdapter.StuffItemListViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull final StuffItemListAdapter.StuffItemListViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImageurl())
                .into(holder.et_localurl);
        holder.et_localname.setText(arrayList.get(position).getLocalname());
        holder.et_stuff.setText(arrayList.get(position).getStuff());
    }
    public int getItemCount() {
        return arrayList.size();
    }

    public class StuffItemListViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView et_localurl;
        TextView et_localname;
        TextView et_stuff;

        public StuffItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_localurl = itemView.findViewById(R.id.et_localurl);
            this.et_localname = itemView.findViewById(R.id.et_localname);
            this.et_stuff = itemView.findViewById(R.id.et_stuff);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int  pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(StuffItemListAdapter.StuffItemListViewHolder.this, v, pos);
                    }
                }
            });
        }
    }

}
