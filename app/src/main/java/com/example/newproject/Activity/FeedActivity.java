package com.example.newproject.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.Adapter.FeedAdapter;
import com.example.newproject.Adapter.FeedStuffAdapter;
import com.example.newproject.Adapter.ServiceItemAdapter;
import com.example.newproject.Adapter.StuffItemAdapter;
import com.example.newproject.AddFeedActivity;
import com.example.newproject.Class.FeedInfo;
import com.example.newproject.Class.LocalUserInfo;
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.Interface.OnStuffItemClickListener;
import com.example.newproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class FeedActivity extends Fragment implements FeedAdapter.OnListItemSelectedInterface {

    //개인별 봉사 추천기능 (분류해서 저장한 뒤 추천)
    private RecyclerView recyclerView_horizontal;
    private RecyclerView.LayoutManager layoutManager_horizontal;
    private RecyclerView.Adapter adapter_horizontal;

    private RecyclerView recyclerView_vertical;
    private RecyclerView.LayoutManager layoutManager_vertical;
    private FeedStuffAdapter adapter_vertical;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    Toolbar toolbar;
    TextView toolbar_title;

    private FloatingActionButton btn_add;

    private String first, second, third;
    private ArrayList<FeedInfo> arrayList = new ArrayList<FeedInfo>();
    private ArrayList<StuffItemInfo> arrayList_vertical = new ArrayList<StuffItemInfo>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        //미리 정의해둔 xml을 view로 변환하는 것
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_feed, container, false);

        DividerItemDecoration v_dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL);
        v_dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_line));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_line));


        toolbar = (Toolbar) view.findViewById(R.id.top_toolbar);
        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);

        recyclerView_vertical = view.findViewById(R.id.recyclerView_vertical);
        recyclerView_vertical.setHasFixedSize(true);
        layoutManager_vertical = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView_vertical.setLayoutManager(layoutManager_vertical);

        recyclerView_horizontal = view.findViewById(R.id.recyclerView_horizontal);
        recyclerView_horizontal.setHasFixedSize(true);
        layoutManager_horizontal = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView_horizontal.setLayoutManager(layoutManager_horizontal);


        btn_add = (FloatingActionButton) view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddFeedActivity.class);
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        finduserinfo();
        return view;
    }

    public void startActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }

    public void finduserlevel() {
    }

    public void finduserinfo() {
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
                        toolbar_title.setText(third);
                        findfeed(first, second, third);
                        findhome(first, second, third);
                    }
                }
            }
        });
    }

    public void findhome(String first, String second, String third) {
        final String[] key = {null};
        final String[] path = new String[1];
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("stuff").child(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //key[0] = String.valueOf(dataSnapshot.getKey());      //문서의 key
                    StuffItemInfo stuffItemInfo = dataSnapshot.getValue(StuffItemInfo.class);
                    arrayList_vertical.add(0, stuffItemInfo);
                }
                adapter_vertical = new FeedStuffAdapter(arrayList_vertical, getContext());
                recyclerView_vertical.setAdapter(adapter_vertical);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void findfeed(String first, String second, String third) {
        final String[] key = {null};
        final String[] path = new String[1];
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        databaseReference = database.getReference(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //key[0] = String.valueOf(dataSnapshot.getKey());      //문서의 key
                    FeedInfo feedInfo = dataSnapshot.getValue(FeedInfo.class);
                    arrayList.add(0, feedInfo);
                }
                adapter_horizontal = new FeedAdapter(arrayList, getContext(), FeedActivity.this);
                recyclerView_horizontal.setAdapter(adapter_horizontal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}