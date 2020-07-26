package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ServiceItemListActivity extends Fragment implements ServiceItemListAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private ServiceItemListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<ServiceItemInfo> arrayList = new ArrayList<ServiceItemInfo>();
    ArrayList<String> arrayList_key = new ArrayList<String>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String first, second, third, userlevel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_service, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        findMyList();

        return view;
    }
    public void findMyList(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        databaseReference = database.getReference().child(user.getUid()).child("service");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ServiceItemInfo serviceItemInfo = dataSnapshot.getValue(ServiceItemInfo.class);
                    arrayList.add(serviceItemInfo);
                    arrayList_key.add(dataSnapshot.getKey());    //클릭시 해당 키로 이동
                }
                adapter = new ServiceItemListAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnServiceItemListClickListener() {
                    @Override
                    public void onItemClick(ServiceItemListAdapter.ServiceItemListViewHolder holder, View view, int position) {
                        ServiceItemListAdapter.ServiceItemListViewHolder viewHolder = (ServiceItemListAdapter.ServiceItemListViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), ServiceItemListDetailActivity.class);
                        intent.putExtra("itemkey", arrayList_key.get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(ServiceItemListAdapter.ServiceItemListViewHolder serviceitemListViewHolder, View v, int position) {

    }
}
