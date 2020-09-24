package com.example.newproject.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.newproject.Class.LocalUserInfo;
import com.example.newproject.Class.ServiceItemInfo;
import com.example.newproject.MyItemListActivity;
import com.example.newproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ServiceItemListDetailActivity extends AppCompatActivity {

    private String itemkey, secondkey;
    private ImageView imageView;
    private TextView textname, datelimit, extratext;

    private String first, second, third, type_num;
    private BottomNavigationView generalbottom;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;

    private Uri filePath;
    private ServiceItemInfo serviceItemInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceitemdetail);

        findViewById(R.id.imageView).setOnClickListener(onClickListener);
        findViewById(R.id.btn_med).setOnClickListener(onClickListener);
        findViewById(R.id.btn_emer).setOnClickListener(onClickListener);
        findViewById(R.id.btn_loc).setOnClickListener(onClickListener);
        findViewById(R.id.btn_etc).setOnClickListener(onClickListener);

        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_updatepicture).setOnClickListener(onClickListener);
        findViewById(R.id.btn_deletepicture).setOnClickListener(onClickListener);


        itemkey = getIntent().getStringExtra("itemkey");
        secondkey = getIntent().getStringExtra("secondkey");
        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");
        //userid = getIntent().getStringExtra("userid");   //글 올린사람의 id
        imageView = (ImageView) findViewById(R.id.imageView);
        textname = (TextView) findViewById(R.id.textname);
        datelimit = (TextView) findViewById(R.id.datelimit);
        extratext = (TextView) findViewById(R.id.extratext);
        generalbottom = findViewById(R.id.generalbottom);
        generalbottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.btn_update:
                        update();
                        break;
                    case R.id.btn_delete:
                        delete();
                        break;
                }
                return true;
            }
        });
        findservice();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_med:
                    type_num = "1";
                    break;
                case R.id.btn_emer:
                    type_num = "2";
                    break;
                case R.id.btn_loc:
                    type_num = "3";
                    break;
                case R.id.btn_etc:
                    type_num = "0";
                    break;
                case R.id.imageView:
                    CardView cardView = findViewById(R.id.btn_cardview);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }
                    else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_gallery:
                    check();
                    break;
                case R.id.btn_updatepicture:
                    check();
                    break;
                case R.id.btn_deletepicture:
                    delete_picture();
                    break;
            }
        }
    };

    public void findservice() {
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("service").child(first).child(second).child(third).child(itemkey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceItemInfo = snapshot.getValue(ServiceItemInfo.class);
                Glide.with(ServiceItemListDetailActivity.this).load(serviceItemInfo.getImageurl()).into(imageView);
                textname.setText(serviceItemInfo.getTextname());
                datelimit.setText(serviceItemInfo.getDatelimit());
                extratext.setText(serviceItemInfo.getExtratext());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void delete() {
        //데이터 삭제시에 removevalue
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        database.getReference(user.getUid()).child("service").child(secondkey).removeValue();

        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        second_database.getReference("service").child(first).child(second).child(third).child(itemkey).removeValue();
        startToast("게시글이 삭제되었습니다.");
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MyItemListActivity.class);
            }
        }, 1000);
    }
    public void update() {
        //데이터를 업데이트 하고 싶다면, 에디트텍스트로 내용을 받아서 새로운 객체를 생성 한뒤, setvalue로 새로운 객체 값을 넣어줌
        user = FirebaseAuth.getInstance().getCurrentUser();
        String localurl = null;
        String imageurl = null;
        if(serviceItemInfo.getLocalurl() != null) {
            localurl = serviceItemInfo.getLocalurl();
        }
        if(serviceItemInfo.getImageurl() != null){
            imageurl = serviceItemInfo.getImageurl();
        }
        String localname = serviceItemInfo.getLocalname();
        String noti = serviceItemInfo.getNoti();
        String type_num = serviceItemInfo.getType_num();
        String day;
        String phone = serviceItemInfo.getPhone();
        String address = serviceItemInfo.getAddress();

        String datelimit = ((EditText)findViewById(R.id.datelimit)).getText().toString();
        String textname = ((EditText) findViewById(R.id.textname)).getText().toString();
        String extratext = ((EditText) findViewById(R.id.extratext)).getText().toString();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat("dd", Locale.getDefault());

        day = year.format(currentTime) + "/"+ month.format(currentTime) +"/"+date.format(currentTime);

        ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), type_num, address, phone, day, noti, datelimit, localname, String.valueOf(localurl), String.valueOf(filePath), textname, extratext, "open", null);

        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        database.getReference("service").child(first).child(second).child(third).child(itemkey).setValue(serviceItemInfo);
        startToast("게시물이 수정되었습니다.");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MyItemListActivity.class);
            }
        }, 1000);
    }
    public void check(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   //왜 uri가 null인가 -> startActivityForResult를 안해줌
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                this.getContentResolver().takePersistableUriPermission(filePath, takeFlags);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void delete_picture(){
        filePath = null;
        imageView.setVisibility(View.INVISIBLE);
    }
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
