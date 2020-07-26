package com.example.newproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class GeneralUserRegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generaluserregister);

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
                    generaluserregister();
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
    public void generaluserregister() {
        mAuth = FirebaseAuth.getInstance();
        final String email = ((EditText) findViewById(R.id.email)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();
        final String name= ((EditText) findViewById(R.id.name)).getText().toString();
        final String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
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
                                GeneralUserInfo generalUserInfo = new GeneralUserInfo(email, name, phone, first, second, third);
                                UserLocationInfo userLocationInfo = new UserLocationInfo(first, second, third);
                                userUpload(generalUserInfo, userLocationInfo, user.getUid());
                                //startToast("회원가입이 정상적으로 이뤄졌습니다.");
                            } else {
                                if (task.getException() != null) {
                                    //startToast(task.getException().toString());
                                }
                            }
                        }
                    });
        }
    }
    public void userUpload(GeneralUserInfo generalUserInfo, UserLocationInfo userLocationInfo, String userid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userid).set(generalUserInfo)
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
        databaseReference.child("level").setValue("1");
    }
    public void check(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                //startToast("권한 거부");
            }
        }
        else{
            startActivity(GalleryActivity.class);
        }
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    /*public void startToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }*/

}
