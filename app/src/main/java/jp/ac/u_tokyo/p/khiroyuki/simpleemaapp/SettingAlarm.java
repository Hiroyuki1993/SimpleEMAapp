package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class SettingAlarm extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        ListView alarmListView = (ListView)findViewById(R.id.alarm_listView);
        AddAlarmList aal = new AddAlarmList(this);
        ArrayList<String> alarmList = aal.searchAlarms();
        ArrayAdapter<String> alarmAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alarmList);
        alarmListView.setAdapter(alarmAdapter);
        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btn_addAlarm_onClick(View view){
        Calendar cal = Calendar.getInstance();
        TimePickerDialog t_time = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                CheckInAlarm cia = new CheckInAlarm(getApplicationContext(), hourOfDay, minute);
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true // initial hour and minute
        );
        t_time.show();
    }
}
