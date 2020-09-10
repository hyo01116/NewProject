package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.newproject.ChatActivity;
import com.example.newproject.Class.ServiceItemInfo;
import com.example.newproject.R;
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

    ImageView imageurl, localurl;
    TextView textname, localname, extratext, toolbar_title;
    Toolbar toolbar;

    ServiceItemInfo serviceItemInfo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState){
        //봉사활동에 대한 세부정보를 나타낼 시에는 itemkey값을 인텐트를 통해 넘겨준다
        //해당 itemkey값을 가지고 database에 접근함
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicedetail);

        //intent로 화면 전환 시 액티비티끼리의 정보 전달
        /*itemkey = getIntent().getStringExtra("itemkey");
        userid = getIntent().getStringExtra("userid");   //글 올린사람의 id
        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");*/

        //parcel로 객체 전달했기때문에 intent.putextra할 필요가 없음
        Intent intent = getIntent();
        serviceItemInfo = intent.getParcelableExtra("parcel");

        toolbar = (Toolbar)findViewById(R.id.top_toolbar);
        imageurl = (ImageView)findViewById(R.id.imageurl);
        localurl = (ImageView)findViewById(R.id.localurl);
        localname = (TextView)findViewById(R.id.localname);
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
        //채팅 버튼 누르면 현재 userid를 채팅 액티비티로 넘기고 실행
        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_userid", userid);
        startActivity(intent);
    }
    public void findservice(){
        if(serviceItemInfo.getImageurl() != null) {
            Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getImageurl()).into(imageurl);
        }
        else{
            Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getLocalurl()).into(imageurl);
        }
        if(serviceItemInfo.getLocalurl() != null) {
            Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getLocalurl()).into(localurl);
        }
        toolbar.setTitle(serviceItemInfo.getTextname());
        //textname.setText(serviceItemInfo.getTextname());
        localname.setText(serviceItemInfo.getLocalname());
        extratext.setText(serviceItemInfo.getExtratext());
    }

    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
