package com.henja.liqnot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import bo.NotifierDirector;

/**
 * Created by javier on 05/01/2017.
 */

public class NotifierFragmentPagerAdapter extends FragmentPagerAdapter {

    private NotifierDirector notifierDirector;

    public NotifierFragmentPagerAdapter(FragmentManager fragmentManager, NotifierDirector notifierDirector){
        super(fragmentManager);
        this.notifierDirector = notifierDirector;
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
