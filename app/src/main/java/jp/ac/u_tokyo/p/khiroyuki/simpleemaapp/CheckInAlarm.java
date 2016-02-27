package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class CheckInAlarm {
    public CheckInAlarm(){

    }

    public boolean checkInAlarm(Context context, int hour, int minute){
        AlarmListDBHelper aList = new AlarmListDBHelper(context);
        int id = aList.writeDB(hour, minute);
        if(id == -1){
            return false;
        }
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("requestCode", id);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = convertUTC(hour, minute);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        return true;
    }

    public boolean removeAlarm(Context context, String timeText){
        int[] time = convertTimeTextToInt(timeText);
        AlarmListDBHelper aListHelper = new AlarmListDBHelper(context);
        int targetId = aListHelper.searchFromHourMinute(time[0], time[1]);
        boolean isSuccess = aListHelper.removeAlarm(targetId);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, targetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
        return isSuccess;
    }

    private Calendar convertUTC(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    private int[] convertTimeTextToInt(String timeText){
        //int[0] is hour, int[1] is minute
        int[] time = new int[2];
        String[] timeTextSplit = timeText.split(":", 0);
        time[0] = Integer.parseInt(timeTextSplit[0]);
        time[1] = Integer.parseInt(timeTextSplit[1]);
        return time;
    }
}
