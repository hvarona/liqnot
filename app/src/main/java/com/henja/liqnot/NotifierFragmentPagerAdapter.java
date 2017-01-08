package com.henja.liqnot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by javier on 05/01/2017.
 */

public class NotifierFragmentPagerAdapter extends FragmentPagerAdapter {

    public NotifierFragmentPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NotifierListFragment();
            case 1:
                return new CurrencyOperatorValueNotifierRuleFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
