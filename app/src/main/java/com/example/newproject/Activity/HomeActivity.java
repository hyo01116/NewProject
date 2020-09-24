package com.example.newproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.newproject.Adapter.MyPagerAdapter;
import com.example.newproject.Class.LocalUserInfo;
import com.example.newproject.FindMapActivity;
import com.example.newproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends Fragment {
    private FirebaseUser user;
    Toolbar toolbar;
    TextView toolbar_title;

    private String first, second, third;

    //봉사와 기부를 두개의 탭으로 나눠서 각각의 탭 클릭시 해당 페이지가 실행되도록 함
    //프래그먼트 내에서 화면 전환이 일어나게 하기 위해 viewpager사용
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);
        setHasOptionsMenu(true);

        final int[] ICONS = new int[]{
                R.drawable.ic_baseline_card_giftcard_24,
                R.drawable.ic_baseline_emoji_people_24,
        };

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("물품").setIcon(ICONS[0]));
        tabs.addTab(tabs.newTab().setText("봉사활동").setIcon(ICONS[1]));

        tabs.setTabGravity(tabs.GRAVITY_FILL);
        //tab 설정

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        //tab의 수만큼 mypageradapter(getchildfragmentmanager(), tabs.getTabcount());
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(myPagerAdapter);

        toolbar = (Toolbar)view.findViewById(R.id.top_toolbar);
        toolbar_title = (TextView)view.findViewById(R.id.toolbar_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.findmap:

                        startActivity(FindMapActivity.class);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        finduserinfo();
        return view;
    }
    public void finduserinfo(){
        //사용자의 위치 정보를 가져와서 툴바에 현재 위치를 표시해줌
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
                        first = localUserInfo.getFirst();
                        second = localUserInfo.getSecond();
                        third = localUserInfo.getThird();
                        toolbar_title.setText(third);
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
