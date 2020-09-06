package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.Class.FeedInfo;
import com.example.newproject.Class.LocalUserInfo;
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

public class MyFeedActivity extends AppCompatActivity implements MyFeedAdapter.OnItemClickListener{    //마이페이지에서 피드관리 + 피드 작성
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
        findViewById(R.id.btn_add).setOnClickListener(onClickListener);

        finduserinfo();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_add:
                    startActivity(AddFeedActivity.class);
                    break;
            }
        }
    };

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
        final String[] key = {null};
        final String[] secondkey = {null};
        user = FirebaseAuth.getInstance().getCurrentUser();
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        databaseReference = database.getReference().child(user.getUid()).child("feed");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {     //write에서 피드 키 찾음
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key[0] = String.valueOf(dataSnapshot.getValue());
                    secondkey[0] = String.valueOf(dataSnapshot.getKey());
                    System.out.println("key: "+key[0]);
                    /*arrayList_key.add(key[0]);    //base에서의 key
                    arrayList_secondkey.add(dataSnapshot.getKey());   //write에서의 key*/
                    second_databaseReference = second_database.getReference(first).child(second).child(third).child(key[0]);
                    second_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FeedInfo feedInfo = snapshot.getValue(FeedInfo.class);
                            arrayList.add(feedInfo);
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
                                    intent.putExtra("third",third);
                                    intent.putExtra("key", key[0]);
                                    intent.putExtra("secondkey", secondkey[0]);
                                    startActivity(intent);
                                }
                            });
                        }    //클릭시 상세 페이지 이동 후 수정, 삭제 가능하게 함
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

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
