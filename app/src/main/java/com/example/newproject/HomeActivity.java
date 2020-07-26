package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends Fragment {
    Button stuff;
    Button service;

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);
        setHasOptionsMenu(true);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("물품"));
        tabs.addTab(tabs.newTab().setText("봉사활동"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(myPagerAdapter);

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
        return view;
    }
    public void startActivity(Class c){
        Intent intent = new Intent(getContext(), c);
        startActivity(intent);
    }
}
