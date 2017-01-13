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

    public static String urlsSocketConnection[] =
            {

                    "wss://de.blockpay.ch:8089",                // German node
                    "wss://fr.blockpay.ch:8089",               // France node
                    "wss://bitshares.openledger.info/ws",      // Openledger node
                    "wss://bit.btsabc.org/ws",
                    "wss://bts.transwiser.com/ws",
                    "wss://freedom.bts123.cc:15138",
                    "wss://okbtc.org:8089/ws",
                    "wss://ratebts.com:8089",
                    "wss://webber.tech:8089/ws",
                    "wss://bitshares.dacplay.org:8089/ws"
            };
    public static int lastServerIndexResponse;

    @Override
    public void onCreate() {
        super.onCreate();
        lastServerIndexResponse = 0;
        this.notifierDirector = new NotifierDirector(this.getApplicationContext());
    }

    public NotifierDirector getNotifierDirector(){
        return this.notifierDirector;
    }
}
