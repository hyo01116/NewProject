package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRegisterActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregister);

        findViewById(R.id.btn_generaluser).setOnClickListener(onClickListener);
        findViewById(R.id.btn_localuser).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_generaluser:
                    startActivity(GeneralUserRegisterActivity.class);
                    break;
                case R.id.btn_localuser:
                    startActivity(LocalUserRegisterActivity.class);
                    break;
            }
        }
    };
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}