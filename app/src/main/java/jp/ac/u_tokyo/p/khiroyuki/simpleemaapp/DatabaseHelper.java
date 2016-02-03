package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    static final private String DBNAME = "ema.sqlite";
    static final private int VERSION = 1;

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
        db.execSQL("CREATE TABLE Questions (_id INTEGER PRIMARY KEY AUTOINCREMENT, parent INTEGER NOT NULL, hq TEXT, type INTEGER NOT NULL, order INTEGER NOT NULL, desc TEXT, max INTEGER, min INTEGER)");
        db.execSQL("CREATE TABLE Items (_id INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL, itemName TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Types (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL");

        db.execSQL("INSERT INTO Types VALUES ('radio')");
        db.execSQL("INSERT INTO Types VALUES ('seek')");
        db.execSQL("INSERT INTO Types VALUES ('time')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        db.execSQL("DROP TABLE IF EXISTS RootItem");
        db.execSQL("DROP TABLE IF EXISTS Questions");
        db.execSQL("DROP TABLE IF EXISTS Items");
        onCreate(db);
    }
}
