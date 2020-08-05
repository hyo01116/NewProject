package com.example.newproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference chat_databaseReference;
    private DatabaseReference chat_secondary_databseReference;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ChatInfo> chatInfoArrayList;

    private EditText et_textsend;
    private Button btn_send;

    private String chat_userid, my_userid;

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

        chat_databaseReference = FirebaseDatabase.getInstance("https://newproject-ab6cb-chat.firebaseio.com/").getReference(my_userid).child(chat_userid);
        chat_secondary_databseReference = FirebaseDatabase.getInstance("https://newproject-ab6cb-chat.firebaseio.com/").getReference(chat_userid).child(my_userid);

        btn_send = findViewById(R.id.btn_send);
        et_textsend = findViewById(R.id.et_textsend);

        chatInfoArrayList= new ArrayList<>();
        adapter = new ChatAdapter(chatInfoArrayList, ChatActivity.this);
        recyclerView.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_textsend.getText().toString();
                if (msg != null) {
                    ChatInfo chat = new ChatInfo();
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
                    String formatDate = sdfNow.format(date);
                    chat.setTime(formatDate);
                    //chat.setProfile();
                    chat.setData(msg);
                    chat.setUid(user.getUid());
                    chat_databaseReference.push().setValue(chat);
                    chat_secondary_databseReference.push().setValue(chat);
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
