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
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.MyItemListActivity;
import com.example.newproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StuffItemListDetailActivity extends AppCompatActivity {

    private String itemkey, secondkey;
    private ImageView imageView;
    private TextView textname, extratext, datelimit;

    private String first, second, third, type_num;
    private BottomNavigationView generalbottom;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;

    private Uri filePath;
    private StuffItemInfo stuffItemInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuffitemdetail);

        findViewById(R.id.btn_food).setOnClickListener(onClickListener);
        findViewById(R.id.btn_wear).setOnClickListener(onClickListener);
        findViewById(R.id.btn_item).setOnClickListener(onClickListener);
        findViewById(R.id.btn_etc).setOnClickListener(onClickListener);
        findViewById(R.id.imageView).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_updatepicture).setOnClickListener(onClickListener);
        findViewById(R.id.btn_deletepicture).setOnClickListener(onClickListener);


        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");
        itemkey = getIntent().getStringExtra("itemkey");    //base - stuff db에서 아이템의 key
        secondkey = getIntent().getStringExtra("secondkey");    //write db에서의 아이템의 key
        //System.out.println("key: "+itemkey);
        //userid = getIntent().getStringExtra("userid");   //글 올린사람의 id
        imageView = (ImageView) findViewById(R.id.imageView);
        textname = (TextView) findViewById(R.id.textname);
        datelimit = (TextView) findViewById(R.id.datelimit);
        extratext = (TextView) findViewById(R.id.extratext);

        generalbottom = findViewById(R.id.navigation_view);
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

        findstuff();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageView:
                    CardView cardView2 = findViewById(R.id.btn_cardview);
                    if (cardView2.getVisibility() == View.VISIBLE) {
                        cardView2.setVisibility(View.GONE);
                    } else {
                        cardView2.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_food:
                    type_num = "1";
                    startToast("분류 : 식품");
                    break;
                case R.id.btn_wear:
                    type_num = "2";
                    startToast("분류 : 의류");
                    break;
                case R.id.btn_item:
                    type_num = "3";
                    startToast("분류 : 생활용품");
                    break;
                case R.id.btn_etc:
                    type_num = "0";
                    startToast("분류 : 기타");
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

    public void findstuff() {     //유저 글 보여주기
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("stuff").child(first).child(second).child(third).child(itemkey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stuffItemInfo = snapshot.getValue(StuffItemInfo.class);
                Glide.with(StuffItemListDetailActivity.this).load(stuffItemInfo.getImageurl()).into(imageView);
                textname.setText(stuffItemInfo.getTextname());
                datelimit.setText(stuffItemInfo.getDatelimit());
                extratext.setText(stuffItemInfo.getExtratext());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void delete() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        database.getReference(user.getUid()).child("stuff").child(secondkey).removeValue();
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        second_database.getReference("stuff").child(first).child(second).child(third).child(itemkey).removeValue();
        startToast("게시글이 삭제되었습니다.");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MyItemListActivity.class);
            }
        }, 1000);
    }

    public void update() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String localurl = stuffItemInfo.getLocalurl();
        String localname = stuffItemInfo.getLocalname();
        String imageurl = stuffItemInfo.getImageurl();     //사진 수정버튼 만들기
        String noti = stuffItemInfo.getNoti();
        String type_num = stuffItemInfo.getType_num();
        String day;
        String phone = stuffItemInfo.getPhone();
        String address = stuffItemInfo.getAddress();

        String datelimit = ((EditText) findViewById(R.id.datelimit)).getText().toString();
        String textname = ((EditText) findViewById(R.id.textname)).getText().toString();
        String extratext = ((EditText) findViewById(R.id.extratext)).getText().toString();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat("dd", Locale.getDefault());

        day = year.format(currentTime) + "/" + month.format(currentTime) + "/" + date.format(currentTime);

        System.out.println(textname);
        StuffItemInfo stuffItemInfo = new StuffItemInfo(type_num, user.getUid(), day, noti, datelimit, phone, address, localname, String.valueOf(localurl), String.valueOf(filePath), textname, extratext, "open", null);
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        database.getReference("stuff").child(first).child(second).child(third).child(itemkey).setValue(stuffItemInfo);
        startToast("게시글이 수정되었습니다.");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MyItemListActivity.class);
            }
        }, 1000);
    }

    public void check() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   //왜 uri가 null인가 -> startActivityForResult를 안해줌
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                this.getContentResolver().takePersistableUriPermission(filePath, takeFlags);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete_picture() {
        filePath = null;
        imageView.setVisibility(View.INVISIBLE);
    }

    public void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
