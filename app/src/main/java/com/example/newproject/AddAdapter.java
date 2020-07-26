package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AddAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public AddAdapter(FragmentManager fr, int NumOfTabs){
        super(fr);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        /*switch (position) {
            case 0:
                AddStuffItemActivity addStuffItemActivity = new AddStuffItemActivity();
                return addStuffItemActivity;
            case 1:
                AddServiceItemActivity addServiceItemActivity = new AddServiceItemActivity();
                return addServiceItemActivity;
        }*/
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
