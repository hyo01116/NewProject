package com.example.newproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.newproject.Class.FeedInfo;
import com.example.newproject.Class.GeneralUserInfo;
import com.example.newproject.Class.LocalUserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class AddFeedActivity extends AppCompatActivity {     //피드 작성

    private FirebaseUser user;
    private FirebaseDatabase database, second_database;
    private DatabaseReference databaseReference, second_databaseReference;

    private Uri filePath, basicPath;
    private BottomNavigationView bottomNavigationView;
    ImageView imageView;

    private String user_level = "1";

    MyApplication myApplication;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfeed);

        findViewById(R.id.imageView).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_update).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);
        imageView = (ImageView)findViewById(R.id.imageView);

        myApplication = (MyApplication) getApplicationContext();
        user_level = myApplication.getUser_level();
        System.out.println(user_level);

        bottomNavigationView = findViewById(R.id.btn_navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btn_add:
                        if(user_level.equals("1")){
                            findgeneraluserinfo();
                        }
                        else {
                            findlocaluserinfo();
                        }
                    break;
                }
                return false;
            }
        });
        setuserinfo();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
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
                case R.id.btn_update:
                    check();
                    break;
                case R.id.btn_delete:
                    delete_picture();
                    break;
            }
        }
    };
    public void findgeneraluserinfo(){
        final String[] first = new String[1];
        final String[] second = new String[1];
        final String[] third = new String[1];
        final String[] generalurl = new String[1];
        final String[] generalname = new String[1];
        final String[] address = new String[1];
        final String[] phone = new String[1];
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(user.getUid());    //현재 로그인한 사람의 주소
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        GeneralUserInfo generalUserInfo = documentSnapshot.toObject(GeneralUserInfo.class);
                        generalurl[0] = generalUserInfo.getImageurl();
                        generalname[0] = generalUserInfo.getName();
                        first[0] = generalUserInfo.getFirst();
                        second[0] = generalUserInfo.getSecond();
                        third[0] = generalUserInfo.getThird();
                        address[0] = "1";      //level = 1은 주소와 전화번호 표시 안함
                        phone[0] = "1";
                        save_feed(first[0], second[0], third[0], generalurl[0], generalname[0], address[0], phone[0]);   //imageurl, name, first, second, third를 받아오기
                    }
                }
            }
        });
    }
    public void findlocaluserinfo(){     //feed db에 저장 후 key를 받아서 write db에 저장
        final String[] first = new String[1];
        final String[] second = new String[1];
        final String[] third = new String[1];
        final String[] localurl = new String[1];
        final String[] localname = new String[1];
        final String[] address = new String[1];
        final String[] phone = new String[1];
        //feed db에 저장하기 위해서 first, second, third가 필요
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(user.getUid());    //현재 로그인한 사람의 주소
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        LocalUserInfo localUserInfo = documentSnapshot.toObject(LocalUserInfo.class);
                        localurl[0] = localUserInfo.getImageurl();
                        localname[0] = localUserInfo.getName();
                        first[0] = localUserInfo.getFirst();
                        second[0] = localUserInfo.getSecond();
                        third[0] = localUserInfo.getThird();
                        address[0] = localUserInfo.getAddress();
                        phone[0] = localUserInfo.getPhone();
                        save_feed(first[0], second[0], third[0], localurl[0], localname[0], address[0], phone[0]);   //imageurl, name, first, second, third를 받아오기
                    }
                }
            }
        });
    }
    public void setuserinfo(){
        final String[] picture = new String[1];
        final String[] name = new String[1];
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(user.getUid());    //현재 로그인한 사람의 주소
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        LocalUserInfo localUserInfo = documentSnapshot.toObject(LocalUserInfo.class);
                        picture[0] = localUserInfo.getImageurl();
                        name[0] = localUserInfo.getName();
                    }
                }
            }
        });
    }
    public void save_feed(final String first, final String second, final String third, final String localurl, final String localname, final String address, final String phone){    //feed db에 저장
        //userid, localurl, localname, picture, extratext
        final String extratext = ((EditText)findViewById(R.id.extratext)).getText().toString();
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        if(filePath == null){
            StorageReference pathReference = storageReference.child("images/p8.jpg");
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    basicPath = uri;
                    FeedInfo feedInfo = new FeedInfo(user.getUid(), localurl, localname, address, phone, String.valueOf(basicPath), extratext);
                    uploader(feedInfo, first, second, third);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        else {
            /*SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";*/
            StorageReference storageRef = storage.getReferenceFromUrl("gs://newproject-ab6cb.appspot.com/").child(user.getUid()).child(String.valueOf(filePath));
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            FeedInfo feedInfo = new FeedInfo(user.getUid(), localurl,  localname, address, phone, String.valueOf(filePath), extratext);
                            uploader(feedInfo, first, second, third);
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void uploader(final FeedInfo feedInfo, String first, String second, String third){
        final String[] key = new String[1];
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-feed.firebaseio.com/");
        databaseReference = database.getReference().child(first).child(second).child(third);

        final DatabaseReference newdatabaseReference = databaseReference.push();
        newdatabaseReference.setValue(feedInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        key[0] = newdatabaseReference.getKey();      //해당 피드의 키
                        second_uploader(key[0]);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    public void second_uploader(String itemkey){     //유저마다 쓴 글 저장
        user = FirebaseAuth.getInstance().getCurrentUser();
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");

        second_databaseReference = second_database.getReference(user.getUid()).child("feed");
        DatabaseReference second_newdatabaseReference = second_databaseReference.push();
        second_newdatabaseReference.setValue(itemkey)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void check() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   //왜 uri가 null인가 -> startActivityForResult를 안해줌
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
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
    public void delete_picture(){
        filePath = null;
        //이미지 삭제시 사라지게 하는법
        imageView.setVisibility(View.INVISIBLE);
    }
}
