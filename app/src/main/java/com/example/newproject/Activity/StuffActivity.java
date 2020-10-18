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

import com.example.newproject.Adapter.StuffNotiItemAdapter;
import com.example.newproject.Class.GeneralUserInfo;
import com.example.newproject.Class.StuffItemInfo;     //stuffiteminfo 클래스를 import 해와서 사용
import com.example.newproject.Interface.OnStuffNotiItemClickListener;
import com.example.newproject.Interface.OnStuffItemClickListener;
import com.example.newproject.R;
import com.example.newproject.Adapter.StuffItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
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

public class StuffActivity extends Fragment implements StuffItemAdapter.OnItemClickListener {

    private FirebaseAnalytics firebaseAnalytics;
    private RecyclerView recyclerView;
    private StuffItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView_noti;
    private StuffNotiItemAdapter adapter_noti;
    private RecyclerView.LayoutManager layoutManager_noti;

    ArrayList<StuffItemInfo> arrayList = new ArrayList<StuffItemInfo>();
    ArrayList<StuffItemInfo> arrayList_noti = new ArrayList<StuffItemInfo>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String first, second, third, name, user_level;

    TextView et_phone;

    Button btn_food, btn_wear, btn_item, btn_etc;
    /*<Button
                android:id="@+id/btn_food"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="식품"
                android:textColor="@color/colorPrimary"
                android:backgroundTint="@color/toolbarcolor"/>
            <Button
                android:id="@+id/btn_wear"
                android:layout_marginLeft="10dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="의류"
                android:textColor="@color/colorPrimary"
                android:backgroundTint="@color/toolbarcolor"/>
            <Button
                android:id="@+id/btn_item"
                android:layout_marginLeft="10dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="생활용품"
                android:textColor="@color/colorPrimary"
                android:backgroundTint="@color/toolbarcolor"/>
            <Button
                android:id="@+id/btn_etc"
                android:layout_marginLeft="10dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="기타"
                android:textColor="@color/colorPrimary"
                android:backgroundTint="@color/toolbarcolor"/>*/

