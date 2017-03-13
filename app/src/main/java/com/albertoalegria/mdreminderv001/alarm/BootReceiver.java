package com.albertoalegria.mdreminderv001.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by alberto on 12/03/17.
 */

public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmHelper alarmHelper = new AlarmHelper();
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Boot received");
            
        }
    }
}
