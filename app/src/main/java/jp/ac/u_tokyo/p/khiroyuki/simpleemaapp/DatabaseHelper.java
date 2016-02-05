package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper{
    static final private String DBNAME = "ema.sqlite";
    static final private int VERSION = 1;
    public String errMsg;

    public DatabaseHelper(Context context){
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE RootItem (_id INTEGER PRIMARY KEY AUTOINCREMENT, rootName TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Questions (_id INTEGER PRIMARY KEY AUTOINCREMENT, parent INTEGER NOT NULL, hq TEXT NULL, type INTEGER NOT NULL, order INTEGER NOT NULL, desc TEXT, max TEXT, min TEXT, itemId TEXT)");
        db.execSQL("CREATE TABLE Items (_id INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL, itemName TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Types (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL");

        db.execSQL("INSERT INTO Types (type) VALUES ('radio')");
        db.execSQL("INSERT INTO Types (type) VALUES ('seek')");
        db.execSQL("INSERT INTO Types (type) VALUES ('time')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        db.execSQL("DROP TABLE IF EXISTS RootItem");
        db.execSQL("DROP TABLE IF EXISTS Questions");
        db.execSQL("DROP TABLE IF EXISTS Items");
        db.execSQL("DROP TABLE IF EXISTS Types");
        onCreate(db);
    }

    public boolean WriteInq(ReadXMLFile data){
        SQLiteDatabase db = getWritableDatabase();
        if(db.insert("RootItem", null, setRoots(data.returnRoots())) != -1){
            if(db.insert("Questions", null, setQuestions(data.returnQuestions())) != -1){
                db.insert("Items", null, setItems(data.returnItems()));
            }
        }
        return true;
    }

    public ContentValues setRoots(String[] roots){
        ContentValues cv = new ContentValues();
        for (String root:roots){
            cv.put("rootName", root);
        }
        return cv;
    }

    public ContentValues setQuestions(HashMap[] qs){
        SQLiteDatabase rdb = getReadableDatabase();
        ContentValues cv = new ContentValues();
        for (HashMap q:qs){
            Cursor parentCursor = rdb.query("rootName", new String[]{"_id"}, "rootName", new String[]{q.get("parent").toString()}, null, null, null, null);
            if(parentCursor.moveToFirst()){
                cv.put("parent",parentCursor.getInt(0));
            }
            cv.put("hq", q.get("hq").toString());
            Cursor typeCursor = rdb.query("Types", new String[]{"_id"}, "type", new String[]{q.get("type").toString()}, null, null, null, null);
            if(typeCursor.moveToFirst()){
                cv.put("type",parentCursor.getInt(0));
            }
            cv.put("order", Integer.parseInt(q.get("order").toString()));
            cv.put("desc", q.get("desc").toString());
            cv.put("max", q.get("max").toString());
            cv.put("min", q.get("min").toString());
            cv.put("itemId", q.get("id").toString());
        }
        return cv;
    }

    public ContentValues setItems(ArrayList<String[]> items){
        SQLiteDatabase rdb = getReadableDatabase();
        ContentValues cv = new ContentValues();
        for (String[] item:items){
            Cursor parentCursor = rdb.query("Questions", new String[]{"_id"}, "itemId", new String[]{item[0]}, null, null, null, null);
            if(parentCursor.moveToFirst()){
                cv.put("questionId",parentCursor.getInt(0));
            }
            cv.put("itemName", item[1]);
        }
        return cv;
    }
}
