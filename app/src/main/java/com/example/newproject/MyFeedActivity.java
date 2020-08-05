package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class MyFeedActivity extends AppCompatActivity implements FeedAdapter.OnListItemSelectedInterface{    //마이페이지에서 피드관리 + 피드 작성
    //피드 작성시 write -> userid -> feed에 작성한 피드 저장
    //write에서 글의 key 받아서 feeddb에서 first,second, third로 들어가서 내용 찾기 (수정으로 바꾸기) + 글 쓰는 버튼 추가
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user;
    private FirebaseDatabase database, second_database;
    private DatabaseReference databaseReference, second_databaseReference;

    private ArrayList<String> arrayList_key;
    private ArrayList<String> arrayList_secondkey;
    private ArrayList<FeedInfo> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfeed);


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
    public void findMyFeed(final String first, final String second, final String third){
        final String[] title_key = new String[1];
        final String[] key = {null};
        user = FirebaseAuth.getInstance().getCurrentUser();
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        databaseReference = database.getReference().child(user.getUid()).child("feed");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {     //write에서 피드 키 찾음
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key[0] = String.valueOf(dataSnapshot.getValue());
                    /*arrayList_key.add(key[0]);    //base에서의 key
                    arrayList_secondkey.add(dataSnapshot.getKey());   //write에서의 key*/
                    second_databaseReference = second_database.getReference().child(first).child(second).child(third).child(key[0]);
                    second_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FeedInfo feedInfo = snapshot.getValue(FeedInfo.class);
                            arrayList.add(feedInfo);
                            adapter = new FeedAdapter(arrayList, getApplicationContext(), MyFeedActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
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
    public void onItemSelected(View v, int position) {

    }
}
