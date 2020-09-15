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
import com.example.newproject.Class.ServiceItemInfo;
import com.example.newproject.Interface.OnServiceItemClickListener;
import com.example.newproject.R;

import java.util.ArrayList;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ServiceItemViewHolder> implements OnServiceItemClickListener {
    //봉사활동에 대한 리사이클러뷰를 어떻게 구성 할 것인가
    //oncreateviewholder, onbindviewholder, getitemcount
    //arraylist에 데이터를 담아옴
    //담아온 아이템을 뷰에 어떻게 나타낼 것인가, 뷰 구성 (oncreateviewholder)
    //position별로 어떤 데이터를 넣을 것인가 (onbindviewholder)
    //몇개의 아이템이 있는가 (getitemcount)
    //클릭시 이동하는 것을 원한다면 (인터페이스 만들어서 클릭시 실행되는 메소드 구현, 매개변수는 holder, view, position 넘겨줌
    //
    private ArrayList<ServiceItemInfo> arrayList;
    private Context context;
    private OnServiceItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ServiceItemAdapter.ServiceItemViewHolder serviceitemViewHolder, View v, int position);
    }

    public void setOnItemClickListener(OnServiceItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder holder, View view, int position) {
        //onserviceitemclicklistener를 상속 받았기 때문에 함수 재정의 해줘야함 (무조건)
        //interface는 추상 메소드의 모음이기때문에 해당 interface를 상속받는다면 클래스 내에서 재정의 필요
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public ServiceItemAdapter(ArrayList<ServiceItemInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceItemAdapter.ServiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //아이템 뷰를 위한 뷰홀더 생성 (하나의 아이템을 어떻게 표현 할 것인가에 대한 뷰)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_serviceitem, parent, false);
        ServiceItemAdapter.ServiceItemViewHolder holder = new ServiceItemAdapter.ServiceItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceItemAdapter.ServiceItemViewHolder holder, int position) {
        //position에 해당하는 데이터를 뷰홀더의 아이템으로 나타내는 것
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getLocalurl())
                .into(holder.et_localurl);
        holder.et_localname.setText(arrayList.get(position).getLocalname());
        holder.et_textname.setText(arrayList.get(position).getTextname());
        holder.et_datelimit.setText(arrayList.get(position).getDatelimit());
        holder.et_day.setText(arrayList.get(position).getDay());
    }

    public int getItemCount() {
        return arrayList.size();
    }

    public class ServiceItemViewHolder extends RecyclerView.ViewHolder {    //cloud firestore
        ImageView et_localurl;
        TextView et_localname;
        TextView et_textname;
        TextView et_datelimit;
        TextView et_day;

        public ServiceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.et_localurl = itemView.findViewById(R.id.et_localurl);
            this.et_localname = itemView.findViewById(R.id.et_localname);
            this.et_textname = itemView.findViewById(R.id.et_textname);
            this.et_datelimit = itemView.findViewById(R.id.et_datelimit);
            this.et_day = itemView.findViewById(R.id.et_day);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ServiceItemAdapter.ServiceItemViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
