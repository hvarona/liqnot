package com.henja.liqnot.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.henja.liqnot.R;
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
                Thread.sleep(60000);//Sleep for 1 minute //TODO Configurable time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void onCreate() {
        this.notifierDirector = ((LiqNotApp)getApplication()).getNotifierDirector();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, getResources().getString(R.string.liqnot_service_starting), Toast.LENGTH_SHORT).show();
        //The thread to fillNotifiersData is created
        NotificationManager NM = (NotificationManager)
                getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext());
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher));
        notificationBuilder.setContentTitle("LiqNot Service");
        notificationBuilder.setContentText("LiqNot Service is running");
//        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 2 , intent, 0);
//        notificationBuilder.setContentIntent(pIntent);
        Notification notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        NM.notify(2, notification);

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
        //Toast.makeText(this, getResources().getString(R.string.liqnot_service_stopped), Toast.LENGTH_SHORT).show();
        System.out.println("Destroying service");
    }
}
