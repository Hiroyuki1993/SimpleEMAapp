package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AlarmListDBHelper extends DatabaseHelper{
    public AlarmListDBHelper(Context context){
        super(context);
    }

    //put alarm list on and return unique id value
    public int writeDB(int hour, int minute){
        if(searchFromHourMinute(hour, minute) != -1){
            return -1;
        }

        SQLiteDatabase wdb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hour", hour);
        cv.put("minute", minute);
        wdb.insert("AlarmList", null, cv);

        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"_id"};
        Cursor cs = rdb.query("AlarmList", cols, null, null, null, null, "_id DESC");
        cs.moveToFirst();
        int newAlarmId = cs.getInt(0);
        cs.close();
        return newAlarmId;
    }

    public ArrayList<String> searchAlarms(){
        ArrayList<String> output = new ArrayList<>();
        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"hour", "minute"};
        Cursor cs = rdb.query("AlarmList", cols, null, null, null, null, null);
        if(!cs.moveToFirst()){
            return null;
        } else {
            do {
                String almTime;
                int hour = cs.getInt(0);
                int minute = cs.getInt(1);
                almTime = timeFormat(hour, minute);
                output.add(almTime);
            } while (cs.moveToNext());
        }
        cs.close();
        return output;
    }

    public int searchFromHourMinute(int hour, int minute){
        int alarmId;
        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"_id"};
        String[] params = {Integer.toString(hour), Integer.toString(minute)};
        Cursor cs = rdb.query("AlarmList", cols, "hour = ? and minute = ?", params, null, null, null, null);
        if(!cs.moveToFirst()){
            alarmId = -1;
        } else {
            alarmId = cs.getInt(0);
        }
        cs.close();
        return alarmId;
    }

    public boolean removeAlarm(int targetId){
        SQLiteDatabase wdb = getWritableDatabase();
        String[] params = {Integer.toString(targetId)};
        int isSuccess = wdb.delete("AlarmList", "_id = ?", params);
        return (isSuccess != 0);
    }

    private String timeFormat(int hour, int minute){
        String txtHour;
        String txtMinute;
        if(hour < 10){
            txtHour = "0"+hour;
        } else {
            txtHour = Integer.toString(hour);
        }
        if(minute < 10){
            txtMinute = "0"+minute;
        } else {
            txtMinute = Integer.toString(minute);
        }
        return txtHour+":"+txtMinute;
    }
}
