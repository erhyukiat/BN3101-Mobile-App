package com.example.yukiat.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class RingtonePlayingService extends Service {

    private MediaPlayer media_song;
    private boolean isRunning;
    int startId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // fetch the extra string values
        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone extra is", state);

        // this converts the extra strings from the intent
        // to start ids 0 or 1
        assert state != null ;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        // if else statements

        if(!this.isRunning && startId == 1) { // no music playing, "alarm on" pressed
            Log.e("there is no music", "and you want start");

            // create an instance of the media player
            media_song = MediaPlayer.create(this, R.raw.koreansubwaybell);
            media_song.setLooping(true);
            media_song.start();

            this.isRunning = true;
            this.startId = 0;

            // notification
            // set up the notification service
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            // set up an intent that goes to the MainActivity
            Intent intentMainActivity = new Intent(this.getApplicationContext(), MainActivity.class);
            // set up a pending intent
            PendingIntent pendingIntentMainActivity = PendingIntent
                    .getActivity(this, 0, intentMainActivity, 0);

            // make the notification parameters
            Notification notificationPopup = new Notification.Builder(this)
                    .setContentTitle("It's time to take your meds")
                    .setContentText("Click me!")
                    .setContentIntent(pendingIntentMainActivity)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .build();

            // set up notifcation start command
            notificationManager.notify(0, notificationPopup);


        } else if(this.isRunning && startId == 0) { // music playing, "alarm off" pressed
            Log.e("there is music", "and you want end");

            // stop the ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

        } else if(!this.isRunning && startId == 0) { // music not playing, "alarm off" pressed
            Log.e("there is no music, ", "and you want end");

            this.isRunning = false;
            this.startId = 0;

        } else if (this.isRunning && startId == 1) { // music playing, "alarm on" pressed
            Log.e("there is music, ", "and you want start");

            this.isRunning = true;
            this.startId = 1;

        } else {    // any other possible combinations
            Log.e("else", "somehow you reached this");

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped
        Log.e("onDestroy called", "ta da");

        super.onDestroy();
        this.isRunning = false;
    }
}
