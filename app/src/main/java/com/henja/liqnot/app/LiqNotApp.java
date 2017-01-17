package com.henja.liqnot.app;

import android.app.Application;
import android.content.Intent;

import com.henja.liqnot.service.LiqNotService;
import com.henja.liqnot.ws.GetAssetList;

import java.util.Arrays;
import java.util.List;

import bo.Asset;
import bo.NotifierDirector;
import bo.SharedDataCentral;

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
        SharedDataCentral.putAsset(new Asset("1.3.0","BTS",5,"core"));
        SharedDataCentral.putAsset(new Asset("1.3.105","SILVER",4,"SMARTCOIN"));
        SharedDataCentral.putAsset(new Asset("1.3.120","EUR",4,"SMARTCOIN"));
        SharedDataCentral.putAsset(new Asset("1.3.121","USD",4,"SMARTCOIN"));
        SharedDataCentral.putAsset(new Asset("1.3.861","OPEN.BTC",8,"UIA"));
        SharedDataCentral.putAsset(new Asset("1.3.113","CNY",4,"SMARTCOIN"));
        SharedDataCentral.putAsset(new Asset("1.3.562","OBITS",8,"UIA"));
        this.notifierDirector = new NotifierDirector(this.getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), LiqNotService.class);
        startService(intent);

    }

    public NotifierDirector getNotifierDirector(){
        return this.notifierDirector;
    }
}
