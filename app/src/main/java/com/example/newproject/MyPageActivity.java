package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyPageActivity extends Fragment {
    private FirebaseUser user;
    private String user_name;

    ImageView imageView2;
    TextView name;

    Button btn_myaccount, btn_myitemlist, btn_mylocation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_mypage, container, false);

        name = view.findViewById(R.id.user_name);
        imageView2 = view.findViewById(R.id.imageView2);

        mydata();        //user email 보여줌

        btn_myaccount = (Button)view.findViewById(R.id.btn_myaccount);
        btn_myitemlist = (Button)view.findViewById(R.id.btn_myitemlist);
        btn_mylocation = (Button)view.findViewById(R.id.btn_mylocation);

        btn_myaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn_myitemlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MyItemListActivity.class);
            }
        });
        btn_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MyLocationActivity.class);
            }
        });
        return view;
    }
    public void mydata(){     //이메일 나타내기
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
                        user_name = generalUserInfo.getName();
                        name.setText(user_name);
                    }
                }
            }
        });
    }
    public void startActivity(Class c){
        Intent intent = new Intent(getContext(), c);
        startActivity(intent);
    }
}
