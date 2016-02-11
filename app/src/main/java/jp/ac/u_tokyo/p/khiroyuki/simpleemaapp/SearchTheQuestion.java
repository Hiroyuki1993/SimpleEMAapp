package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SearchTheQuestion extends DatabaseHelper{
    public boolean endOfQuestion = false;
    public int errMsg;
    public int qid;
    public String hq;
    public String qType;
    public String desc;
    public String qMax;
    public String qMin;
    public ArrayList<String> items = new ArrayList<>();

    public SearchTheQuestion(Context context){
        super(context);
    }

    public boolean SearchQ(int parentId, int index){
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"_id","hq", "QType", "QDesc", "QMax", "QMin"};
        String[] params = {Integer.toString(parentId)};
        Cursor cs = db.query("Questions", cols, "parent=?", params, null, null, "QOrder ASC", null);

        if(cs.moveToFirst()){
            for (int i=0; i<index;i++){
                if (cs.isLast()){
                    endOfQuestion = true;
                    return false;
                }
                cs.moveToNext();
            }
            qid = cs.getInt(0);
            hq = cs.getString(1);
            qType = getQtypeName(cs.getInt(2));
            if(qType.isEmpty()) return false;
            desc = cs.getString(3);
            qMax = cs.getString(4);
            qMin = cs.getString(5);
        } else {
            errMsg = R.string.empty_questions;
            return false;
        }
        cs.close();
        return true;
    }

    public String getQtypeName(int Tid){
        String qTypeName;
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"TType"};
        String[] params = {Integer.toString(Tid)};
        Cursor cs = db.query("Types", cols, "_id=?", params, null, null, null, null);
        if(cs.moveToFirst()){
            qTypeName = cs.getString(0);
        } else {
            errMsg = R.string.unknown_qType;
            return null;
        }
        cs.close();
        return qTypeName;
    }

    public boolean getQitem(int Qid){
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"itemName"};
        String[] params = {Integer.toString(Qid)};
        Cursor cs = db.query("Items", cols, "questionId=?", params, null, null, null);
        if(cs.moveToFirst()){
            do {
                items.add(cs.getString(0));
            } while (cs.moveToNext());
        } else {
            errMsg = R.string.item_not_found;
            return false;
        }
        cs.close();
        return true;
    }
}
