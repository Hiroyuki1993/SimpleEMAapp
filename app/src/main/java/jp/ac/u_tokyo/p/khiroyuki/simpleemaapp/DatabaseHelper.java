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
        db.execSQL("CREATE TABLE Questions (_id INTEGER PRIMARY KEY AUTOINCREMENT, parent INTEGER NOT NULL, hq TEXT NULL, QType INTEGER NOT NULL, QOrder INTEGER NOT NULL, QDesc TEXT, QMax TEXT, QMin TEXT, itemId INTEGER)");
        db.execSQL("CREATE TABLE Items (_id INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL, itemName TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Types (_id INTEGER PRIMARY KEY AUTOINCREMENT, TType TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Trials (_id INTEGER PRIMARY KEY AUTOINCREMENT, QType TEXT, QTime DATETIME)");
        db.execSQL("CREATE TABLE Answers (_id INTEGER PRIMARY KEY AUTOINCREMENT, trialId INTEGER NOT NULL, question TEXT, answer TEXT, answeredTime DATETIME)");
        db.execSQL("CREATE TABLE AlarmList (_id INTEGER PRIMARY KEY AUTOINCREMENT, hour INTEGER, minute INTEGER)");
        db.execSQL("INSERT INTO Types (TType) VALUES ('radio')");
        db.execSQL("INSERT INTO Types (TType) VALUES ('seek')");
        db.execSQL("INSERT INTO Types (TType) VALUES ('time')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        db.execSQL("DROP TABLE IF EXISTS RootItem");
        db.execSQL("DROP TABLE IF EXISTS Questions");
        db.execSQL("DROP TABLE IF EXISTS Items");
        db.execSQL("DROP TABLE IF EXISTS Types");
        db.execSQL("DROP TABLE IF EXISTS Trials");
        db.execSQL("DROP TABLE IF EXISTS Answers");
        db.execSQL("DROP TABLE IF EXISTS AlarmList");
        onCreate(db);
    }
}
