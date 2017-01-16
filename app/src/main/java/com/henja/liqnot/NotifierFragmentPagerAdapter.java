package com.henja.liqnot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 * Created by javier on 05/01/2017.
 */

class NotifierFragmentPagerAdapter extends FragmentPagerAdapter {

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
    public int getCount() {
        return 2;
    }
}
