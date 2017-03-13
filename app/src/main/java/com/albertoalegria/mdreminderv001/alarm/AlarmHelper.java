package com.albertoalegria.mdreminderv001.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.albertoalegria.mdreminderv001.db.MedDBHelper;
import com.albertoalegria.mdreminderv001.model.Med;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alberto on 11/03/17.
 */

public class AlarmHelper extends WakefulBroadcastReceiver {
    private static final String TAG = "AlarmHelper";
    private static final int ALARM_REQUEST_CODE = 0;

    //Bundle tags
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String RESOURCE = "resource";

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Intent received");

        Intent service = new Intent(context, NotificationHelper.class);

        service.putExtra(ID, intent.getExtras().getInt(ID));
        service.putExtra(TITLE, intent.getExtras().getString(TITLE));
        service.putExtra(BODY, intent.getExtras().getString(BODY));
        service.putExtra(RESOURCE, intent.getExtras().getInt(RESOURCE));

        startWakefulService(context, service);
    }

    public void setAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        MedDBHelper helper = new MedDBHelper(context);
        ArrayList<Med> meds = helper.retrieveAll();


    }

    public void setAlarm(Context context, int hour, int minute, int repetition, int id, String title, String body, int resource) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmHelper.class);
        intent.putExtra(ID, id);
        intent.putExtra(TITLE, title);
        intent.putExtra(BODY, body);
        intent.putExtra(RESOURCE, resource);


        alarmIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), hourToMillis(repetition), alarmIntent);

        Log.d(TAG, "Alarm created at " + calendar.get(Calendar.HOUR_OF_DAY) + ", " + calendar.get(Calendar.MINUTE));
    }

    private int hourToMillis(int hour) {
        return 1000 * 60 * 60 * hour;
    }

}