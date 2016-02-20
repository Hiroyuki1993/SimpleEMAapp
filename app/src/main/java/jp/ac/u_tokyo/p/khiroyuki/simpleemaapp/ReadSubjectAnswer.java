package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ReadSubjectAnswer extends DatabaseHelper{
    public int errMsg;
    private ArrayList<String[]> output = new ArrayList<>();

    public ReadSubjectAnswer(Context context) {
        super(context);
    }

    public ArrayList<String[]> searchAnswerToArray() {
        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"_id", "QTime", "QType"};
        Cursor trialCS = rdb.query("Trials", cols, null, null, null, null, "QTime ASC", null);
        if(!trialCS.moveToFirst()){
            errMsg = R.string.read_trial_err;
            return null;
        } else {
            do {
                String[] outputLine = {"","",""};
                int trialId = trialCS.getInt(0);
                outputLine[0] = trialCS.getString(1);
                outputLine[1] = trialCS.getString(2);
                output.add(outputLine);
                if(!addAnswersToArray(trialId)){
                    return null;
                }
                String[] emptyLine = {"","",""};
                output.add(emptyLine);
            } while (trialCS.moveToNext());
        }
        trialCS.close();
        return output;
    }

    public Boolean addAnswersToArray(int trialId){
        SQLiteDatabase rdb = getReadableDatabase();
        String[] cols = {"question", "answer", "answeredTime"};
        String[] params = {Integer.toString(trialId)};
        Cursor cs = rdb.query("Answers", cols, "trialId = ?", params, null, null, "answeredTime ASC", null);
        if(!cs.moveToFirst()){
            errMsg = R.string.read_ans_err;
            return false;
        } else {
            do {
                String[] outputLine = {"","",""};
                outputLine[0] = cs.getString(0);
                outputLine[1] = cs.getString(1);
                outputLine[2] = cs.getString(2);
                output.add(outputLine);
            } while (cs.moveToNext());
        }
        cs.close();
        return true;
    }
}
