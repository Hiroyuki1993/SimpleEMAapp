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
    private ArrayList<String> rootItem = new ArrayList<>();
    private ArrayList<Integer> rootId = new ArrayList<>();
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
        db.execSQL("CREATE TABLE Questions (_id INTEGER PRIMARY KEY AUTOINCREMENT, parent INTEGER NOT NULL, hq TEXT NULL, QType INTEGER NOT NULL, QOrder INTEGER NOT NULL, QDesc TEXT, QMax TEXT, QMin TEXT, itemId TEXT)");
        db.execSQL("CREATE TABLE Items (_id INTEGER PRIMARY KEY AUTOINCREMENT, questionId INTEGER NOT NULL, itemName TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Types (_id INTEGER PRIMARY KEY AUTOINCREMENT, TType TEXT NOT NULL)");
        db.execSQL("CREATE TABLE Trials (_id INTEGER PRIMARY KEY AUTOINCREMENT, QType TEXT, QTime DATETIME DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("CREATE TABLE Answers (_id INTEGER PRIMARY KEY AUTOINCREMENT, trialId INTEGER NOT NULL, question TEXT, answer TEXT, answeredTime DATETIME DEFAULT CURRENT_TIMESTAMP)");
        //TIME STAMP is UTC. Please convert localtime when you use this time.
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
        onCreate(db);
    }

    public boolean WriteInq(ReadXMLFile data, Context c){
        SQLiteDatabase db = getWritableDatabase();
        if(setRoots(data.returnRoots(), db)){
            if(setQuestions(data.returnQuestions(), db)){
                if(!setItems(data.returnItems(), db)){
                    errMsg = c.getResources().getString(R.string.writing_items);
                }
            } else errMsg = c.getResources().getString(R.string.writing_q);
        } else errMsg = c.getResources().getString(R.string.writing_roots);
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
            cv.put("itemId", q.get("id").toString());
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

    public boolean getRootItems(Context c){
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"_id","rootName"};
        Cursor cs = db.query("RootItem", cols, null, null, null, null, null, null);
        if (cs.moveToFirst()){
            do {
                rootId.add(cs.getInt(0));
                rootItem.add(cs.getString(1));
            } while (cs.moveToNext());
            cs.close();
            db.close();
        } else {
            errMsg = c.getResources().getString(R.string.no_root_err);
            return false;
        }
        return true;
    }

    public String[] returnRootItem(){
        return rootItem.toArray(new String[rootItem.size()]);
    }

    public Integer[] returnRootId() {
        return rootId.toArray(new Integer[rootId.size()]);
    }
}
