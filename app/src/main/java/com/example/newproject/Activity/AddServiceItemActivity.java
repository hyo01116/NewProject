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
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.newproject.AddFeedActivity;
import com.example.newproject.Class.GeneralUserInfo;
import com.example.newproject.Class.LocalUserInfo;
import com.example.newproject.Class.ServiceItemInfo;
import com.example.newproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddServiceItemActivity extends BaseActivity {
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;

    private String localname, key, noti, type_num, address, phone;
    private BottomNavigationView generalbottom;

    ImageView imageView;
    private CardView cardView;
    private Uri filePath, basicPath, localurl;
    private TextView user_name, user_address;
    private ImageView imageView2;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addserviceitem);

        imageView = (ImageView)findViewById(R.id.imageView);
        user_name = (TextView)findViewById(R.id.user_name);
        user_address = (TextView)findViewById(R.id.user_address);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        findViewById(R.id.imageView).setOnClickListener(onClickListener);   //아직 사진 안넣음

        noti = "0";
        type_num = "0";

        findViewById(R.id.btn_med).setOnClickListener(onClickListener);
        findViewById(R.id.btn_emer).setOnClickListener(onClickListener);
        findViewById(R.id.btn_loc).setOnClickListener(onClickListener);
        findViewById(R.id.btn_etc).setOnClickListener(onClickListener);

        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_update).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);
        //수정, 삭제 버튼 추가

        generalbottom = findViewById(R.id.navigation_view);
        generalbottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.btn_noti:    //긴급
                        if(noti.equals("0")){
                            noti = "1";
                            startToast("긴급 등록");
                        }
                        else{
                            noti = "0";
                            startToast("긴급 해제");
                        }
                        break;
                    case R.id.btn_add:  //저장
                        addservice();
                        break;
                }
                return true;
            }
        });
        setuserinfo();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView:
                    cardView = findViewById(R.id.btn_cardview);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }
                    else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_med:
                    type_num = "1";
                    startToast("'보건의료'로 등록됩니다.");
                    break;
                case R.id.btn_emer:
                    type_num = "2";
                    startToast("'재해/재난'으로 등록됩니다.");
                    break;
                case R.id.btn_loc:
                    type_num = "3";
                    startToast("'농/어촌'으로 등록됩니다.");
                    break;
                case R.id.btn_etc:
                    type_num = "0";
                    startToast("기타");
                    break;
                case R.id.btn_gallery:
                    check();
                    break;
                case R.id.btn_update:
                    check();
                    break;
                case R.id.btn_delete:
                    delete_picture();
                    startToast("사진이 삭제되었습니다.");
                    break;
            }
        }
    };
    public void setuserinfo(){
        final String[] picture = new String[1];
        final String[] name = new String[1];
        final String[] address = new String[1];
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
                        address[0] = localUserInfo.getAddress();
                        Glide.with(AddServiceItemActivity.this).load(picture[0]).into(imageView2);
                        user_name.setText(name[0]);
                        user_address.setText(address[0]);
                    }
                }
            }
        });
    }

    public void addservice(){
        final String[] first = new String[1];
        final String[] second = new String[1];
        final String[] third = new String[1];
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
                        first[0] = localUserInfo.getFirst();
                        second[0] = localUserInfo.getSecond();
                        third[0] = localUserInfo.getThird();
                        localname = localUserInfo.getName();
                        localurl = Uri.parse(localUserInfo.getImageurl());
                        address = localUserInfo.getAddress();
                        phone = localUserInfo.getPhone();
                        addserviceinfo(first[0], second[0], third[0], address, phone, localname, String.valueOf(localurl));
                    }
                }
            }
        });
    }
    public void addserviceinfo(final String first, final String second, final String third, final String address, final String phone, final String localname, final String localurl){
        final String textname = ((EditText)findViewById(R.id.textname)).getText().toString();
        final String extratext =((EditText)findViewById(R.id.extratext)).getText().toString();
        final String datelimit= ((EditText)findViewById(R.id.date)).getText().toString();
        final String day;
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat("dd", Locale.getDefault());
        day = year.format(currentTime) + "/"+ month.format(currentTime) +"/"+date.format(currentTime);

        if(filePath == null){
            StorageReference pathReference = storageReference.child("images/p8.jpg");
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    basicPath = uri;
                    startToast("게시글이 등록되었습니다.");
                    ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), type_num, address, phone, day, noti, datelimit, localname, localurl, String.valueOf(basicPath), textname, extratext, "open", null);
                    uploader(serviceItemInfo, first, second, third);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) +".png";
            StorageReference storageRef = storage.getReferenceFromUrl("gs://newproject-ab6cb.appspot.com/").child(user.getUid()).child(String.valueOf(filePath));
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            startToast("게시글이 등록되었습니다.");
                            ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), type_num, address, phone, day, noti, datelimit, localname, localurl, String.valueOf(filePath), textname, extratext, "open", null);
                            uploader(serviceItemInfo, first, second, third);
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }
    public void uploader(final ServiceItemInfo serviceItemInfo, String first, String second, String third){
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("service").child(first).child(second).child(third);

        final DatabaseReference newdatabaseReference = databaseReference.push();
        newdatabaseReference.setValue(serviceItemInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        key = newdatabaseReference.getKey();
                        Handler handler =new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(LocalUserActivity.class);
                            }
                        }, 1000);
                        second_uploader(key);
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

        second_databaseReference = second_database.getReference(user.getUid()).child("service");
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
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   //왜 uri가 null인가 -> startActivityForResult를 안해줌
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            //Log.d("TAG", "uri:" + String.valueOf(filePath));
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                if(cardView.getVisibility() == View.VISIBLE){
                    cardView.setVisibility(View.GONE);
                }
                else{
                    cardView.setVisibility(View.VISIBLE);
                }
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
