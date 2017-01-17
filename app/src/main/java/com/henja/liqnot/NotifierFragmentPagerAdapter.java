package com.henja.liqnot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 *
 * Created by javier on 05/01/2017.
 */

class NotifierFragmentPagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private long baseId = 0;

    NotifierFragmentPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NotifierListFragment.newInstance();
            case 1:
                return CurrencyOperatorValueNotifierRuleFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        if( object.getClass().equals(CurrencyOperatorValueNotifierRuleFragment.class))return POSITION_NONE;
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return baseId+position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public void notifyChangeInPosition(int n) {
        baseId += 2 + n;
    }
}
