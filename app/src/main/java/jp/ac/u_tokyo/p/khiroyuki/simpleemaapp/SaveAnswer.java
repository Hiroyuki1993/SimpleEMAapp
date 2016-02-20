package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveAnswer extends DatabaseHelper {
    public SaveAnswer(Context context){
        super(context);
    }

    public int AddNewTrial(String questionType){
        SQLiteDatabase wdb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("QType", questionType);
        cv.put("QTime", getDateTime("yyyy-MM-dd HH:mm:ss"));
        long rowId = wdb.insert("Trials", null, cv);
        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"_id"};
        String[] params = {String.valueOf(rowId)};
        Cursor cs = rdb.query("Trials", cols, "ROWID=?", params, null, null, null, null);
        cs.moveToFirst();
        int newTrialId = cs.getInt(0);
        cs.close();
        return newTrialId;
    }

    public void SaveTheAnswer(String question, String answer, int trialId){
        SQLiteDatabase wdb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("question", question);
        cv.put("answer", answer);
        cv.put("trialId", trialId);
        cv.put("answeredTime", getDateTime("HH:mm:ss"));
        wdb.insert("Answers", null, cv);
    }

    private String getDateTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                format, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
