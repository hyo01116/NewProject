package com.example.newproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.gms.tasks.Continuation;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddServiceItemActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;

    private String imageurl, localname, address, localurl, key;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addserviceitem);

        findViewById(R.id.imageurl).setOnClickListener(onClickListener);
        findViewById(R.id.btn_save_add).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageurl:
                    CardView cardView = findViewById(R.id.btn_cardview);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }
                    else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_save_add:
                    addservice();
                    Handler timer = new Handler();
                    /*timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), LocalUserActivity.class);
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
        final String service = ((EditText)findViewById(R.id.service)).getText().toString();
        final String address = ((EditText)findViewById(R.id.address)).getText().toString();
        final String datelimit = ((EditText)findViewById(R.id.datelimit)).getText().toString();
        final String extratext =((EditText)findViewById(R.id.extratext)).getText().toString();
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        final StorageReference mounatainImagesRef = storageReference.child("users/"+user.getUid()+"/foodimages.jpg");

        if(imageurl == null){
            ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), localname, address, localurl, textname, service, datelimit, extratext, "open", null);
            uploader(serviceItemInfo, first, second, third);
        }
        else {
            try{
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
            }
        }
    }
    public void uploader(final ServiceItemInfo serviceItemInfo, String first, String second, String third){
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-service.firebaseio.com/");
        databaseReference = database.getReference(first).child(second).child(third);

        final DatabaseReference newdatabaseReference = databaseReference.push();
        newdatabaseReference.setValue(serviceItemInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("등록에 성공하였습니다.");
                        key = newdatabaseReference.getKey();
                        second_uploader(serviceItemInfo, key);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("등록에 실패했습니다.");
                    }
                });
    }
    public void second_uploader(ServiceItemInfo serviceItemInfo, String itemkey){     //유저마다 쓴 글 저장
        user = FirebaseAuth.getInstance().getCurrentUser();
        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");

        second_databaseReference = second_database.getReference(user.getUid()).child("service");
        DatabaseReference second_newdatabaseReference = second_databaseReference.push();
        serviceItemInfo.setKey(itemkey);
        second_newdatabaseReference.setValue(serviceItemInfo)
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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
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
        }
    }
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}
