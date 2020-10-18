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
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class StuffItemDetailActivity extends BaseActivity implements OnMapReadyCallback {
    public FirebaseUser user;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public String first, second, third, userid, name;

    private BottomNavigationView generalbottom;
    private FusedLocationSource mLocationSource;


    ImageView imageurl, localurl;

    TextView textname, localname, address, stuff, datelimit, extratext;
    private MapView map;
    private NaverMap mNavermap;
    Geocoder geocoder;
    StuffItemInfo stuffItemInfo;
    String addr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuffdetail);
        stuffItemInfo = (StuffItemInfo) getIntent().getSerializableExtra("Serialize");
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
        findstuff();
    }
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
        addr = stuffItemInfo.getAddress();
        //등록된 주소로 map의 위치 찾아서 나타내기
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
