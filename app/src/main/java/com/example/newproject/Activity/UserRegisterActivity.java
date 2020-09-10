package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newproject.R;

public class UserRegisterActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        //오버라이드 해온 oncreate메소드를 super를 통해 초기화 또는 재정의 해줌
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