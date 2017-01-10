package com.henja.liqnot.app;

import android.app.Application;

import bo.NotifierDirector;

/**
 * Created by javier on 10/01/2017.
 */

public class LiqNotApp extends Application {

    private NotifierDirector notifierDirector;

    @Override
    public void onCreate() {
        super.onCreate();
        this.notifierDirector = new NotifierDirector(this.getApplicationContext());
    }

    public NotifierDirector getNotifierDirector(){
        return this.notifierDirector;
    }
}
