package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class Question extends ActionBarActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private String answer;
    private int parentId;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent i = this.getIntent();
        parentId = i.getIntExtra("questionType", -1);
        index = i.getIntExtra("questionIndex", -1);
        if (parentId < 0 || index < 0){
            returnTop(R.string.receive_intent_err);
        }
        SearchTheQuestion theQ = new SearchTheQuestion(this);
        if(!theQ.SearchQ(parentId, index)){
            if(theQ.endOfQuestion){
                Intent end_intent = new Intent(this, EndOfQuestion.class);
                startActivity(end_intent);
            } else {
                returnTop(theQ.errMsg);
            }
        }

        setTitle(theQ);

        switch (theQ.qType) {
            case "radio":
                if (!theQ.getQitem(theQ.qid)) {
                    returnTop(theQ.errMsg);
                }
                makeRadio(theQ);
                break;
            case "seek":
                makeSeek(theQ);
                break;
            case "time":
                makeTimePicker();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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

    public void returnTop(int Rid){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error)
        .setMessage(Rid).show();
        Intent i = new Intent(this, Top.class);
        startActivity(i);
    }

    public void setTitle(SearchTheQuestion theQ) {
        TextView qHeader = (TextView)findViewById(R.id.hq);
        qHeader.setText(theQ.hq);
        qHeader.setTextSize(DynamicTextSize.dynamicTextSizeChange(this));

        TextView qDesc = (TextView)findViewById(R.id.desc);
        qDesc.setText(theQ.desc);
        qDesc.setTextSize(DynamicTextSize.dynamicTextSizeChange(this));
    }

    public void makeRadio(SearchTheQuestion theQ){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear_question);

        ListView itemList = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, theQ.items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView)super.getView(position, convertView, parent);
                view.setTextSize(DynamicTextSize.dynamicTextSizeChange(getApplicationContext()));
                return view;
            }
        };
        itemList.setAdapter(adapter);
        itemList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                answer = ((TextView) view).getText().toString();
            }
        });
        layout.addView(itemList, 2, new LinearLayout.LayoutParams(MP, WC));
    }

    public void makeSeek(SearchTheQuestion theQ){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear_question);
        final MySeekBar mySeek = new MySeekBar(this);
        mySeek.init(this, theQ.qMin, theQ.qMax);
        SeekBar seekBar = mySeek.returnSeekObject(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                answer = Integer.toString(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.getThumb().setAlpha(0);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        layout.addView(mySeek, 2, new LinearLayout.LayoutParams(MP, WC));
    }
    public void makeTimePicker(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear_question);
        TimePicker timePicker = new TimePicker(this);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                answer = hourOfDay + ":" + minute;
            }
        });
        layout.addView(timePicker, 2, new LinearLayout.LayoutParams(MP, WC));
    }

    public void btnNext_onClick(View view){
        Intent intent = new Intent(getApplicationContext(), Question.class);
        intent.putExtra("questionType", parentId);
        index++;
        intent.putExtra("questionIndex", index);
        startActivity(intent);
    }
}
