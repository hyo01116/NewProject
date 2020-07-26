package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RegisterAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public RegisterAdapter(FragmentManager fr, int NumOfTabs){
        super(fr);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        /*switch (position) {
            case 0:
                GeneralUserRegisterActivity generalUserRegisterActivity = new GeneralUserRegisterActivity();
                return generalUserRegisterActivity;
            case 1:
                LocalUserRegisterActivity localUserRegisterActivity = new LocalUserRegisterActivity();
                return localUserRegisterActivity;
        }*/
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
