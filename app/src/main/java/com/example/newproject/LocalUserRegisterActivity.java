package com.example.newproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalUserRegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    ImageView imageView;
    private StorageReference storageRef;
    private ArrayList<String> pathList = new ArrayList<>();

    private Uri filePath;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localuserregister);

        imageView = (ImageView)findViewById(R.id.imageView);

        findViewById(R.id.imageView).setOnClickListener(onClickListener);
        findViewById(R.id.btn_save_add).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
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
                case R.id.btn_save_add:
                    localuserregister();
                    /*Handler timer = new Handler();
                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }, 2000);*/
                    break;
                case R.id.btn_gallery:
                    check();
                    break;
            }

        }
    };

    public void localuserregister() {
        mAuth = FirebaseAuth.getInstance();
        final String email = ((EditText) findViewById(R.id.email)).getText().toString();
        final String password = ((EditText)findViewById(R.id.password)).getText().toString();
        final String name= ((EditText) findViewById(R.id.name)).getText().toString();
        final String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
        final String first = "서울특별시";
        final String second = "강남구";
        final String third = "서초동";

        if (name.length() > 0 && email.length() > 0 && password.length() > 0 && phone.length() > 7) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                if(filePath == null){        //사진 없으면
                                    LocalUserInfo localUserInfo = new LocalUserInfo(email, name, phone, first, second, third);
                                    UserLocationInfo userLocationInfo = new UserLocationInfo(first, second, third);
                                    userUpload(localUserInfo, userLocationInfo, user.getUid());
                                    //회원가입이 정상적으로 이뤄졌습니다.
                                }
                                else{
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                                    Date now = new Date();
                                    String filename = formatter.format(now) +".png";
                                    StorageReference storageRef = storage.getReferenceFromUrl("gs://newproject-ab6cb.appspot.com/").child(user.getUid()).child(String.valueOf(filePath));
                                    storageRef.putFile(filePath)
                                            //성공시
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                                    LocalUserInfo localUserInfo = new LocalUserInfo(email, name, phone, String.valueOf(filePath), first,second, third);
                                                    UserLocationInfo userLocationInfo = new UserLocationInfo(first, second, third);
                                                    userUpload(localUserInfo, userLocationInfo, user.getUid());
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
                                /*user = mAuth.getCurrentUser();
                                LocalUserInfo localUserInfo = new LocalUserInfo(email, name, phone, first, second, third);
                                UserLocationInfo userLocationInfo = new UserLocationInfo(first, second, third);
                                userUpload(localUserInfo, userLocationInfo, user.getUid());
                                startToast("회원가입이 정상적으로 이뤄졌습니다.");*/
                            } else {
                                if (task.getException() != null) {
                                    startToast(task.getException().toString());
                                }
                            }
                        }
                    });
        }
        else{
            startToast("다시 입력해주세요");
        }
    }
    public void userUpload(LocalUserInfo localUserInfo, UserLocationInfo userLocationInfo, String userid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userid).set(localUserInfo)
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
        databaseReference = FirebaseDatabase.getInstance("https://newproject-ab6cb.firebaseio.com/").getReference(userid);
        databaseReference.child("level").setValue("2");
    }
    public void check() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
