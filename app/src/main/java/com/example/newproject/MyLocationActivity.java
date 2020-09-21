package com.example.newproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

public class MyLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationSource mLocationSource;
    private MapView mapView;
    private NaverMap mNavermap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        mLocationSource = new FusedLocationSource(this, 0);
        findViewById(R.id.btn_save_mylocation).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_mylocation:
                    //save_mylocation();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mNavermap = naverMap;
        mNavermap.setLocationSource(mLocationSource);
        mNavermap.setLocationTrackingMode(LocationTrackingMode.Follow);

        naverMap.addOnLocationChangeListener(location ->
                Toast.makeText(this,
                        location.getLatitude() + ", " + location.getLongitude(),
                        Toast.LENGTH_SHORT).show());
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
    public void startActivity(MyLocationActivity myLocationActivity, Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
