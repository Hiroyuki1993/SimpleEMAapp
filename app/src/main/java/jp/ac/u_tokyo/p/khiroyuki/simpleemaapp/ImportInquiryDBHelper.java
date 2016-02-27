package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ImportInquiryDBHelper extends DatabaseHelper{
    public int errMsg;

    public ImportInquiryDBHelper(Context context){
        super(context);
    }

    public boolean WriteInq(ReadXMLFile data){
        SQLiteDatabase db = getWritableDatabase();
        if(setRoots(data.returnRoots(), db)){
            if(setQuestions(data.returnQuestions(), db)){
                if(!setItems(data.returnItems(), db)){
                    errMsg = R.string.writing_items;
                }
            } else errMsg = R.string.writing_q;
        } else errMsg = R.string.writing_roots;
        return true;
    }

    public Boolean setRoots(String[] roots, SQLiteDatabase db){
        Boolean dbSuccess = true;
        for (String root:roots){
            ContentValues cv = new ContentValues();
            cv.put("rootName", root);
            if(db.insert("RootItem", null, cv) == -1){
                dbSuccess = false;
                break;
            }
        }
        return dbSuccess;
    }

    public Boolean setQuestions(HashMap[] qs, SQLiteDatabase db){
        SQLiteDatabase rdb = getReadableDatabase();
        Boolean dbSuccess = true;
        for (HashMap q:qs){
            ContentValues cv = new ContentValues();
            Cursor parentCursor = rdb.query("rootItem", new String[]{"_id"}, "rootName = ?", new String[]{q.get("parent").toString()}, null, null, null, null);
            if(parentCursor.moveToFirst()){
                cv.put("parent",parentCursor.getInt(0));
            }
            cv.put("hq", q.get("hq").toString());
            Cursor typeCursor = rdb.query("Types", new String[]{"_id"}, "TType = ?", new String[]{q.get("type").toString()}, null, null, null, null);
            if(typeCursor.moveToFirst()){
                cv.put("QType", typeCursor.getInt(0));
            }
            cv.put("QOrder", Integer.parseInt(q.get("order").toString()));
            cv.put("QDesc", q.get("desc").toString());
            cv.put("QMax", q.get("max").toString());
            cv.put("QMin", q.get("min").toString());
            cv.put("itemId", q.get("itemId").toString());
            if(db.insert("Questions", null, cv) == -1){
                dbSuccess = false;
                break;
            }
            parentCursor.close();
            typeCursor.close();
        }
        return dbSuccess;
    }

    public Boolean setItems(ArrayList<String[]> items, SQLiteDatabase db){
        Boolean dbSuccess = true;
        SQLiteDatabase rdb = getReadableDatabase();
        ContentValues cv = new ContentValues();
        for (String[] item:items){
            Cursor parentCursor = rdb.query("Questions", new String[]{"_id"}, "itemId = ?", new String[]{item[0]}, null, null, null, null);
            if(parentCursor.moveToFirst()){
                cv.put("questionId",parentCursor.getInt(0));
            }
            cv.put("itemName", item[1]);
            if(db.insert("Items", null, cv) == -1){
                dbSuccess = false;
                break;
            }
            parentCursor.close();
        }
        return dbSuccess;
    }

    public void initTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("RootItem", null, null);
        db.delete("Questions", null, null);
        db.delete("Items", null, null);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'RootItem'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'Questions'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'Items'");
    }
}
