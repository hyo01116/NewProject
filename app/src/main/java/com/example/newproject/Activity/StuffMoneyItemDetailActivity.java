package com.example.newproject.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.newproject.ChatActivity;
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.List;

public class StuffMoneyItemDetailActivity extends BaseActivity {
    public FirebaseUser user;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public String first, second, third, userid, name;

    private BottomNavigationView generalbottom;
    ImageView imageurl, localurl;

    TextView textname, localname, address, stuff, datelimit, extratext;
    private MapView map;
    Geocoder geocoder;
    StuffItemInfo stuffItemInfo;
    String addr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuffmoneyitem);
        stuffItemInfo = (StuffItemInfo) getIntent().getSerializableExtra("Serialize");
        geocoder = new Geocoder(this);

        imageurl = (ImageView) findViewById(R.id.imageurl);
        localname = (TextView) findViewById(R.id.localname);
        address = (TextView) findViewById(R.id.address);
        localurl = (ImageView) findViewById(R.id.localurl);
        extratext = (TextView) findViewById(R.id.extratext);
        textname = (TextView) findViewById(R.id.textname);

        generalbottom = findViewById(R.id.navigation_view);
        generalbottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.btn_phone:
                        break;
                    case R.id.btn_money:
                        //startActivity("기부페이지");
                        break;
                }
                return true;
            }
        });
        findstuff();
    }
    public void findstuff(){
        //serialize 이용해서 객체를 전달받음 (세부사항 표시)
        //intent를 사용해서 데이터를 넘길 필요가 없음
        if(stuffItemInfo.getImageurl() != null){
            Glide.with(StuffMoneyItemDetailActivity.this).load(stuffItemInfo.getImageurl()).into(imageurl);
        }
        else {
            Glide.with(StuffMoneyItemDetailActivity.this).load(stuffItemInfo.getLocalurl()).into(imageurl);
        }
        if(stuffItemInfo.getLocalurl() != null){
            Glide.with(StuffMoneyItemDetailActivity.this).load(stuffItemInfo.getLocalurl()).into(localurl);
        }
        localname.setText(stuffItemInfo.getLocalname());
        address.setText(stuffItemInfo.getAddress());
        extratext.setText(stuffItemInfo.getExtratext());
        //textname.setText(stuffItemInfo.getTextname());
    }

    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
