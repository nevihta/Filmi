package com.rvir.filmi.filmi.priporocila;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rvir.filmi.filmi.filmi.FragmentIskanje;
import com.rvir.filmi.filmi.filmi.FragmentKategorije;
import com.rvir.filmi.filmi.filmi.FragmentPopularni;

public class PriporocilaPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PriporocilaPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
               FragmentApiPriporocila tab1 = new FragmentApiPriporocila();
                return tab1;
            case 1:
                FragmentPrijateljiPriporocila tab2 = new FragmentPrijateljiPriporocila();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
