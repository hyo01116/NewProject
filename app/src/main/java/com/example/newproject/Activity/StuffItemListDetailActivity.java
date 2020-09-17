package com.example.newproject.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.newproject.Class.StuffItemInfo;
import com.example.newproject.R;
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
    private ImageView imageurl, localurl;
    private TextView textname, localname,  extratext;

    private String first, second, third;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;

    private Uri filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuffitemdetail);

        findViewById(R.id.btn_update).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);
        findViewById(R.id.btn_clear).setOnClickListener(onClickListener);
        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");
        itemkey = getIntent().getStringExtra("itemkey");    //base - stuff db에서 아이템의 key
        secondkey = getIntent().getStringExtra("secondkey");    //write db에서의 아이템의 key
        //System.out.println("key: "+itemkey);
        //userid = getIntent().getStringExtra("userid");   //글 올린사람의 id
        imageurl = (ImageView) findViewById(R.id.imageurl);
        textname = (TextView) findViewById(R.id.textname);
        localname = (TextView) findViewById(R.id.localname);
        localurl = (ImageView)findViewById(R.id.localurl);
        extratext = (TextView) findViewById(R.id.extratext);
        findstuff();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_update:    //변화된게 없다면 고대로 다시 저장
                    update();
                    startToast("게시물이 수정되었습니다.");
                    //startfunction();
                    break;
                case R.id.btn_delete:    //db에서 해당 글 모두 삭제
                    delete();
                    startToast("게시물이 삭제되었습니다.");
                    //startfunction();
                    break;
                case R.id.btn_clear:     //state를 close로 변화 + 리스트에 나타낼때 state가 close인 경우 회색으로 표시
                    itemclear_basicdb();
                    startToast("게시물이 거래완료 되었습니다.");
                    //startfunction();
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
                StuffItemInfo stuffItemInfo = snapshot.getValue(StuffItemInfo.class);
                Glide.with(StuffItemListDetailActivity.this).load(stuffItemInfo.getImageurl()).into(imageurl);
                Glide.with(StuffItemListDetailActivity.this).load(stuffItemInfo.getLocalurl()).into(localurl);
                textname.setText(stuffItemInfo.getTextname());
                localname.setText(stuffItemInfo.getLocalname());
                extratext.setText(stuffItemInfo.getExtratext());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void itemclear_basicdb() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        database.getReference("stuff").child(first).child(second).child(third).child(itemkey).child("state").setValue("close");
    }

    public void delete() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        database.getReference(user.getUid()).child("stuff").child(secondkey).removeValue();
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        second_database.getReference("stuff").child(first).child(second).child(third).child(itemkey).removeValue();
    }
    public void update() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        File localurl = null;
        File imageurl = null;     //사진 수정버튼 만들기
        String noti = "0";
        String datelimit="0";
        String day;
        String phone = "0109999999";
        String address = "주소";
        String localname = ((TextView)findViewById(R.id.localname)).getText().toString();
        String textname = ((EditText) findViewById(R.id.textname)).getText().toString();
        String extratext = ((EditText) findViewById(R.id.extratext)).getText().toString();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("mm", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat("dd", Locale.getDefault());

        day = year.format(currentTime) + "/"+ month.format(currentTime) +"/"+date.format(currentTime);

        StuffItemInfo stuffItemInfo = new StuffItemInfo(user.getUid(), day, noti, datelimit, phone, address, localname, String.valueOf(localurl), String.valueOf(imageurl), textname, extratext, "open", null);
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        database.getReference("stuff").child(first).child(second).child(third).child(itemkey).setValue(stuffItemInfo);
    }
    public void check(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
        /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                startToast("권한 거부");
            }
        }
        else{
            startActivity(GalleryActivity.class);
        }*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   //왜 uri가 null인가 -> startActivityForResult를 안해줌
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("TAG", "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageurl.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void delete_picture(){
        filePath = null;
        localurl.setImageResource(R.drawable.ic_baseline_photo_camera_24);
    }
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
