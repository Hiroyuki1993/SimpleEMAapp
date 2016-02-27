package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MySeekBar extends RelativeLayout {
    public MySeekBar(Context context) {
        super(context);
    }

    public void init(Context context, String qMin, String qMax) {
        View layout = LayoutInflater.from(context).inflate(R.layout.my_seek_bar, this);
        TextView seekMin = (TextView)layout.findViewById(R.id.seekMin);
        TextView seekMax = (TextView)layout.findViewById(R.id.seekMax);
        seekMax.setTextSize(DynamicTextSize.dynamicTextSizeChange(getContext()));
        seekMin.setTextSize(DynamicTextSize.dynamicTextSizeChange(getContext()));
        final SeekBar seekbar = (SeekBar)layout.findViewById(R.id.seek);
        seekbar.getThumb().setAlpha(0);
        seekMin.setText(qMin);
        seekMax.setText(qMax);
    }
}
