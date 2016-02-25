package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver{
    Context context;
    @Override
    public void onReceive(Context context, Intent intent){
        this.context = context;
        Intent intent_branch = new Intent(context, Branching.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_branch, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(context.getResources().getString(R.string.notification_ticker))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getResources().getString(R.string.notification_ticker))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.cancelAll();
        notificationManager.notify(R.string.app_name, notification);
    }
}
