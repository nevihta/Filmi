package com.rvir.filmi.filmi.filmi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FilmiPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public FilmiPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
               FragmentPopularni tab1 = new FragmentPopularni();
                return tab1;
            case 1:
                FragmentKategorije tab2 = new FragmentKategorije();
                return tab2;
            case 2:
                FragmentIskanje tab3 = new FragmentIskanje();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
