package com.example.newproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.newproject.MyChatListActivity;
import com.example.newproject.MyPageActivity;
import com.example.newproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class LocalUserActivity extends AppCompatActivity{    //작성자 사용화면
    private BottomNavigationView bottomNavigationView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localuser);

        FragmentTransaction feedtransaction = getSupportFragmentManager().beginTransaction();
        FeedActivity feedActivity = new FeedActivity();
        feedtransaction.replace(R.id.frame, feedActivity);
        feedtransaction.addToBackStack(null);
        feedtransaction.commit();

        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    case R.id.bottom_add:     //카드뷰로 stuff인지 service인지 확인하기
                        CardView cardView = findViewById(R.id.btn_cardview);
                        if(cardView.getVisibility() == View.VISIBLE){
                            cardView.setVisibility(View.GONE);
                        }
                        else{
                            cardView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.bottom_mychat:
                        FragmentTransaction chattransaction = getSupportFragmentManager().beginTransaction();
                        MyChatListActivity myChatListActivity = new MyChatListActivity();
                        chattransaction.replace(R.id.frame, myChatListActivity);
                        chattransaction.addToBackStack(null);
                        chattransaction.commit();
                        break;
                    case R.id.btn_mypage:
                        FragmentTransaction mypagetransaction = getSupportFragmentManager().beginTransaction();
                        MyPageActivity myPageActivity = new MyPageActivity();
                        mypagetransaction.replace(R.id.frame, myPageActivity);
                        mypagetransaction.addToBackStack(null);
                        mypagetransaction.commit();
                        break;
                }
                return true;
            }
        });
        findViewById(R.id.btn_stuff).setOnClickListener(onClickListener);
        findViewById(R.id.btn_service).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_stuff:
                    startActivity(AddStuffItemActivity.class);
                    break;
                case R.id.btn_service:
                    startActivity(AddServiceItemActivity.class);
                    break;
            }
        }
    };
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
