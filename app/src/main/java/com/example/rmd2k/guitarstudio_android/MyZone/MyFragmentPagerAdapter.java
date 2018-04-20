package com.example.rmd2k.guitarstudio_android.MyZone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by rmd2k on 2018/3/17.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;
    String[] titles;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragmentList = fragments;
        this.titles = titles;
    }

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
