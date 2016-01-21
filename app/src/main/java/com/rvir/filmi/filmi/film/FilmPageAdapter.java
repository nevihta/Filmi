package com.rvir.filmi.filmi.film;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rvir.filmi.filmi.filmi.FragmentIskanje;
import com.rvir.filmi.filmi.filmi.FragmentKategorije;
import com.rvir.filmi.filmi.filmi.FragmentPopularni;

public class FilmPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public FilmPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
               FragmentOpis tab1 = new FragmentOpis();
                return tab1;
            case 1:
                FragmentKritike tab2 = new FragmentKritike();
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
