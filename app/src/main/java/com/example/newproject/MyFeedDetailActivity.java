package com.example.newproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.newproject.Class.FeedInfo;
import com.example.newproject.Class.LocalUserInfo;
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

public class MyFeedDetailActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseDatabase database, second_database;
    private DatabaseReference databaseReference, second_databaseReference;

    private Uri filePath;

    ImageView localurl, imageView;
    TextView localname, extratext;

    private String first, second, third, key, secondkey, address, phone;
    private String local_url;
    private String local_name;
    private String picture;
    private String text;

    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeddetail);

        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_updatepicture).setOnClickListener(onClickListener);
        findViewById(R.id.btn_deletepicture).setOnClickListener(onClickListener);
        findViewById(R.id.imageView).setOnClickListener(onClickListener);

        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {   //버튼 안됨
                switch (menuItem.getItemId()){
                    case R.id.btn_update:
                        System.out.println("1");
                        update();
                        startToast("게시물이 수정되었습니다.");
                        break;
                    case R.id.btn_delete:
                        delete();
                        startToast("게시물이 삭제되었습니다.");
                }
                return true;
            }
        });

        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");
        key = getIntent().getStringExtra("key");
        secondkey = getIntent().getStringExtra("secondkey");

        imageView = (ImageView)findViewById(R.id.imageView);
        extratext = (TextView) findViewById(R.id.extratext);
        finduserinfo();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
                    System.out.println("gallery");
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
    public void finduserinfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        LocalUserInfo localUserInfo = documentSnapshot.toObject(LocalUserInfo.class);
                        first = localUserInfo.getFirst();
                        second = localUserInfo.getSecond();
                        third = localUserInfo.getThird();
                        local_url = localUserInfo.getImageurl();
                        local_name = localUserInfo.getName();
                        address = localUserInfo.getAddress();
                        phone = localUserInfo.getPhone();
                        findfeed(first, second, third);
                    }
                }
            }
        });
    }
    public void findfeed(String first, String second, String third) {
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        databaseReference = database.getReference().child(first).child(second).child(third).child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FeedInfo feedInfo = snapshot.getValue(FeedInfo.class);
                local_url = feedInfo.getLocalurl();
                local_name = feedInfo.getLocalname();
                filePath = Uri.parse(feedInfo.getPicture());
                text = feedInfo.getExtratext();
                address = feedInfo.getAddress();
                phone = feedInfo.getPhone();

                Glide.with(MyFeedDetailActivity.this).load(filePath).into(imageView);
                extratext.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void delete() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        database.getReference(user.getUid()).child("feed").child(secondkey).removeValue();

        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        second_database.getReference().child(first).child(second).child(third).child(key).removeValue();
    }
    public void update() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        text = ((EditText) findViewById(R.id.extratext)).getText().toString();
        FeedInfo feedInfo = new FeedInfo(user.getUid(), local_url, local_name, address, phone, String.valueOf(filePath), text);

        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        database.getReference().child(first).child(second).child(third).child(key).setValue(feedInfo);
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
            Log.d("TAG", "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
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

}
