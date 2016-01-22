package com.rvir.filmi.filmi.seznami;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rvir.filmi.filmi.filmi.FragmentIskanje;
import com.rvir.filmi.filmi.filmi.FragmentKategorije;
import com.rvir.filmi.filmi.filmi.FragmentPopularni;

public class SeznamiPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SeznamiPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
               FragmentOgledani tab1 = new FragmentOgledani();
                return tab1;
            case 1:
                FragmentPriljubljeni tab2 = new FragmentPriljubljeni();
                return tab2;
            case 2:
                FragmentWishlist tab3 = new FragmentWishlist();
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
