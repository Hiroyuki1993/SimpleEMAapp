package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RootItemsDBHelper extends  DatabaseHelper{
    private ArrayList<String> rootItem = new ArrayList<>();
    private ArrayList<Integer> rootId = new ArrayList<>();
    public int errMsg;

    public RootItemsDBHelper(Context context){
        super(context);
    }

    public boolean getRootItems(){
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
            errMsg = R.string.no_root_err;
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
