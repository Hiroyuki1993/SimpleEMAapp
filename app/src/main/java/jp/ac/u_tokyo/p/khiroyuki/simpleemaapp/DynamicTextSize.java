package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DynamicTextSize {

    public static float dynamicTextSizeChange(Context c){
        final int MICRO = 0;
        final int SMALL = 1;
        final int MEDIUM = 2;
        final int LARGE = 3;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        Integer size = Integer.parseInt(pref.getString("txtSize", "3"));
        int ref;
        switch (size){
            case MICRO:
                ref = R.dimen.text_size_micro;
                break;
            case SMALL:
                ref = R.dimen.text_size_small;
                break;
            case MEDIUM:
                ref = R.dimen.text_size_medium;
                break;
            case LARGE:
                ref = R.dimen.text_size_large;
                break;
            default:
                ref = R.dimen.text_size_micro;
        }
        return c.getResources().getDimension(ref);
    }
}
