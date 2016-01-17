package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class NumberPickerDialogPreference extends DialogPreference {

    NumberPicker np;
    public NumberPickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerDialogPreference(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateDialogView() {
        this.np = new NumberPicker(this.getContext());
        this.np.setMinValue(10);
        this.np.setMaxValue(40);
        return this.np;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            persistInt(this.np.getValue());
        }
        super.onDialogClosed(positiveResult);
    }
}
