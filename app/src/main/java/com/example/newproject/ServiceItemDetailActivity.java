package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceItemDetailActivity extends AppCompatActivity {
    public FirebaseUser user;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public String first, second, third, userid, itemkey;

    ImageView imageurl;
    TextView textname, localname, address, service, datelimit, extratext;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicedetail);

        itemkey = getIntent().getStringExtra("itemkey");
        userid = getIntent().getStringExtra("userid");   //글 올린사람의 id
        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");

        imageurl = (ImageView)findViewById(R.id.imageurl);
        textname = (TextView)findViewById(R.id.textname);
        localname = (TextView)findViewById(R.id.localname);
        address = (TextView)findViewById(R.id.address);
        service = (TextView)findViewById(R.id.service);
        datelimit = (TextView)findViewById(R.id.datelimit);
        extratext = (TextView)findViewById(R.id.extratext);
        findViewById(R.id.btn_chat).setOnClickListener(onClickListener);
        findservice();
    }
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btn_chat:
                    startchat();
                    break;
            }
        }
    };
    public void startchat(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_userid", userid);
        startActivity(intent);
    }
    public void findservice(){
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference= database.getReference("service").child(first).child(second).child(third).child(itemkey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemInfo serviceItemInfo = snapshot.getValue(ServiceItemInfo.class);
                if(serviceItemInfo.getImageurl() != null) {
                    Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getImageurl()).into(imageurl);
                }
                else{
                    Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getLocalurl()).into(imageurl);
                }
                textname.setText(serviceItemInfo.getTextname());
                localname.setText(serviceItemInfo.getLocalname());
                address.setText(serviceItemInfo.getAddress());
                datelimit.setText(serviceItemInfo.getDatelimit());
                service.setText(serviceItemInfo.getService());
                extratext.setText(serviceItemInfo.getExtratext());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}