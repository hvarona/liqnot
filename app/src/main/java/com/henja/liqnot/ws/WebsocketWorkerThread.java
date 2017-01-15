package com.henja.liqnot.ws;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.henja.liqnot.app.LiqNotApp;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.IOException;

/**
 *
 * Created by hvarona on 10/01/2017.
 */

public class WebsocketWorkerThread extends Thread {
    private WebSocket mWebSocket;
    private Context context;

    public WebsocketWorkerThread(WebSocketListener webSocketListener,Context context) throws Exception {

        this.context = context;
        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
        //if(hasConnection()) {
            try {
                mWebSocket = factory.createSocket(LiqNotApp.urlsSocketConnection[LiqNotApp.lastServerIndexResponse]); //TODO server list
                mWebSocket.addListener(webSocketListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        /*}else{
            //TODO no hay red
            throw new Exception();
        }*/
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            mWebSocket.connect();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public boolean hasConnection(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
