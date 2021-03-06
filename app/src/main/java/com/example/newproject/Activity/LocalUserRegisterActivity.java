package com.example.newproject.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.newproject.Class.LocalUserInfo;
import com.example.newproject.R;
import com.example.newproject.Class.UserLocationInfo;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalUserRegisterActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    ImageView imageView;
    private StorageReference storageRef;
    private ArrayList<String> pathList = new ArrayList<>();

    private Uri filePath, basicPath;

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
                    break;
                case R.id.btn_gallery:
                    check();
                    break;
                case R.id.btn_update:
                    check();
                    break;
                case R.id.btn_delete:
                    delete();
                    startToast("사진이 삭제되었습니다.");
                    break;
            }

        }
    };

    public void localuserregister() {
        //이메일, 비밀번호, 위치정보를 받아서 usercreateemailandpassword라는 함수를 사용해서 사용자 정보 생성
        mAuth = FirebaseAuth.getInstance();
        final String email = ((EditText) findViewById(R.id.email)).getText().toString();
        final String password = ((EditText)findViewById(R.id.password)).getText().toString();
        final String address = ((EditText)findViewById(R.id.address)).getText().toString();
        final String name= ((EditText) findViewById(R.id.name)).getText().toString();    //name에서 좌표로 변환시키기
        final String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
        final String first = "서울특별시";
        final String second = "강서구";
        final String third = "화곡동";

        if (name.length() > 0 && email.length() > 0 && password.length() > 0 && phone.length() > 7) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                if (filePath == null) {        //사진 없으면
                                    StorageReference pathReference = storageReference.child("images/p8.jpg");
                                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            basicPath = uri;
                                            startToast("회원가입 완료");
                                            LocalUserInfo localUserInfo = new LocalUserInfo(email, name, phone, address, String.valueOf(basicPath), null, first, second, third);
                                            UserLocationInfo userLocationInfo = new UserLocationInfo(first, second, third);
                                            userUpload(localUserInfo, userLocationInfo, user.getUid(), name);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                                    //회원가입이 정상적으로 이뤄졌습니다.
                                } else {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                                    Date now = new Date();
                                    String filename = formatter.format(now) + ".png";
                                    StorageReference storageRef = storage.getReferenceFromUrl("gs://newproject-ab6cb.appspot.com/").child(user.getUid()).child(String.valueOf(filePath));
                                    storageRef.putFile(filePath)
                                            //성공시
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    //Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                                    startToast("회원가입 완료");
                                                    LocalUserInfo localUserInfo = new LocalUserInfo(email, name, phone, address, String.valueOf(filePath), null, first, second, third);
                                                    UserLocationInfo userLocationInfo = new UserLocationInfo(first, second, third);
                                                    userUpload(localUserInfo, userLocationInfo, user.getUid(), name);
                                                }
                                            })
                                            //실패시
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
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
    public void userUpload(LocalUserInfo localUserInfo, UserLocationInfo userLocationInfo, String userid, String name){
        final List<Address>[] list = new List[]{null};
        final double[] lat = new double[1];
        final double[] lon = new double[1];
        final GeoPoint[] geoPoint = new GeoPoint[1];
        final Geocoder geocoder = new Geocoder(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userid).set(localUserInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(LoginActivity.class);
                            }
                        }, 1000);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        try {
            list[0] = geocoder.getFromLocationName(name, 10);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        if(list != null) {
            if (list[0].size() == 0) {
                System.out.println("해당되는 주소가 없습니다.");
            } else {
                Address addr = list[0].get(0);
                lat[0] = addr.getLatitude();
                lon[0] = addr.getLongitude();
                System.out.println(addr.getThoroughfare());     //addr에서 현재 주소를 받아와서 행정구역명 찾을수있게 함
                System.out.println(addr.getSubThoroughfare());
                System.out.println(addr.getSubLocality());
                System.out.println(addr.getAdminArea());
                System.out.println(addr.getCountryName());
                System.out.println(addr.getSubAdminArea());
                System.out.println(addr.getLocality());
                System.out.println(addr.getLocale());
                geoPoint[0] = new GeoPoint(lat[0], lon[0]);
                db.collection("Users").document(userid).update("geoPoint", geoPoint[0]);
            }
        }
        databaseReference = FirebaseDatabase.getInstance("https://newproject-ab6cb.firebaseio.com/").getReference(userid);
        databaseReference.child("level").setValue("2");
    }
    public void check() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }
    public void delete(){
        filePath = null;
        imageView.setImageResource(R.drawable.ic_baseline_photo_camera_24);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   //왜 uri가 null인가 -> startActivityForResult를 안해줌
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d("TAG", "uri:" + String.valueOf(filePath));
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
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
