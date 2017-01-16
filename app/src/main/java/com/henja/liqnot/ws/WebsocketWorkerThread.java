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

    public WebsocketWorkerThread(WebSocketListener webSocketListener) throws ConnectionException {

        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
        try {
            mWebSocket = factory.createSocket(LiqNotApp.urlsSocketConnection[LiqNotApp.lastServerIndexResponse]); //TODO server list
            mWebSocket.addListener(webSocketListener);
        } catch (Exception e) {
            throw new ConnectionException(e.getMessage(),e.getCause());
        }
    }

    @Override
    public void run() {
        System.out.println("Connecting");
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            mWebSocket.connect();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
}
