package com.example.newproject;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFeedActivity extends AppCompatActivity {     //피드 작성

    private FirebaseUser user;
    private FirebaseDatabase database, second_database;
    private DatabaseReference databaseReference, second_databaseReference;

    private Uri filePath, basicPath;

    ImageView imageView, localurl;
    TextView localname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfeed);

        findViewById(R.id.imageView).setOnClickListener(onClickListener);
        findViewById(R.id.btn_add).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_update).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);
        imageView = (ImageView)findViewById(R.id.imageView);
        localurl = (ImageView)findViewById(R.id.localurl);
        localname = (TextView)findViewById(R.id.localname);

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
                case R.id.btn_add:
                    finduserinfo();
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
    public void finduserinfo(){     //feed db에 저장 후 key를 받아서 write db에 저장
        final String[] first = new String[1];
        final String[] second = new String[1];
        final String[] third = new String[1];
        final String[] localurl = new String[1];
        final String[] localname = new String[1];
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
                        save_feed(first[0], second[0], third[0], localurl[0], localname[0]);   //imageurl, name, first, second, third를 받아오기
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
                        localname.setText(name[0]);
                        Glide.with(AddFeedActivity.this).load(picture[0]).into(localurl);
                    }
                }
            }
        });
    }
    public void save_feed(final String first, final String second, final String third, final String localurl, final String localname){    //feed db에 저장
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
                    FeedInfo feedInfo = new FeedInfo(user.getUid(),localurl, localname, String.valueOf(basicPath),extratext);
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
                            FeedInfo feedInfo = new FeedInfo(user.getUid(), localurl, localname, String.valueOf(filePath), extratext);
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
    public void check(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
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
            //Log.d("TAG", "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Log.d("TAG", "uri:" + String.valueOf(filePath));
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
