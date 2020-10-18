package com.example.newproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.newproject.Activity.BaseActivity;
import com.example.newproject.Activity.LoginActivity;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends BaseActivity {
    private static final int PERMISSION_MULTI_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        if (Build.VERSION.SDK_INT < 23) {
            goLoginActivity();
        } else {
            if(checkAndRequestPermissions()){
                goLoginActivity();
            }
        }
    }
    private void goLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkAndRequestPermissions(){
        String [] permissions = new String[] {
                    Manifest.permission.MANAGE_DOCUMENTS,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(permission);
                System.out.println(permission);
            }
        }
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_MULTI_CODE);
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length == 0){
            return;
        }
        switch(requestCode) {
            case PERMISSION_MULTI_CODE:
                checkPermissionResult(permissions, grantResults);
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void checkPermissionResult(String[] permissions, int[] grantResults){
        boolean isAllGranted = true;
        for(int i = 0; i <permissions.length; i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                isAllGranted = false;
            }
        }
        isAllGranted = true;
        if(isAllGranted){
            goLoginActivity();
        }
        else{
        }
    }

    private void showPermissionDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.request_permission);
        dialog.setMessage(R.string.request_permission);
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                PermissionActivity.this.finish();
                goAppSettingActivity();
            }
        });
        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionActivity.this.finish();
            }
        });
        dialog.show();
    }
    private void goAppSettingActivity(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
