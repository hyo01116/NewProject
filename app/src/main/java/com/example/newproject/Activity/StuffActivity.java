package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.Class.GeneralUserInfo;
import com.example.newproject.Class.StuffItemInfo;     //stuffiteminfo 클래스를 import 해와서 사용
import com.example.newproject.Interface.OnStuffItemClickListener;
import com.example.newproject.R;
import com.example.newproject.Adapter.StuffItemAdapter;
import com.example.newproject.Class.UserLocationInfo;
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

public class StuffActivity extends Fragment implements StuffItemAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_noti;
    private StuffItemAdapter adapter_noti;
    private StuffItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<StuffItemInfo> arrayList = new ArrayList<StuffItemInfo>();
    ArrayList<StuffItemInfo> arrayList_noti = new ArrayList<StuffItemInfo>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String first, second, third, phone, address;

    TextView et_phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_stuff, container, false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recycler_line));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView_noti = (RecyclerView) view.findViewById(R.id.recyclerView_noti);
        recyclerView_noti.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView_noti.setLayoutManager(layoutManager);
        recyclerView_noti.addItemDecoration(dividerItemDecoration);

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
                        GeneralUserInfo generalUserInfo = documentSnapshot.toObject(GeneralUserInfo.class);
                        first = generalUserInfo.getFirst();
                        second = generalUserInfo.getSecond();
                        third = generalUserInfo.getThird();
                        findstuff(first, second, third);
                    }
                }
            }
        });
    }
    public void findstuff(final String first, final String second, final String third){
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("stuff").child(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            //리스너를 사용해서 해당시점의 datasnapshot를 받아옴
            // ondatachange 콜백함수를 사용해서 이벤트가 발생하면 다른 메소드를 호출해서 알려줌 -> 데이터 존재하면 화면에 나타냄
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    StuffItemInfo stuffItemInfo = dataSnapshot.getValue(StuffItemInfo.class);
                    //getvalue를 통해 객체에 받아온 정보를 넣고 open 이라면 arraylist에 대입
                    if(stuffItemInfo.getState().equals("open")){
                        if(stuffItemInfo.getNoti().equals("1")){
                            arrayList_noti.add(stuffItemInfo);
                        }
                        else{
                            arrayList.add(stuffItemInfo);
                        }
                    }
                }
                adapter_noti = new StuffItemAdapter(arrayList_noti, getContext());
                recyclerView_noti.setAdapter(adapter_noti);
                adapter_noti.setOnItemClickListener(new OnStuffItemClickListener() {
                    @Override
                    public void onItemClick(StuffItemAdapter.StuffItemViewHolder holder, View view, int position) {
                        StuffItemAdapter.StuffItemViewHolder viewHolder = (StuffItemAdapter.StuffItemViewHolder)recyclerView_noti.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), StuffItemDetailActivity.class);
                        intent.putExtra("Serialize", arrayList_noti.get(position));
                        startActivity(intent);
                    }
                });

                adapter = new StuffItemAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnStuffItemClickListener() {
                    @Override
                    public void onItemClick(StuffItemAdapter.StuffItemViewHolder holder, View view, int position) {
                        StuffItemAdapter.StuffItemViewHolder viewHolder = (StuffItemAdapter.StuffItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), StuffItemDetailActivity.class);
                        //(class에 serializable을 상속시켜놓음, stuffiteminfo를 직렬화하여 intent로 다음 액티비티에 넘김)
                        //원래는 intent로 넘겼지만, 객체 통째로 넘김
                        intent.putExtra("Serialize", arrayList.get(position));
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //사용자가 데이터를 읽을 권한이 없는 경우
            }
        });
    }
    @Override
    public void onItemClick(StuffItemAdapter.StuffItemViewHolder itemViewHolder, View v, int position) {
    }
}
