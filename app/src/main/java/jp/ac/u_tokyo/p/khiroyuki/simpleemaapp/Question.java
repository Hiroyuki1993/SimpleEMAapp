package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Question extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent i = this.getIntent();
        int parentId = i.getIntExtra("questionType", -1);
        int index = i.getIntExtra("questionIndex", -1);
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
        if (theQ.qType.equals("radio")){
            if(!theQ.getQitem(theQ.qid)){
                returnTop(theQ.errMsg);
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

    public void returnTop(int Rid){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error)
        .setMessage(Rid).show();
        Intent i = new Intent(this, Top.class);
        startActivity(i);
    }
}
