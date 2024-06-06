package com.example.igenerationmobile.pages;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.example.igenerationmobile.R;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class NotificationPage extends AppCompatActivity {

    private Toolbar toolbar;
    private Switch push_notification;
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        toolbar = findViewById(R.id.toolbar);
        push_notification = findViewById(R.id.push_notification);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        boolean isCheck = sharedPreferences.getBoolean("isCheckNotify", false);

        push_notification.setChecked(isCheck);

        push_notification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setAutoCancel(false)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setContentTitle("Уведомление")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Эксперт оставил комментарий на проекте: Умный зонт"))
                    .setPriority(PRIORITY_HIGH);

            createChannel(notificationManager);
            notificationManager.notify(NOTIFY_ID, builder.build());

            editor.putBoolean("isCheckNotify", isChecked);

            editor.apply();
        });

    }

    private static void createChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}