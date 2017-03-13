package com.albertoalegria.mdreminderv001.alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by alberto on 11/03/17.
 */

public class NotificationHelper extends IntentService {
    private static final String TAG = "NotificationHelper";

    public NotificationHelper() {
        super(TAG);
        Log.d(TAG, "Inside constructor");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Handling intent");
        if (intent != null && intent.getExtras() != null) {
            Log.d(TAG, "Creating notification");
            createNotification(intent.getExtras().getInt(AlarmHelper.ID), intent.getExtras().getString(AlarmHelper.TITLE),
                    intent.getExtras().getString(AlarmHelper.BODY), intent.getExtras().getInt(AlarmHelper.RESOURCE));
        } else {
            Log.d(TAG, "Error: Intent is null or empty");
        }
    }

    public void createNotification(int id, String title, String body, int resource) {
        Log.d(TAG, "Showing notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(resource);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
    }

    public void cancelNotification(int id) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(id);
    }
}
