package com.example.newproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServiceItemListDetailActivity extends AppCompatActivity {

    private String itemkey, secondkey;
    private ImageView imageurl;
    private TextView textname, localname, address, service, datelimit, extratext;

    private String first, second, third;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseDatabase second_database;
    private DatabaseReference second_databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceitemdetail);

        findViewById(R.id.btn_update).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delete).setOnClickListener(onClickListener);
        findViewById(R.id.btn_clear).setOnClickListener(onClickListener);

        itemkey = getIntent().getStringExtra("itemkey");
        secondkey = getIntent().getStringExtra("secondkey");
        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");
        //userid = getIntent().getStringExtra("userid");   //글 올린사람의 id
        imageurl = (ImageView) findViewById(R.id.imageurl);
        textname = (TextView) findViewById(R.id.textname);
        localname = (TextView) findViewById(R.id.localname);
        address = (TextView) findViewById(R.id.address);
        service = (TextView) findViewById(R.id.service);
        datelimit = (TextView) findViewById(R.id.datelimit);
        extratext = (TextView) findViewById(R.id.extratext);
        finduserinfo();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_update:    //변화된게 없다면 고대로 다시 저장
                    update();
                    startToast("게시물이 수정되었습니다.");
                    //startfunction();
                    break;
                case R.id.btn_delete:    //db에서 해당 글 모두 삭제
                    delete();
                    startToast("게시물이 삭제되었습니다.");
                    //startfunction();
                    break;
                case R.id.btn_clear:     //state를 close로 변화 + 리스트에 나타낼때 state가 close인 경우 회색으로 표시
                    itemclear_basicdb();
                    startToast("게시물이 거래완료 되었습니다.");
                    //startfunction();
                    break;
            }
        }
    };

    public void findservice(String first, String second, String third) {
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("service").child(first).child(second).child(third).child(itemkey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemInfo serviceItemInfo = snapshot.getValue(ServiceItemInfo.class);
                /*if(stuffItemInfo.getImageurl() != null) {
                    Glide.with(StuffItemDetailActivity.this).load(stuffItemInfo.getImageurl()).into(imageurl);
                }
                else{
                    Glide.with(StuffItemDetailActivity.this).load(stuffItemInfo.getLocalurl()).into(imageurl);
                }*/
                textname.setText(serviceItemInfo.getTextname());
                localname.setText(serviceItemInfo.getLocalname());
                address.setText(serviceItemInfo.getAddress());
                service.setText(serviceItemInfo.getService());
                datelimit.setText(serviceItemInfo.getDatelimit());
                extratext.setText(serviceItemInfo.getExtratext());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void finduserinfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        UserLocationInfo userLocationInfo = documentSnapshot.toObject(UserLocationInfo.class);
                        first = userLocationInfo.getFirst();
                        second = userLocationInfo.getSecond();
                        third = userLocationInfo.getThird();
                        findservice(first, second, third);
                    }
                }
            }
        });
    }
    public void itemclear_basicdb() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        database.getReference("service").child(first).child(second).child(third).child(itemkey).child("state").setValue("close");
    }

    public void delete() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-write.firebaseio.com/");
        database.getReference(user.getUid()).child("service").child(secondkey).removeValue();

        second_database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        second_database.getReference("service").child(first).child(second).child(third).child(itemkey).removeValue();
    }
    public void update() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String localurl = null;
        String imageurl = null;
        String textname = ((EditText) findViewById(R.id.textname)).getText().toString();
        String localname = ((EditText) findViewById(R.id.localname)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        String service = ((EditText) findViewById(R.id.service)).getText().toString();
        String datelimit = ((EditText) findViewById(R.id.datelimit)).getText().toString();
        String extratext = ((EditText) findViewById(R.id.extratext)).getText().toString();

        ServiceItemInfo serviceItemInfo = new ServiceItemInfo(user.getUid(), localname, address, localurl, textname, service, datelimit, extratext, "open", null);

        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        database.getReference("service").child(first).child(second).child(third).child(itemkey).setValue(serviceItemInfo);
    }
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}