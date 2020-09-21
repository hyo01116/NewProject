package com.example.newproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.Interface.OnStuffItemClickListener;
import com.example.newproject.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StuffItemAdapter extends RecyclerView.Adapter<StuffItemAdapter.StuffItemViewHolder> implements OnStuffItemClickListener {
    //어댑터는 뷰와 데이터를 연결해주는 역할
    private ArrayList<StuffItemInfo> mDataset;
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
    public StuffItemAdapter(ArrayList<StuffItemInfo> mDataset, Context context){
        //context : 어플리케이션이나 객체의 현재 상태를 나타내는 것 (activity는 context를 상속받음)
        this.mDataset = mDataset;
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
        StuffItemInfo stuffItemInfo =  mDataset.get(position);
        Glide.with(holder.itemView)
                .load(mDataset.get(position).getLocalurl())
                .into(holder.et_localurl);
        holder.et_localname.setText(stuffItemInfo.getLocalname());
        holder.et_textname.setText(stuffItemInfo.getTextname());
        holder.et_datelimit.setText(stuffItemInfo.getDatelimit());
        holder.et_day.setText(stuffItemInfo.getDay());
    }

    public int getItemCount() {
        return mDataset.size();
    }

    public class StuffItemViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView et_localurl;
        TextView et_localname;
        TextView et_textname;
        TextView et_datelimit;
        TextView et_day;

        public StuffItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_localurl = itemView.findViewById(R.id.et_localurl);
            this.et_localname = itemView.findViewById(R.id.et_localname);
            this.et_textname = itemView.findViewById(R.id.et_textname);
            this.et_datelimit = itemView.findViewById(R.id.et_datelimit);
            this.et_day = itemView.findViewById(R.id.et_day);

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
