package com.henja.liqnot.app;

import android.app.Application;

import com.henja.liqnot.ws.GetAssetList;

import java.util.Arrays;
import java.util.List;

import bo.NotifierDirector;

/**
 * Created by javier on 10/01/2017.
 */

public class LiqNotApp extends Application {

    private NotifierDirector notifierDirector;
    public final static List<String> SMARTCOINS = Arrays.asList(new String[] {"CNY","BTC","USD","GOLD","EUR","SILVER",
            "ARS","CAD","GBP","KRW","CHF","JPY","HKD","SGD","AUD","RUB","SBK"});

    @Override
    public void onCreate() {
        super.onCreate();
        GetAssetList.getAllAssets();
        this.notifierDirector = new NotifierDirector(this.getApplicationContext());
    }

    public NotifierDirector getNotifierDirector(){
        return this.notifierDirector;
    }
}
