package com.example.yukiat.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("In AlarmReceiver", "Line 12");

        // fetch extra strings from the intent
        String getString = intent.getExtras().getString("extra");

        Log.e("What is the key?", getString);

        // create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // pass the extra string from MainActivity to the RingtonePlayingService
        service_intent.putExtra("extra", getString);

        // start the ringtone service
        context.startService(service_intent);

    }
}
