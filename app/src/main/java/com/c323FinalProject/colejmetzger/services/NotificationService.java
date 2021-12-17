package com.c323FinalProject.colejmetzger.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;

public class NotificationService extends Service {


    double timeLeft;

    public void startNotificationListener() {
        //start's a new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                //fetching notifications from server
                //if there is notifications then call this method
                showNotification();
            }
        }).start();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startNotificationListener();
        startTimer();
    }

    private void startTimer() {
        new CountDownTimer((long) (timeLeft *1000), 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("asdf", "timer tick") ;
            }
            public void onFinish() {
                Log.d("asdf", "finished timer") ;
                showNotification();
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        timeLeft = (double) intent.getExtras().get("time");
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getBaseContext(),"timer_done")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Food Delivered")
                .setContentText("You're food has been delivered.")
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .build();
        notificationManager.notify(0, notification);
    }
}
