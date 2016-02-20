package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Administration extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_administration, menu);
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
    public void read_file_onClick(View v){
        Intent i = new Intent(this, ReadInquiryFile.class);
        startActivity(i);
    }
    public void change_text_onClick(View v){
        Intent i = new Intent(this, MyConfig.class);
        startActivity(i);
    }
    public void reflect_onClick(View v){
        Intent i = new Intent(this, Top.class);
        startActivity(i);
    }
    public void export_answer_onClick(View v){
        Intent i = new Intent(this, ExportAnswers.class);
        startActivity(i);
    }
}
