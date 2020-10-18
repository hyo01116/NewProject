package com.example.newproject;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.Activity.BaseActivity;
import com.example.newproject.Class.ChatInfo;
import com.example.newproject.Class.GeneralUserInfo;
import com.example.newproject.Class.LocalUserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends BaseActivity {
    private FirebaseUser user;
    private DatabaseReference chat_databaseReference;
    private DatabaseReference chat_secondary_databseReference;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ChatInfo> chatInfoArrayList;

    private EditText et_textsend;
    private ImageView btn_send;

    private String chat_userid, my_userid, user_level, day;

    MyApplication myApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();
        chat_userid = getIntent().getStringExtra("chat_userid");    //상대방 id
        my_userid = user.getUid();

        myApplication = (MyApplication) getApplicationContext();
        user_level = myApplication.getUser_level();

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        chat_databaseReference = FirebaseDatabase.getInstance("https://newproject-ab6cb-chat.firebaseio.com/").getReference(my_userid).child(chat_userid);
        chat_secondary_databseReference = FirebaseDatabase.getInstance("https://newproject-ab6cb-chat.firebaseio.com/").getReference(chat_userid).child(my_userid);

        btn_send = findViewById(R.id.btn_send);
        et_textsend = findViewById(R.id.et_textsend);

        chatInfoArrayList= new ArrayList<>();
        adapter = new ChatAdapter(chatInfoArrayList, ChatActivity.this, my_userid);
        recyclerView.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_textsend.getText().toString();
                if (msg != null) {
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
                    SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
                    SimpleDateFormat date = new SimpleDateFormat("dd", Locale.getDefault());
                    day = year.format(currentTime) + "/"+ month.format(currentTime) +"/"+date.format(currentTime);
                    if(user_level.equals("1")) {      //일반사용자라면 general로 firestore받아오기
                        final DocumentReference documentReference = db.collection("Users").document(user.getUid());    //현재 로그인한 사람의 주소
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        GeneralUserInfo generalUserInfo = documentSnapshot.toObject(GeneralUserInfo.class);
                                        ChatInfo chat = new ChatInfo();
                                        chat.setData(msg);
                                        chat.setUid(my_userid);
                                        chat.setDate(day);
                                        chat.setProfile(generalUserInfo.getImageurl());
                                        et_textsend.setText("");
                                        chat_databaseReference.push().setValue(chat);
                                        chat_secondary_databseReference.push().setValue(chat);
                                    }
                                }
                            }
                        });
                    }
                    else if(user_level.equals("2")){     //지역사용자라면 local로 받아오기
                        final DocumentReference documentReference = db.collection("Users").document(user.getUid());    //현재 로그인한 사람의 주소
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        LocalUserInfo localUserInfo = documentSnapshot.toObject(LocalUserInfo.class);
                                        ChatInfo chat = new ChatInfo();
                                        chat.setData(msg);
                                        chat.setUid(my_userid);
                                        chat.setDate(day);
                                        chat.setProfile(localUserInfo.getImageurl());
                                        et_textsend.setText("");
                                        chat_databaseReference.push().setValue(chat);
                                        chat_secondary_databseReference.push().setValue(chat);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        chat_databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               ChatInfo chat = snapshot.getValue(ChatInfo.class);
               ((ChatAdapter)adapter).addChat(chat);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
