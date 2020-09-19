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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class AddServiceItemActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;

    private String localurl, localname, key, noti, type_num;
    private BottomNavigationView generalbottom;
    ImageView imageView;
    private Uri filePath, basicPath;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addserviceitem);

        imageView = (ImageView)findViewById(R.id.imageView);
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
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                /*case R.id.btn_photo:
                    CardView cardView = findViewById(R.id.btn_cardview);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }
                    else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;*/
                case R.id.imageView:
                    CardView cardView2 = findViewById(R.id.btn_cardview);
                    if(cardView2.getVisibility() == View.VISIBLE){
                        cardView2.setVisibility(View.GONE);
                    }
                    else{
                        cardView2.setVisibility(View.VISIBLE);
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
                        localurl = localUserInfo.getImageurl();
                        addserviceinfo(first[0], second[0], third[0], localname, localurl);
                    }
                }
            }
        });
    }
    public void addserviceinfo(final String first, final String second, final String third, final String localname, final String localurl){
        final String textname = ((EditText)findViewById(R.id.textname)).getText().toString();
        final String extratext =((EditText)findViewById(R.id.extratext)).getText().toString();
        final String datelimit= ((EditText)findViewById(R.id.date)).getText().toString();
        final String day;
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat month = new SimpleDateFormat("mm", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat("dd", Locale.getDefault());
        day = year.format(currentTime) + "/"+ month.format(currentTime) +"/"+date.format(currentTime);

        if(filePath == null){
            StorageReference pathReference = storageReference.child("images/p8.jpg");
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    basicPath = uri;
                    ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), day, noti, datelimit, localname, localurl, String.valueOf(basicPath), textname, extratext, "open", null);
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
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), day, noti, datelimit, localname, localurl, String.valueOf(filePath), textname, extratext, "open", null);
                            uploader(serviceItemInfo, first, second, third);
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });
            /*try{
                InputStream stream = new FileInputStream(new File(imageurl));
                final UploadTask uploadTask = mounatainImagesRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return mounatainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), localname, address, localurl, imageurl, textname, service, datelimit, extratext, "open", null);
                            uploader(serviceItemInfo, first, second, third);
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
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
                        startToast("등록에 성공하였습니다.");
                        key = newdatabaseReference.getKey();
                        second_uploader(key);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("등록에 실패했습니다.");
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
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}