    private FloatingActionButton btn_data;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_stuff, container, false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recycler_line));

        DividerItemDecoration v_dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL);
        v_dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_line));

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        user_level = "1";

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView_noti = (RecyclerView) view.findViewById(R.id.recyclerView_noti);
        recyclerView_noti.setHasFixedSize(true);
        layoutManager_noti = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView_noti.setLayoutManager(layoutManager_noti);

        /*btn_food = (Button) view.findViewById(R.id.btn_food);
        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("1");
                Bundle stuff_food = new Bundle();
                stuff_food.putString("user_level", user_level);
                stuff_food.putString("user_loc", third);
                firebaseAnalytics.logEvent("stuff_food", stuff_food);
            }
        });
        btn_wear = (Button) view.findViewById(R.id.btn_wear);
        btn_wear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("2");
                Bundle stuff_wear = new Bundle();
                stuff_wear.putString("user_level", user_level);
                stuff_wear.putString("user_loc", third);
                firebaseAnalytics.logEvent("stuff_wear", stuff_wear);
            }
        });
        btn_item = (Button) view.findViewById(R.id.btn_item);
        btn_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("3");
                Bundle stuff_item = new Bundle();
                stuff_item.putString("user_level", user_level);
                stuff_item.putString("user_loc", third);
                firebaseAnalytics.logEvent("stuff_item", stuff_item);
            }
        });
        btn_etc = (Button) view.findViewById(R.id.btn_etc);
        btn_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocationinfo("0");
                Bundle stuff_etc = new Bundle();
                stuff_etc.putString("user_level", user_level);
                stuff_etc.putString("user_loc", third);
                firebaseAnalytics.logEvent("stuff_etc", stuff_etc);
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
                        GeneralUserInfo generalUserInfo = documentSnapshot.toObject(GeneralUserInfo.class);
                        first = generalUserInfo.getFirst();
                        second = generalUserInfo.getSecond();
                        third = generalUserInfo.getThird();
                        name = generalUserInfo.getName();
                        if(type.equals("-1")){
                            findnoti(first, second, third);
                        }
                        else {
                            findstuff(type, first, second, third, name);
                        }
                    }
                }
            }
        });
    }
    public void findnoti(final String first, final String second, final String third){
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("stuff").child(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            //리스너를 사용해서 해당시점의 datasnapshot를 받아옴
            // ondatachange 콜백함수를 사용해서 이벤트가 발생하면 다른 메소드를 호출해서 알려줌 -> 데이터 존재하면 화면에 나타냄
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StuffItemInfo stuffItemInfo = dataSnapshot.getValue(StuffItemInfo.class);
                    //getvalue를 통해 객체에 받아온 정보를 넣고 open 이라면 arraylist에 대입
                    //거꾸로 나오게 해야하는데ㅠㅠ
                    if (stuffItemInfo.getState().equals("open")) {
                        if (stuffItemInfo.getNoti().equals("1")) {
                            arrayList_noti.add(0, stuffItemInfo);
                        } else {
                            arrayList.add(0, stuffItemInfo);
                        }
                    }
                }
                adapter_noti = new StuffNotiItemAdapter(arrayList_noti, getContext());
                recyclerView_noti.setAdapter(adapter_noti);
                adapter_noti.setOnItemClickListener(new OnStuffNotiItemClickListener() {
                    @Override
                    public void onItemClick(StuffNotiItemAdapter.StuffNotiItemViewHolder holder, View view, int position) {
                        StuffNotiItemAdapter.StuffNotiItemViewHolder viewHolder = (StuffNotiItemAdapter.StuffNotiItemViewHolder) recyclerView_noti.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), StuffItemDetailActivity.class);
                        intent.putExtra("Serialize", arrayList_noti.get(position));
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
                adapter = new StuffItemAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnStuffItemClickListener() {
                    @Override
                    public void onItemClick(StuffItemAdapter.StuffItemViewHolder holder, View view, int position) {
                        if(arrayList.get(position).getType_num().equals("0")) {    //돈 기부일경우 다르게 나타나도록
                            Intent intent = new Intent(getContext(), StuffMoneyItemDetailActivity.class);
                            intent.putExtra("Serialize", arrayList.get(position));
                            intent.putExtra("name", name);
                            startActivity(intent);
                        }
                        else{
                            StuffItemAdapter.StuffItemViewHolder viewHolder = (StuffItemAdapter.StuffItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                            Intent intent = new Intent(getContext(), StuffItemDetailActivity.class);
                            //(class에 serializable을 상속시켜놓음, stuffiteminfo를 직렬화하여 intent로 다음 액티비티에 넘김)
                            //원래는 intent로 넘겼지만, 객체 통째로 넘김
                            intent.putExtra("Serialize", arrayList.get(position));
                            intent.putExtra("name", name);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //사용자가 데이터를 읽을 권한이 없는 경우
            }
        });
    }
    public void findstuff(final String type, final String first, final String second, final String third, final String name){
        ArrayList<StuffItemInfo> arrayList_type = new ArrayList<StuffItemInfo>();
        database  = FirebaseDatabase.getInstance("https://newproject-ab6cb-base.firebaseio.com/");
        databaseReference = database.getReference("stuff").child(first).child(second).child(third);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            //리스너를 사용해서 해당시점의 datasnapshot를 받아옴
            // ondatachange 콜백함수를 사용해서 이벤트가 발생하면 다른 메소드를 호출해서 알려줌 -> 데이터 존재하면 화면에 나타냄
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    StuffItemInfo stuffItemInfo = dataSnapshot.getValue(StuffItemInfo.class);
                    //getvalue를 통해 객체에 받아온 정보를 넣고 open 이라면 arraylist에 대입
                    if(stuffItemInfo.getType_num().equals(type) && stuffItemInfo.getNoti().equals("0")){
                        if(stuffItemInfo.getState().equals("open")) {
                            arrayList_type.add(stuffItemInfo);
                        }
                    }
                }
                recyclerView.removeAllViewsInLayout();
                adapter = new StuffItemAdapter(arrayList_type, getContext());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnStuffItemClickListener() {
                    @Override
                    public void onItemClick(StuffItemAdapter.StuffItemViewHolder holder, View view, int position) {
                        StuffItemAdapter.StuffItemViewHolder viewHolder = (StuffItemAdapter.StuffItemViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                        Intent intent = new Intent(getContext(), StuffItemDetailActivity.class);
                        //(class에 serializable을 상속시켜놓음, stuffiteminfo를 직렬화하여 intent로 다음 액티비티에 넘김)
                        //원래는 intent로 넘겼지만, 객체 통째로 넘김
                        intent.putExtra("Serialize", arrayList_type.get(position));
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
    public void onItemClick(StuffItemAdapter.StuffItemViewHolder itemViewHolder, View v, int position) {
    }
}
