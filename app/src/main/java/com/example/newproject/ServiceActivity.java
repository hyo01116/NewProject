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

public class ServiceActivity extends Fragment implements ServiceItemAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ServiceItemAdapter adapter;
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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_line));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(dividerItemDecoration);

        findLocationinfo();

        return view;
    }
    public void findLocationinfo() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        final DocumentReference documentReference;
        documentReference = db.collection("Users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        UserLocationInfo userLocationInfo = documentSnapshot.toObject(UserLocationInfo.class);
                        first = userLocationInfo.getFirst();
                        second = userLocationInfo.getSecond();
                        third = userLocationInfo.getThird();
                        findservice();
                    }
                }
            }
        });
    }
    public void findservice(){
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("service").child(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ServiceItemInfo serviceItemInfo = dataSnapshot.getValue(ServiceItemInfo.class);
                    if(serviceItemInfo.getState().equals("open")){
                        arrayList.add(serviceItemInfo);
                        arrayList_key.add(dataSnapshot.getKey());    //클릭시 해당 키로 이동
                    }
                }
                adapter = new ServiceItemAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnServiceItemClickListener() {
                    @Override
                    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder holder, View view, int position) {
                        ServiceItemAdapter.ServiceItemViewHolder viewHolder = (ServiceItemAdapter.ServiceItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), ServiceItemDetailActivity.class);
                        intent.putExtra("itemkey", arrayList_key.get(position));
                        intent.putExtra("userid", arrayList.get(position).getUserid());
                        intent.putExtra("first", first);
                        intent.putExtra("second",second);
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

    @Override
    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder itemViewHolder, View v, int position) {
    }
}
