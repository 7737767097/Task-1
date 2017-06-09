package com.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * The <code>PagerAdapter</code> serves the fragments when paging.
 *
 * @author mwho
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    public List<Fragment> fragments;

    /**
     * Constructor of the class
     *
     * @param fragments
     */
    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /**
     * This method will be invoked when a page is requested to create
     */
    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    /**
     * Returns the number of pages
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }
}