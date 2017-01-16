package com.henja.liqnot;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * Created by javier on 05/01/2017.
 */

public class NotifierMainViewPager extends ViewPager {

    boolean swipeByTouchEnabled;

    public NotifierMainViewPager(Context context) {
        super(context);
        this.setSwipeByTouchEnabled(false);
    }

    public NotifierMainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.swipeByTouchEnabled && super.onTouchEvent(event);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.swipeByTouchEnabled && super.onInterceptTouchEvent(event);

    }

    public void setSwipeByTouchEnabled(boolean enabled) {
        this.swipeByTouchEnabled = enabled;
    }
}
