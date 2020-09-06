package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.newproject.GeneralPageActivity;
import com.example.newproject.HomeActivity;
import com.example.newproject.MyChatListActivity;
import com.example.newproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GeneralUserActivity extends AppCompatActivity {    //일반사용자 사용화면
    private BottomNavigationView generalbottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generaluser);

        FragmentTransaction feedtransaction = getSupportFragmentManager().beginTransaction();
        FeedActivity feedActivity = new FeedActivity();
        feedtransaction.replace(R.id.frame, feedActivity);
        feedtransaction.addToBackStack(null);
        feedtransaction.commit();

        generalbottom = findViewById(R.id.navigation_view);
        generalbottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_feed:
                        FragmentTransaction feedtransaction = getSupportFragmentManager().beginTransaction();
                        FeedActivity feedActivity = new FeedActivity();
                        feedtransaction.replace(R.id.frame, feedActivity);
                        feedtransaction.addToBackStack(null);
                        feedtransaction.commit();
                        break;
                    case R.id.bottom_home:
                        FragmentTransaction hometransaction = getSupportFragmentManager().beginTransaction();
                        HomeActivity homeActivity = new HomeActivity();
                        hometransaction.replace(R.id.frame, homeActivity);
                        hometransaction.addToBackStack(null);
                        hometransaction.commit();
                        break;
                    case R.id.bottom_mychat:
                        FragmentTransaction chattransaction = getSupportFragmentManager().beginTransaction();
                        MyChatListActivity myChatListActivity = new MyChatListActivity();
                        chattransaction.replace(R.id.frame, myChatListActivity);
                        chattransaction.addToBackStack(null);
                        chattransaction.commit();
                        break;
                    case R.id.btn_mypage:
                        FragmentTransaction generalpagetransaction = getSupportFragmentManager().beginTransaction();
                        GeneralPageActivity generalPageActivity = new GeneralPageActivity();
                        generalpagetransaction.replace(R.id.frame, generalPageActivity);
                        generalpagetransaction.addToBackStack(null);
                        generalpagetransaction.commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
