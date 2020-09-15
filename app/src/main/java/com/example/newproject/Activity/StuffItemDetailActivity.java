package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.newproject.ChatActivity;
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.GeneralPageActivity;
import com.example.newproject.MyChatListActivity;
import com.example.newproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class StuffItemDetailActivity extends AppCompatActivity {
    public FirebaseUser user;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public String first, second, third, userid;

    private BottomNavigationView generalbottom;

    ImageView imageurl, localurl;

    TextView textname, localname, address, stuff, datelimit, extratext;
    StuffItemInfo stuffItemInfo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuffdetail);
        stuffItemInfo = (StuffItemInfo)getIntent().getSerializableExtra("Serialize");

        imageurl = (ImageView)findViewById(R.id.imageurl);
        localname = (TextView)findViewById(R.id.localname);
        datelimit = (TextView)findViewById(R.id.datelimit);
        textname = (TextView)findViewById(R.id.textname);
        address = (TextView)findViewById(R.id.address);
        localurl = (ImageView)findViewById(R.id.localurl);
        extratext = (TextView)findViewById(R.id.extratext);

        generalbottom = findViewById(R.id.navigation_view);
        generalbottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.btn_like:
                        break;
                    case R.id.btn_phone:
                        break;
                    case R.id.btn_chat:
                        startchat();
                        break;
                }
                return true;
            }
        });
        findstuff();
    }

    /*View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btn_chat:
                    startchat();
                    break;
            }
        }
    };*/
    public void startchat(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = stuffItemInfo.getUserid();
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_userid", userid);
        startActivity(intent);
    }
    public void findstuff(){
        //serialize 이용해서 객체를 전달받음 (세부사항 표시)
        //intent를 사용해서 데이터를 넘길 필요가 없음
        if(stuffItemInfo.getImageurl() != null){
            Glide.with(StuffItemDetailActivity.this).load(stuffItemInfo.getImageurl()).into(imageurl);
        }
        else {
            Glide.with(StuffItemDetailActivity.this).load(stuffItemInfo.getLocalurl()).into(imageurl);
        }
        if(stuffItemInfo.getLocalurl() != null){
            Glide.with(StuffItemDetailActivity.this).load(stuffItemInfo.getLocalurl()).into(localurl);
        }

        localname.setText(stuffItemInfo.getLocalname());
        datelimit.setText(stuffItemInfo.getDatelimit());
        address.setText(stuffItemInfo.getAddress());
        textname.setText(stuffItemInfo.getTextname());
        extratext.setText(stuffItemInfo.getExtratext());
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
