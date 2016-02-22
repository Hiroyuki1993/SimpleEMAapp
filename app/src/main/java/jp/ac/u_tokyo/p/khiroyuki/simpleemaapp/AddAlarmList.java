package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AddAlarmList extends DatabaseHelper{
    public AddAlarmList(Context context){
        super(context);
    }

    //put alarm list on and return unique id value
    public int writeDB(int hour, int minute){
        SQLiteDatabase wdb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hour", hour);
        cv.put("minute", minute);
        cv.put("status", 1);
        wdb.insert("AlarmList", null, cv);

        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"_id"};
        Cursor cs = rdb.query("AlarmList", cols, null, null, null, null, "_id DESC");
        cs.moveToFirst();
        return cs.getInt(0);
    }

    public ArrayList<String> searchAlarms(){
        ArrayList<String> output = new ArrayList<>();
        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"hour", "minute"};
        String[] params = {"1"};
        Cursor cs = rdb.query("AlarmList", cols, "status = ?", params, null, null, null);
        if(!cs.moveToFirst()){
            return null;
        } else {
            do {
                String almTime;
                int hour = cs.getInt(0);
                int minute = cs.getInt(0);
                almTime = hour + ":" + minute;
                output.add(almTime);
            } while (cs.moveToNext());
        }
        return output;
    }
}
