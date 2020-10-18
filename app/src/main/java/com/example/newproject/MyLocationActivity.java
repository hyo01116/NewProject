package com.example.newproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newproject.Activity.BaseActivity;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.List;

public class MyLocationActivity extends BaseActivity implements OnMapReadyCallback {

    private FusedLocationSource mLocationSource;
    private MapView mapView;
    private NaverMap mNavermap;
    private Geocoder geocoder;
    List<Address> list = null;
    Double latitude;
    Double longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);

        geocoder = new Geocoder(this);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mLocationSource = new FusedLocationSource(this, 0);
        mapView.getMapAsync(this);

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
        //mNavermap.setLocationTrackingMode(LocationTrackingMode.None)

        Marker marker = new Marker();
        naverMap.addOnLocationChangeListener(location ->
        {marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                marker.setMap(naverMap);
            naverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(location.getLatitude(), location.getLongitude())));
        });
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                System.out.println(latLng.latitude + " " + latLng.longitude);
                marker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
                marker.setMap(naverMap);
                naverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(latLng.latitude, latLng.longitude)));
                latitude = latLng.latitude;
                longitude  = latLng.longitude;
                try {
                    list = geocoder.getFromLocation(latitude, longitude, 10);
                    if(list != null){
                        if(list.size() == 0){
                            System.out.println("해당되는 주소가 없습니다.");
                        }
                        else{
                            System.out.println(list.get(0).toString());
                        }
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if(permsRequestCode == 0){
            if(grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED){
                mNavermap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
            }
        }
    }
    public void startActivity(MyLocationActivity myLocationActivity, Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
