package com.henja.liqnot.ws;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.IOException;

/**
 * Created by hvarona on 10/01/2017.
 */

public class WebsocketWorkerThread extends Thread {
    private WebSocket mWebSocket;

    public WebsocketWorkerThread(WebSocketListener webSocketListener){
        this(webSocketListener,0);
    }

    public WebsocketWorkerThread(WebSocketListener webSocketListener, int socketIndex){

        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
        try {
            mWebSocket = factory.createSocket("wss://de.blockpay.ch:8089"); //TODO server list
            mWebSocket.addListener(webSocketListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Moves the current Thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            mWebSocket.connect();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
}
