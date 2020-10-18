package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.Adapter.ServiceNotiItemAdapter;
import com.example.newproject.Class.GeneralUserInfo;
import com.example.newproject.Interface.OnServiceNotiItemClickListener;
import com.example.newproject.Interface.OnStuffNotiItemClickListener;
import com.example.newproject.Interface.OnServiceItemClickListener;
import com.example.newproject.R;
import com.example.newproject.Adapter.ServiceItemAdapter;
import com.example.newproject.Class.ServiceItemInfo;
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

import java.util.ArrayList;

public class ServiceActivity extends Fragment implements ServiceItemAdapter.OnItemClickListener {
    //객체를 넘기고 싶은데, 클릭 시 페이지 이동하는 인터페이스를 상속받게 구현했다
    //페이지 이동하는 인터페이스 상속받았기 떄문에 클릭시 메소드 구현 (추상메소드 반드시 구현)
    //그래서 다중상속을 사용할 수 없어서 intent로 아이템 키 값을 넘기고 다시 데이터베이스에 접근해서 아이템을 찾아오는 방식으로 세부사항 구현
    private RecyclerView recyclerView;
    private ServiceItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView_noti;
    private ServiceNotiItemAdapter adapter_noti;
    private RecyclerView.LayoutManager layoutManager_noti;

    ArrayList<ServiceItemInfo> arrayList = new ArrayList<ServiceItemInfo>();
    ArrayList<ServiceItemInfo> arrayList_noti = new ArrayList<ServiceItemInfo>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String first, second, third, name;

    TextView et_phone;

    Button btn_med, btn_emer, btn_loc, btn_etc;
    /*
    <LinearLayout
                android:layout_gravity="center"
                android:layout_margin="10dp"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">
                <Button
                    android:id="@+id/btn_med"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="보건의료"
                    android:textColor="@color/colorPrimary"
                    android:backgroundTint="@color/toolbarcolor"/>
                <Button
                    android:id="@+id/btn_emer"
                    android:layout_marginLeft="10dp"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="재해/재난"
                    android:textColor="@color/colorPrimary"
                    android:backgroundTint="@color/toolbarcolor"/>
                <Button
                    android:id="@+id/btn_loc"
                    android:layout_marginLeft="10dp"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="농/어촌"
                    android:textColor="@color/colorPrimary"
                    android:backgroundTint="@color/toolbarcolor"/>
                <Button
                    android:id="@+id/btn_etc"
                    android:layout_marginLeft="10dp"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="기타"
                    android:textColor="@color/colorPrimary"
                    android:backgroundTint="@color/toolbarcolor"/>
            </LinearLayout>
            */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_service, container, false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recycler_line));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView_noti = (RecyclerView) view.findViewById(R.id.recyclerView_noti);
        recyclerView_noti.setHasFixedSize(true);
        layoutManager_noti = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView_noti.setLayoutManager(layoutManager_noti);


        /*btn_med = (Button) view.findViewById(R.id.btn_med);
        btn_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("1");
                System.out.println("med");
            }
        });
        btn_emer = (Button) view.findViewById(R.id.btn_emer);
        btn_emer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("2");
                System.out.println("emer");
            }
        });
        btn_loc = (Button) view.findViewById(R.id.btn_loc);
        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("3");
                System.out.println("loc");
            }
        });
        btn_etc = (Button) view.findViewById(R.id.btn_etc);
        btn_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("0");
                System.out.println("etc");
            }
        });*/
        findLocationinfo("-1");

        return view;
    }
    public void findLocationinfo(String type) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        final DocumentReference documentReference;
        documentReference = db.collection("Users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        //userlocationinfo 객체를 생성해서 받아온 documentsnapshot에서 객체의 형태로 저장
                        //그다음 get메소드로 사용자의 위치 정보 받아옴
                        GeneralUserInfo generalUserInfo = documentSnapshot.toObject(GeneralUserInfo.class);
                        first = generalUserInfo.getFirst();
                        second = generalUserInfo.getSecond();
                        third = generalUserInfo.getThird();
                        name = generalUserInfo.getName();
                        if(type.equals("-1")){
                            findnoti(first, second, third);
                        }
                        else {
                            findservice(type, first, second, third, name);
                        }
                    }
                }
            }
        });
    }
    public void findnoti(final String first, final String second, final String third) {
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("service").child(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            //리스너를 사용해서 해당시점의 datasnapshot를 받아옴
            // ondatachange 콜백함수를 사용해서 이벤트가 발생하면 다른 메소드를 호출해서 알려줌 -> 데이터 존재하면 화면에 나타냄
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ServiceItemInfo serviceItemInfo = dataSnapshot.getValue(ServiceItemInfo.class);
                    //getvalue를 통해 객체에 받아온 정보를 넣고 open 이라면 arraylist에 대입
                    if (serviceItemInfo.getState().equals("open")) {
                        if (serviceItemInfo.getNoti().equals("1")) {
                            arrayList_noti.add(0, serviceItemInfo);
                        } else {
                            arrayList.add(0, serviceItemInfo);
                        }
                    }
                }
                adapter_noti = new ServiceNotiItemAdapter(arrayList_noti, getContext());
                recyclerView_noti.setAdapter(adapter_noti);
                adapter_noti.setOnItemClickListener(new OnServiceNotiItemClickListener() {
                    @Override
                    public void onItemClick(ServiceNotiItemAdapter.ServiceNotiItemViewHolder holder, View view, int position) {
                        ServiceNotiItemAdapter.ServiceNotiItemViewHolder viewHolder = (ServiceNotiItemAdapter.ServiceNotiItemViewHolder) recyclerView_noti.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), ServiceItemDetailActivity.class);
                        intent.putExtra("parcel", arrayList_noti.get(position));
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
                adapter = new ServiceItemAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnServiceItemClickListener() {
                    @Override
                    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder holder, View view, int position) {
                        ServiceItemAdapter.ServiceItemViewHolder viewHolder = (ServiceItemAdapter.ServiceItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), ServiceItemDetailActivity.class);
                        intent.putExtra("parcel", arrayList.get(position));
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //사용자가 데이터를 읽을 권한이 없는 경우
            }
        });
    }
    public void findservice(final String type, final String first, final String second, final String third, final String name){
        ArrayList<ServiceItemInfo> arrayList_type = new ArrayList<ServiceItemInfo>();
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("service").child(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //특정 시점에 동작하게 하는 함수
                //비동기 해결하기 위해 구현하는데 그 이유는 다른 일 하다가 데이터가 준비되면 콜백함수내의 작업들을 실행하라고
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ServiceItemInfo serviceItemInfo = dataSnapshot.getValue(ServiceItemInfo.class);
                    if(serviceItemInfo.getType_num().equals(type) && serviceItemInfo.getState().equals("open")){
                        if(serviceItemInfo.getNoti().equals("0")){
                            arrayList_type.add(serviceItemInfo);
                        }
                    }
                }
                recyclerView.removeAllViewsInLayout();
                adapter = new ServiceItemAdapter(arrayList_type, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnServiceItemClickListener() {
                    @Override
                    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder holder, View view, int position) {
                        ServiceItemAdapter.ServiceItemViewHolder viewHolder = (ServiceItemAdapter.ServiceItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), ServiceItemDetailActivity.class);
                        intent.putExtra("parcel", arrayList_type.get(position));
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //사용자가 데이터를 읽을 권한이 없는 경우
            }
        });
    }
    public void startActivity(Class c){
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onItemClick(ServiceItemAdapter.ServiceItemViewHolder itemViewHolder, View v, int position) {
    }
}
