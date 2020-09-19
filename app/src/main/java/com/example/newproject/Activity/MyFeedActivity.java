package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.AddFeedActivity;
import com.example.newproject.Class.FeedInfo;
import com.example.newproject.Class.LocalUserInfo;
import com.example.newproject.Interface.OnMyFeedItemListClickListener;
import com.example.newproject.Adapter.MyFeedAdapter;
import com.example.newproject.MyFeedDetailActivity;
import com.example.newproject.R;
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

public class MyFeedActivity extends AppCompatActivity implements MyFeedAdapter.OnItemClickListener {    //마이페이지에서 피드관리 + 피드 작성
    //피드 작성시 write -> userid -> feed에 작성한 피드 저장
    //write에서 글의 key 받아서 feeddb에서 first,second, third로 들어가서 내용 찾기 (수정으로 바꾸기) + 글 쓰는 버튼 추가
    private RecyclerView recyclerView;
    private MyFeedAdapter adapter;       //recyclerview.adapter(x) -> myfeedadapter (setonitemclicklistener 가능)
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user;
    private FirebaseDatabase database, second_database;
    private DatabaseReference databaseReference, second_databaseReference;

    private ArrayList<String> arrayList_key = new ArrayList<String>();     //new arraylist 안해주면 nullexception 생김
    private ArrayList<String> arrayList_secondkey = new ArrayList<String>();
    private ArrayList<FeedInfo> arrayList = new ArrayList<FeedInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfeed);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MyFeedActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        //버튼클릭시 이동

        finduserinfo();
    }

    public void finduserinfo() {
        final String[] first = new String[1];
        final String[] second = new String[1];
        final String[] third = new String[1];
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
                        first[0] = localUserInfo.getFirst();
                        second[0] = localUserInfo.getSecond();
                        third[0] = localUserInfo.getThird();
                        findMyFeed(first[0], second[0], third[0]);
                    }
                }
            }
        });
    }
    public void findMyFeed(final String first, final String second, final String third){    //클릭시 상세페이지로 이동하여 수정하기
        //현재는 내가 작성한 피드를 보여줌
        user = FirebaseAuth.getInstance().getCurrentUser();
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        databaseReference = database.getReference().child(user.getUid()).child("feed");
        //기관 사용자가 데이터를 쓰거나(봉사, 기부글 또는 피드글) 두개의 데이터베이스에 접근해야함
        //피드 db 또는 write db + 사용자 별 피드, 글을 저장하는 데이터베이스
        //데이터의 중복을 줄이기 위해 사용자 별 디비에는 피드, write에 글을 저장시키고 생성된 키 값을 저장시킴
        //글이나 피드 쓸때 일단 피드, write 디비에 저장시키고 생성된 키값을 사용자 별 글을 저장하는 디비에 저장
        //사용자 별 디비를 만든 이유는 나중에 수정을 원할 때 사용자 마이페이지에서 빠르게 사용자가 쓴 글을 보여주기 위해서
        // 아니라면 모든 데이터에 접근해서 userid가 현재 사용자의 아이디와 일치하는지 체크해야함
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {     //write에서 피드 키 찾음
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayList_key.add(String.valueOf(dataSnapshot.getValue()));    //feed에서의 key
                    arrayList_secondkey.add(String.valueOf(dataSnapshot.getKey()));   //write에서의 key
                }
                find_second(first, second, third, arrayList_key, arrayList_secondkey);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void find_second(final String first, final String second, final String third, ArrayList<String> first_array, ArrayList<String> second_array){
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        System.out.println(first_array.size());
        System.out.println(second_array.size());
        second_databaseReference = second_database.getReference().child(first).child(second).child(third);
        second_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (first_array.contains(dataSnapshot.getKey())) {
                        System.out.println(dataSnapshot.getKey());
                        FeedInfo feedInfo = dataSnapshot.getValue(FeedInfo.class);
                        arrayList.add(feedInfo);
                        System.out.println(feedInfo.getExtratext());
                    }
                }
                adapter = new MyFeedAdapter(arrayList, MyFeedActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnMyFeedItemListClickListener() {
                    @Override
                    public void onItemClick(MyFeedAdapter.MyFeedViewHolder holder, View view, int position) {
                        MyFeedAdapter.MyFeedViewHolder viewHolder = (MyFeedAdapter.MyFeedViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        //first, second, third, key[0]을 넘김 (수정, 삭제 페이지로 이동)
                        Intent intent = new Intent(getApplicationContext(), MyFeedDetailActivity.class);
                        intent.putExtra("first", first);
                        intent.putExtra("second", second);
                        intent.putExtra("third", third);
                        intent.putExtra("key", arrayList_key.get(position));
                        intent.putExtra("secondkey", arrayList_secondkey.get(position));
                        startActivity(intent);
                    }
                });
            }    //클릭시 상세 페이지 이동 후 수정, 삭제 가능하게 함

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onItemClick(MyFeedAdapter.MyFeedViewHolder myFeedViewHolder, View v, int position) {

    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
