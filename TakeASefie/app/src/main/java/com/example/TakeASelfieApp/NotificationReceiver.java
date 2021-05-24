package com.example.TakeASelfieApp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 1;
    Context context;
    private String mChannelID;
    private int mNotificationCount=0;
    private static long timeCount;
    private final CharSequence tickerText = "It's Time";
    private final CharSequence Title = "Reminder";
    private final CharSequence Text = "Lets take a selfie";
    public NotificationReceiver(Context context){
        this.context = context;
    }
    public NotificationReceiver(){
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        notificationMessege(context);
        scheduleNotification(context,timeCount);
    }
    public void createNotificationChannel(Context context){
        NotificationManager mNotificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mChannelID = context.getPackageName() + ".channel_01";
        String description =context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        CharSequence name = context.getString(R.string.channel_name);
        NotificationChannel mChannel = new NotificationChannel(mChannelID, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        Uri mSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mChannel.setSound(mSoundURI, (new AudioAttributes.Builder())
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build());

        mNotificationManager.createNotificationChannel(mChannel);
    }
    public void notificationMessege(Context context){
        // Define action Intent
        Log.e("Mess",+System.currentTimeMillis()+"");
        Intent mNotificationIntent = new Intent(context.getApplicationContext(),
                Subnotification.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent mContentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,
                mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Define the Notification's expanded message and Intent:

        android.app.Notification.Builder notificationBuilder = new android.app.Notification.Builder(
                context.getApplicationContext(), mChannelID)
                .setTicker(tickerText)
                .setSmallIcon(R.drawable.instagram)
                .setAutoCancel(true)
                .setContentTitle(Title)
                .setContentText(Text)
                .setContentIntent(mContentIntent);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());

    }
    public static void scheduleNotification(Context context, long time) {
        timeCount = time;
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 43, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Schdedule notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+timeCount, pending);
    }
    public static void cancelNotification(Context context) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 43, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Cancel notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pending);
    }
}
