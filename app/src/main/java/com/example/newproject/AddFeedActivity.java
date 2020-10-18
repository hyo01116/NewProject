package com.example.newproject;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.newproject.Activity.BaseActivity;
import com.example.newproject.Activity.LocalUserActivity;
import com.example.newproject.Class.FeedInfo;
import com.example.newproject.Class.GeneralUserInfo;
import com.example.newproject.Class.LocalUserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AddFeedActivity extends BaseActivity {     //피드 작성
    //피드 작성후에 자동으로 feedactivity가 실행되게함 (2초 후)

    private FirebaseUser user;
    private FirebaseDatabase database, second_database;
    private DatabaseReference databaseReference, second_databaseReference;

    private Uri filePath, basicPath;
    private BottomNavigationView bottomNavigationView;
    ImageView imageView, imageView2, new_imageView;

    private CardView cardView;

    private TextView user_name, user_address;

    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1;
    boolean permissionCheck = false;

    private String user_level = "1";

    MyApplication myApplication;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton btn_share, btn_twitter, btn_instagram, btn_facebook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfeed);

        findViewById(R.id.imageView).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);

        imageView = (ImageView)findViewById(R.id.imageView);
        new_imageView = (ImageView)findViewById(R.id.new_imageView);
        user_name = (TextView)findViewById(R.id.user_name);
        user_address = (TextView)findViewById(R.id.user_address);
        imageView2 = (ImageView)findViewById(R.id.imageView2);

        myApplication = (MyApplication) getApplicationContext();
        user_level = myApplication.getUser_level();
        System.out.println(user_level);

        fab_open  = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        /*btn_share = (FloatingActionButton) findViewById(R.id.btn_share);
        btn_twitter = (FloatingActionButton)findViewById(R.id.btn_twitter);
        btn_instagram = (FloatingActionButton) findViewById(R.id.btn_instagram);
        btn_facebook = (FloatingActionButton)findViewById(R.id.btn_facebook);*/

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
                return true;
            }
        });
        setuserinfo();
    }

    /*@Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_share:
                anim();
                break;
            case R.id.btn_twitter:
                share_twitter();
                break;
            case R.id.btn_instagram:
                share_instagram();
                break;
            case R.id.btn_facebook:
                break;
        }
    }*/
    public void anim(){
        if(isFabOpen){
            btn_twitter.startAnimation(fab_close);
            btn_facebook.startAnimation(fab_close);
            btn_instagram.startAnimation(fab_close);
            btn_twitter.setClickable(false);
            btn_instagram.setClickable(false);
            btn_facebook.setClickable(false);
            isFabOpen = false;
        }
        else{
            btn_facebook.startAnimation(fab_open);
            btn_instagram.startAnimation(fab_open);
            btn_twitter.startAnimation(fab_open);
            btn_facebook.setClickable(true);
            btn_instagram.setClickable(true);
            btn_twitter.setClickable(true);
            isFabOpen = true;
        }
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
                case R.id.btn_gallery:
                    check();
                    break;
                case R.id.btn_delete:
                    delete_picture();
                    break;
            }
        }
    };
    public void share_twitter(){
        /*카카오링크 실패
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("슬봄",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                .setMobileWebUrl("https://developers.kakao.com").build())
                .setDescrption("카카오링크")
                .build())
                .addButton(new ButtonObject("웹에서보기", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()))
                .addButton(new ButtonObject("앱에서보기", LinkObject.newBuilder()
                .setWebUrl("https://developers.kakao.com")
                .setMobileWebUrl("https://developers.kakao.com")
                .setAndroidExecutionParams("key1= value1")
                .setIosExecutionParams("key1=value1")
                .build()))
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });*/
        String strLink = null;
        try{
            strLink = String.format("http://twitter.com/intent/tweet?text=%s",
                    URLEncoder.encode("공유할 텍스트 입력: ", "utf-8"));
        }
        catch (UnsupportedEncodingException e1){
            e1.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
        startActivity(intent);
    }
    public void share_instagram(){
        onRequestPermission();

        if(permissionCheck){
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
            String storage = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "이미지명.png";

            String folderName = "/폴더명/";
            String fullPath = storage + folderName;
            File filePath;

            try{
                filePath = new File(fullPath);
                if(!filePath.isDirectory()){
                    filePath.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(fullPath + fileName);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            Uri uri = Uri.fromFile(new File(fullPath, fileName));
            try{
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, "텍스트는 지원하지않음!");
                share.setPackage("com.instagram.android");
                startActivity(share);
            }
            catch(ActivityNotFoundException e){
                Toast.makeText(this, "인스타그램이 설치되어있지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onRequestPermission(){
        int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_CODE);
        }
        else{
            permissionCheck = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if(permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        if(grantResult == PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "허용했으니 가능", Toast.LENGTH_LONG).show();
                            permissionCheck = true;
                        }
                        else{
                            Toast.makeText(this, "허용하지 않으면 공유 불가능", Toast.LENGTH_LONG).show();
                            permissionCheck = false;
                        }
                    }
                }
                break;
        }
    }

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
    /*<com.google.android.material.floatingactionbutton.FloatingActionButton
            android:layout_alignParentBottom="true"
            android:id="@+id/btn_facebook"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_marginBottom="15dp"
            android:layout_marginLeft="340dp"
            android:background="@drawable/ic_baseline_share_24"
            android:backgroundTint="@color/toolbarcolor"
            android:tint="@color/toolbarcolor"
            android:visibility="invisible"
            app:borderWidth="0dp"
            app:fabSize="normal" />
        <com.google.android.material.floatingactionbutton.FloatingActionButton
            android:layout_alignParentBottom="true"
            android:id="@+id/btn_instagram"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_marginBottom="15dp"
            android:layout_marginLeft="340dp"
            android:background="@drawable/ic_baseline_share_24"
            android:backgroundTint="@color/toolbarcolor"
            android:tint="@color/toolbarcolor"
            android:visibility="invisible"
            app:borderWidth="0dp"
            app:fabSize="normal" />
        <com.google.android.material.floatingactionbutton.FloatingActionButton
            android:layout_alignParentBottom="true"
            android:id="@+id/btn_twitter"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_marginBottom="15dp"
            android:layout_marginLeft="340dp"
            android:background="@drawable/ic_baseline_share_24"
            android:backgroundTint="@color/toolbarcolor"
            android:tint="@color/toolbarcolor"
            android:visibility="invisible"
            app:borderWidth="0dp"
            app:fabSize="normal" />
        <com.google.android.material.floatingactionbutton.FloatingActionButton
            android:layout_alignParentBottom="true"
            android:id="@+id/btn_share"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_marginBottom="15dp"
            android:layout_marginLeft="340dp"
            android:src="@drawable/ic_baseline_share_24"
            android:backgroundTint="@color/toolbarcolor"
            android:tint="@color/toolbarcolor"
            android:visibility="visible"
            app:borderWidth="0dp"
            app:fabSize="normal" />*/
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
                        if(user_level.equals("1")){
                            GeneralUserInfo generalUserInfo = documentSnapshot.toObject(GeneralUserInfo.class);
                            picture[0] = generalUserInfo.getImageurl();
                            name[0] = generalUserInfo.getName();
                            Glide.with(AddFeedActivity.this).load(picture[0]).into(imageView2);
                            user_name.setText(name[0]);
                        }
                        else {
                            //
                            LocalUserInfo localUserInfo = documentSnapshot.toObject(LocalUserInfo.class);
                            picture[0] = localUserInfo.getImageurl();
                            name[0] = localUserInfo.getName();
                            address[0] = localUserInfo.getAddress();
                            Glide.with(AddFeedActivity.this).load(picture[0]).into(imageView2);
                            user_name.setText(name[0]);
                            user_address.setText(address[0]);
                        }
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
                    startToast("게시글이 저장되었습니다.");
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
                            //Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            startToast("게시글이 저장되었습니다.");
                            FeedInfo feedInfo = new FeedInfo(user.getUid(), localurl,  localname, address, phone, String.valueOf(filePath), extratext);
                            uploader(feedInfo, first, second, third);
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
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(LocalUserActivity.class);
                            }
                        }, 1000);
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
                if(cardView.getVisibility() == View.VISIBLE){
                    cardView.setVisibility(View.GONE);
                }
                else{
                    cardView.setVisibility(View.VISIBLE);
                }
                this.getContentResolver().takePersistableUriPermission(filePath, takeFlags);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                new_imageView.setImageBitmap(bitmap);
                new_imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void delete_picture(){
        filePath = null;
        //이미지 삭제시 사라지게 하는법
        new_imageView.setVisibility(View.INVISIBLE);
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
