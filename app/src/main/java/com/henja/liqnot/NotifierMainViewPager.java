package com.henja.liqnot;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
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
        if (this.swipeByTouchEnabled) {
            return super.onTouchEvent(event);
        }

        return false; //To prevent the swipe by touch
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeByTouchEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false; //To prevent the swipe by touch
    }

    public void setSwipeByTouchEnabled(boolean enabled) {
        this.swipeByTouchEnabled = enabled;
    }
}
