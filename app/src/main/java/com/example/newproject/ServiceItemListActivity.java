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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ServiceItemListActivity extends Fragment implements ServiceItemListAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private ServiceItemListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<ServiceItemInfo> arrayList = new ArrayList<ServiceItemInfo>();
    ArrayList<String> arrayList_key = new ArrayList<String>();
    ArrayList<String> arrayList_secondkey = new ArrayList<String>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;
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

        finduserinfo();

        return view;
    }
    public void finduserinfo(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(user.getUid());    //현재 로그인한 사람의 주소
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        LocalUserInfo localUserInfo = documentSnapshot.toObject(LocalUserInfo.class);
                        first = localUserInfo.getFirst();
                        second = localUserInfo.getSecond();
                        third = localUserInfo.getThird();
                        findMyList(first, second, third);
                    }
                }
            }
        });
    }
    public void findMyList(final String first, final String second, final String third){
        final String[] key = {null};
        user = FirebaseAuth.getInstance().getCurrentUser();
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference().child(user.getUid()).child("service");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key[0] = String.valueOf(dataSnapshot.getValue());
                    arrayList_key.add(key[0]);    //base에서의 key
                    arrayList_secondkey.add(dataSnapshot.getKey());   //write에서의 key
                    second_databaseReference = second_database.getReference("service").child(first).child(second).child(third).child(key[0]);
                    second_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ServiceItemInfo serviceItemInfo = snapshot.getValue(ServiceItemInfo.class);
                            arrayList.add(serviceItemInfo);
                            adapter = new ServiceItemListAdapter(arrayList, getContext());
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new OnServiceItemListClickListener() {
                                @Override
                                public void onItemClick(ServiceItemListAdapter.ServiceItemListViewHolder holder, View view, int position) {
                                    ServiceItemListAdapter.ServiceItemListViewHolder viewHolder = (ServiceItemListAdapter.ServiceItemListViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                                    Intent intent = new Intent(getContext(), ServiceItemListDetailActivity.class);
                                    intent.putExtra("itemkey", arrayList_key.get(position));
                                    intent.putExtra("secondkey", arrayList_secondkey.get(position));
                                    intent.putExtra("first", first);
                                    intent.putExtra("second", second);
                                    intent.putExtra("third", third);
                                    startActivity(intent);
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                /*adapter = new ServiceItemListAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnServiceItemListClickListener() {
                    @Override
                    public void onItemClick(ServiceItemListAdapter.ServiceItemListViewHolder holder, View view, int position) {
                        ServiceItemListAdapter.ServiceItemListViewHolder viewHolder = (ServiceItemListAdapter.ServiceItemListViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), ServiceItemListDetailActivity.class);
                        intent.putExtra("itemkey", arrayList_key.get(position));
                        intent.putExtra("secondkey", arrayList_secondkey.get(position));
                        intent.putExtra("first", first);
                        intent.putExtra("second", second);
                        intent.putExtra("third", third);
                        startActivity(intent);
                    }
                });*/
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
