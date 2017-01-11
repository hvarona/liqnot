package com.henja.liqnot.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.henja.liqnot.app.LiqNotApp;

import bo.NotifierDirector;

/**
 * Created by javier on 10/01/2017.
 */

public class LiqNotService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private NotifierDirector notifierDirector;
    private boolean keepFillingNotifierData;
    private Thread FillNotifiersDataThread;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }

    public void fillNotifiersData(){
        this.keepFillingNotifierData = true;

        while(this.keepFillingNotifierData){
            //this.notifierDirector.execute();
            try{
                Log.i("LiqNotService.FNDThread","Filling notifiers data");
                notifierDirector.execute();
                Thread.sleep(60000);//Sleep for 1 minute
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void onCreate() {
        this.notifierDirector = ((LiqNotApp)getApplication()).getNotifierDirector();


        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        /*HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "LiqNot Service Starting!", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        /*Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);*/


        //The thread to fillNotifiersData is created
        if (FillNotifiersDataThread == null) {
            FillNotifiersDataThread = new Thread() {
                public void run() {
                    fillNotifiersData();
                }
            };
            FillNotifiersDataThread.start();
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "LiqNot Service Stopped.", Toast.LENGTH_SHORT).show();
    }
}
