package com.example.igenerationmobile.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.igenerationmobile.R;
import com.example.igenerationmobile.pages.MainPage;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Уведомление")
                .setContentText("Прошла минута");

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = builder.build();

        Intent intentTL = new Intent(context, MainPage.class);

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        nm.notify(1, notification);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,

                intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        am.set(AlarmManager.RTC_WAKEUP, 6000, pendingIntent);

    }
}

