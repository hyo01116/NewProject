package com.example.newproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    //부모클래스의 oncreate를 상속받아 사용하는 것이기 때문에 재정의 해줘야함
    //super.oncreate를 통해 액티비티를 만드는 기본 코드를 실행시켜주고 그다음에 내가 필요한 코드를 oncreate에서 실행
    //view설정, 버튼 findviewbyid
    protected void onCreate(Bundle savedInstanceState) {
        //super는 부모클래스의 생성자를 불러서 초기화해주는것
        //this는 현재 클래스의 인스턴스
        //this()는 현재클래스의 생성자 부를때 사용
        //부모 클래스의 oncreate를 사용하기 위해서는 super를 통해 oncreate를 호출한다
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_login).setOnClickListener(onClickListener);
        findViewById(R.id.btn_register).setOnClickListener(onClickListener);
        //xml에서 설정된 뷰들을 가져오는 메소드
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
                default:
                    break;
            }
        }
    };
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}