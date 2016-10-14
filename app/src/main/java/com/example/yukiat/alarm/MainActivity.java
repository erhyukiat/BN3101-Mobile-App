package com.example.yukiat.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Context context;

    /*
          9am     2pm    7pm
  input  morning  noon  night
    1	   0      0      0
    2	   0      0      1
    3	   1      0      0
    4	   1      0      1
    5	   1      1      0
    6	   1      1      1
    7	   0      1      0
    8	   0      1      1
    */

    static int box1 = 0, box2 = 0, box3 = 0, box4 = 0, input = 3711,
            morningid = 1, afternoonid = 2, nightid = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        Log.e("input is", String.valueOf(input));
        Log.e("morning", String.valueOf(morningAlarm(input)));
        Log.e("afternoon", String.valueOf(afternoonAlarm(input)));
        Log.e("night", String.valueOf(nightAlarm(input)));

        // initialise button
        Button alarm_on = (Button)findViewById(R.id.alarm_on);
        Button stopRingtone = (Button)findViewById(R.id.stopRingtone);
        Button clearAlarmButton = (Button)findViewById(R.id.cancelAlarms);

        // create an onClick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calNow = Calendar.getInstance();
                Calendar calMorning = (Calendar)calNow.clone();
                Calendar calAfternoon = (Calendar)calNow.clone();
                Calendar calNight = (Calendar)calNow.clone();

                if(morningAlarm(input)) {
                    calMorning.set(Calendar.HOUR_OF_DAY, 9);
                    calMorning.set(Calendar.MINUTE, 0);
                    calMorning.set(Calendar.SECOND, 0);
                    calMorning.set(Calendar.MILLISECOND, 0);

                    // if time now is after alarm time, ring 1 day later
                   if (calMorning.compareTo(calNow) <= 0) {
                       //Today set time has passed, count to tomorrow
                       calMorning.add(Calendar.DATE, 1);
                    }

                    setAlarm(calMorning, morningid);
                }
                if (afternoonAlarm(input)) {
                    calAfternoon.set(Calendar.HOUR_OF_DAY, 14);
                    calAfternoon.set(Calendar.MINUTE, 0);
                    calAfternoon.set(Calendar.SECOND, 0);
                    calAfternoon.set(Calendar.MILLISECOND, 0);

                    if (calAfternoon.compareTo(calNow) <= 0) {
                        //Today set time has passed, count to tomorrow
                        calAfternoon.add(Calendar.DATE, 1);
                    }

                    setAlarm(calAfternoon, afternoonid);
                }

                if (nightAlarm(input)) {
                    calNight.set(Calendar.HOUR_OF_DAY, 19);
                    calNight.set(Calendar.MINUTE, 0);
                    calNight.set(Calendar.SECOND, 0);
                    calNight.set(Calendar.MILLISECOND, 0);

                    if (calNight.compareTo(calNow) <= 0) {
                        //Today set time has passed, count to tomorrow
                        calNight.add(Calendar.DATE, 1);
                    }

                    setAlarm(calNight, nightid);
                }
            }

        });

        stopRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRingtone();
            }
        });

        clearAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAlarms();
            }
        });

    }

    private void setAlarm(Calendar targetCal, int id) {
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("extra", "alarm on");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), id, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.e("alarm", "set");
    }

    private void stopRingtone() {
        Log.e("Ringtone", "Stopped");

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("extra", "alarm off");
        sendBroadcast(intent);
    }

    private void clearAlarms() {
        Log.e("Alarms", "cleared" );

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(morningAlarm(input)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), morningid, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
        if(afternoonAlarm(input)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), afternoonid, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
        if(nightAlarm(input)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), nightid, intent, 0);
            alarmManager.cancel(pendingIntent);
        }

    }

    public static boolean morningAlarm(int value) {
        /*
          9am     2pm    7pm
  input  morning  noon  night
    1	   0      0      0
    2	   0      0      1
    3	   1      0      0
    4	   1      0      1
    5	   1      1      0
    6	   1      1      1
    7	   0      1      0
    8	   0      1      1
    */
        box1 = value / 1000;
        box2 = (value / 100) % 10;
        box3 = (value/10) % 10;
        box4 = value % 10;

        return (box1 > 2 && box1 < 7 ) || (box2 > 2 && box2 < 7 ) || (box3 > 2 && box3 < 7 ) ||
                (box4 > 2 && box4 < 7 );
    }

    public boolean afternoonAlarm(int value) {
        /*
          9am     2pm    7pm
  input  morning  noon  night
    1	   0      0      0
    2	   0      0      1
    3	   1      0      0
    4	   1      0      1
    5	   1      1      0
    6	   1      1      1
    7	   0      1      0
    8	   0      1      1
    */
        box1 = value / 1000;
        box2 = (value / 100) % 10;
        box3 = (value/10) % 10;
        box4 = value % 10;

        return (box1 > 4) || (box2 > 4) || (box3 > 4) || (box4 > 4);
    }

    public boolean nightAlarm(int value) {
        /*
          9am     2pm    7pm
  input  morning  noon  night
    1	   0      0      0
    2	   0      0      1
    3	   1      0      0
    4	   1      0      1
    5	   1      1      0
    6	   1      1      1
    7	   0      1      0
    8	   0      1      1
    */
        box1 = value / 1000;
        box2 = (value / 100) % 10;
        box3 = (value/10) % 10;
        box4 = value % 10;

        return (box1%2 == 0) || (box2%2 == 0) || (box3%2 == 0) || (box4%2 == 0);
    }
}
