package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class SettingAlarm extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        ListView alarmListView = (ListView)findViewById(R.id.alarm_listView);
        AlarmListDBHelper AListHelper = new AlarmListDBHelper(this);
        ArrayList<String> alarmList = AListHelper.searchAlarms();
        if(alarmList != null){
            ArrayAdapter<String> alarmAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alarmList);
            alarmListView.setAdapter(alarmAdapter);
        }
        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String timeText = ((TextView)view).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.remove_alarm)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckInAlarm cia = new CheckInAlarm();
                                cia.removeAlarm(getApplicationContext(), timeText);
                                updateAlarmList();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
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
                CheckInAlarm cia = new CheckInAlarm();
                if(!cia.checkInAlarm(getApplicationContext(), hourOfDay, minute)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingAlarm.this);
                    builder.setTitle(R.string.warning)
                            .setMessage(R.string.redundant_alarm)
                            .show();
                    return;
                }
                updateAlarmList();
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true // initial hour and minute
        );
        t_time.show();
    }

    private void updateAlarmList(){
        ListView alarmListView = (ListView)findViewById(R.id.alarm_listView);
        AlarmListDBHelper AListHelper = new AlarmListDBHelper(this);
        ArrayList<String> alarmList = AListHelper.searchAlarms();
        ArrayAdapter<String> alarmAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alarmList);
        alarmListView.setAdapter(alarmAdapter);
    }
}
