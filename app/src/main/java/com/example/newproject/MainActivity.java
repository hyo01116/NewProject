package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_login).setOnClickListener(onClickListener);
        findViewById(R.id.btn_register).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_login:
                    startActivity(LoginActivity.class);
                    break;
                case R.id.btn_register:
                    startActivity(UserRegisterActivity.class);
                    break;
            }
        }
    };
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}