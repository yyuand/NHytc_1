package com.hytc.nhytc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/8/18.
 */
public class LostViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public LostViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public LostViewPageAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }



}
