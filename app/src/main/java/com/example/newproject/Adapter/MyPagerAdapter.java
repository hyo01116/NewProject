package com.example.newproject.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newproject.Activity.ServiceActivity;
import com.example.newproject.Activity.StuffActivity;

public class MyPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fr, int NumOfTabs){
        super(fr);
        this.mNumOfTabs = NumOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                StuffActivity stuffActivity = new StuffActivity();
                return stuffActivity;
            case 1:
                ServiceActivity serviceActivity = new ServiceActivity();
                return serviceActivity;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
