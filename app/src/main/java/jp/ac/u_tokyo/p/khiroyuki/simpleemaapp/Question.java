package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class Question extends CommonActivity {
    private String question;
    private String answer = "";
    private Integer trialId;
    private int parentId;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent i = this.getIntent();
        parentId = i.getIntExtra("questionType", -1);
        index = i.getIntExtra("questionIndex", -1);
        trialId = i.getIntExtra("trialId", -1);
        if (parentId < 0 || index < 0 || trialId < 0){
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
        } else {

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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
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
        question = theQ.hq.replace("\\n", "\n");
        qHeader.setText(question);
        qHeader.setTextSize(DynamicTextSize.dynamicTextSizeChange(this));

        TextView qDesc = (TextView)findViewById(R.id.desc);
        qDesc.setText(theQ.desc.replace("\\n", "\n"));
        qDesc.setTextSize(DynamicTextSize.dynamicTextSizeChange(this));

        Button nextBtn = (Button)findViewById(R.id.nextButton);
        nextBtn.setTextSize(DynamicTextSize.dynamicTextSizeChange(this));
    }

    public void makeRadio(SearchTheQuestion theQ){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear_question);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 5;


        ListView itemList = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, theQ.items){
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
        layout.addView(itemList, 2, layoutParams);
    }

    public void makeSeek(SearchTheQuestion theQ){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear_question);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 5;
        final MySeekBar mySeek = new MySeekBar(this);
        mySeek.init(this, theQ.qMin.replace("\\n", "\n"), theQ.qMax.replace("\\n", "\n"));
        SeekBar seekBar = (SeekBar)mySeek.findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Boolean isSeekChanged = false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!isSeekChanged) {
                    seekBar.getThumb().setAlpha(255);
                    isSeekChanged = true;
                }
                answer = Integer.toString(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        layout.addView(mySeek, 2, layoutParams);
    }
    public void makeTimePicker(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear_question);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 5;
        TimePicker timePicker = new TimePicker(this);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                answer = hourOfDay + ":" + minute;
            }
        });
        layout.addView(timePicker, 2, layoutParams);
    }

    public void btnNext_onClick(View view){
        if(answer.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error)
                    .setMessage(R.string.empty_answer)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        } else {
            SaveAnswer sa = new SaveAnswer(this);
            sa.SaveTheAnswer(question, answer, trialId);
            Intent intent = new Intent(getBaseContext(), Question.class);
            intent.putExtra("questionType", parentId);
            intent.putExtra("trialId", trialId);
            index++;
            intent.putExtra("questionIndex", index);
            startActivity(intent);
        }
    }
}
