package com.example.newproject.Activity;

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

import com.example.newproject.Interface.OnServiceItemClickListener;
import com.example.newproject.R;
import com.example.newproject.Adapter.ServiceItemAdapter;
import com.example.newproject.Class.ServiceItemInfo;
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

public class ServiceActivity extends Fragment implements ServiceItemAdapter.OnItemClickListener {
    //객체를 넘기고 싶은데, 클릭 시 페이지 이동하는 인터페이스를 상속받게 구현했다
    //페이지 이동하는 인터페이스 상속받았기 떄문에 클릭시 메소드 구현 (추상메소드 반드시 구현)
    //그래서 다중상속을 사용할 수 없어서 intent로 아이템 키 값을 넘기고 다시 데이터베이스에 접근해서 아이템을 찾아오는 방식으로 세부사항 구현
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
                        //userlocationinfo 객체를 생성해서 받아온 documentsnapshot에서 객체의 형태로 저장
                        //그다음 get메소드로 사용자의 위치 정보 받아옴
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
                //특정 시점에 동작하게 하는 함수
                //비동기 해결하기 위해 구현하는데 그 이유는 다른 일 하다가 데이터가 준비되면 콜백함수내의 작업들을 실행하라고
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ServiceItemInfo serviceItemInfo = dataSnapshot.getValue(ServiceItemInfo.class);
                    if(serviceItemInfo.getState().equals("open")){
                        arrayList.add(serviceItemInfo);
                    }
                }
                adapter = new ServiceItemAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnServiceItemClickListener() {
                    //인터페이스 사용 -> onitemclick을 무조건 재정의, 여기서는 클릭시 화면 이동 + 인텐트로 현재 정보를 넘기는것
                    //인텐트는 화면간 정보전달
                    @Override
                    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder holder, View view, int position) {
                        ServiceItemAdapter.ServiceItemViewHolder viewHolder = (ServiceItemAdapter.ServiceItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), ServiceItemDetailActivity.class);
                        intent.putExtra("parcel", arrayList.get(position));
                        startActivity(intent);
                        /*intent.putExtra("itemkey", arrayList_key.get(position));
                        intent.putExtra("userid", arrayList.get(position).getUserid());
                        intent.putExtra("first", first);
                        intent.putExtra("second",second);
                        intent.putExtra("third", third);*/
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
