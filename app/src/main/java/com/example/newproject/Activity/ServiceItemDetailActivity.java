package com.example.newproject.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import net.daum.android.map.MapController;

import java.io.IOException;
import java.util.List;

public class ServiceItemDetailActivity extends BaseActivity implements OnMapReadyCallback {
    public FirebaseUser user;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public String first, second, third, userid, name;

    private BottomNavigationView generalbottom;
    private FusedLocationSource mLocationSource;

    TextView textname, localname, address, stuff, datelimit, extratext;
    private MapView map;
    private NaverMap mNavermap;
    Geocoder geocoder;
    String addr;

    ImageView imageurl, localurl;
    Toolbar toolbar;

    ServiceItemInfo serviceItemInfo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState){
        //봉사활동에 대한 세부정보를 나타낼 시에는 itemkey값을 인텐트를 통해 넘겨준다
        //해당 itemkey값을 가지고 database에 접근함
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicedetail);

        Intent intent = getIntent();
        serviceItemInfo = intent.getParcelableExtra("parcel");

        geocoder = new Geocoder(this);

        imageurl = (ImageView) findViewById(R.id.imageurl);
        localname = (TextView) findViewById(R.id.localname);
        datelimit = (TextView) findViewById(R.id.datelimit);
        textname = (TextView) findViewById(R.id.textname);
        address = (TextView) findViewById(R.id.address);
        localurl = (ImageView) findViewById(R.id.localurl);
        extratext = (TextView) findViewById(R.id.extratext);

        map = (MapView) findViewById(R.id.loc_map);
        map.onCreate(savedInstanceState);
        mLocationSource = new FusedLocationSource(this, 0);
        map.getMapAsync(this);

        generalbottom = findViewById(R.id.navigation_view);
        generalbottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
        findservice();
    }
    public void startchat(){
        //채팅 버튼 누르면 현재 userid를 채팅 액티비티로 넘기고 실행
        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_userid", userid);
        startActivity(intent);
    }
    public void findservice(){
        if(serviceItemInfo.getImageurl() != null){
            Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getImageurl()).into(imageurl);
        }
        else {
            Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getLocalurl()).into(imageurl);
        }
        if(serviceItemInfo.getLocalurl() != null){
            Glide.with(ServiceItemDetailActivity.this).load(serviceItemInfo.getLocalurl()).into(localurl);
        }

        localname.setText(serviceItemInfo.getLocalname());
        datelimit.setText(serviceItemInfo.getDatelimit());
        address.setText(serviceItemInfo.getAddress());
        textname.setText(serviceItemInfo.getTextname());
        extratext.setText(serviceItemInfo.getExtratext());
        addr = serviceItemInfo.getAddress();
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        List<Address> list = null;
        try{
            list = geocoder.getFromLocationName(addr, 10);
        }
        catch (IOException e){
            e.printStackTrace();
            Log.e("test", "오류 발생");
        }
        if(list != null){
            if(list.size() != 0){
                Address new_addr = list.get(0);
                double lat = new_addr.getLatitude();
                double lon = new_addr.getLongitude();
                System.out.println(lat + " "+ lon);
                Marker marker = new Marker();
                marker.setPosition(new LatLng(lat, lon));
                marker.setMap(naverMap);
                marker.setIconPerspectiveEnabled(true);   //원근감 표시
                naverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(lat, lon)));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if(permsRequestCode == 0){
            if(grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED){
                mNavermap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
