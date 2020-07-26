package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyItemListAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MyItemListAdapter(FragmentManager fr, int NumOfTabs){
        super(fr);
        this.mNumOfTabs = NumOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                StuffItemListActivity stuffItemListActivity = new StuffItemListActivity();
                return stuffItemListActivity;
            case 1:
                ServiceItemListActivity serviceItemListActivity = new ServiceItemListActivity();
                return serviceItemListActivity;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
