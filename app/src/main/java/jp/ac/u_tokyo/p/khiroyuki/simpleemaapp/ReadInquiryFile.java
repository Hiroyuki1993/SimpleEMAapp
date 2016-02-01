package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ReadInquiryFile extends ActionBarActivity {
    private String fpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_inquiry_file);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_inquiry_file, menu);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView textView1 = (TextView)findViewById(R.id.txt_file_path);
        String value = textView1.getText().toString();
        outState.putString("TEXT_VIEW_STR", value);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String value = savedInstanceState.getString("TEXT_VIEW_STR");
        TextView textView1 = (TextView)findViewById(R.id.txt_file_path);
        textView1.setText(value);
    }

    //start file choice dialog
    public void btn_ChoseFile(View v){
        final TextView text = (TextView)this.findViewById(R.id.txt_file_path);
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        FileDialog fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".xml");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                fpath = file.toString();
                text.setText("file path:" + fpath);
            }
        });
        //fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
            //public void directorySelected(File directory) {
                //(getClass().getName(), "selected dir " + directory.toString());
            //}
        //});
        //fileDialog.setSelectDirectoryOption(false);
        fileDialog.showDialog();
    }

    public void btn_importFile(View v) throws FileNotFoundException, XmlPullParserException {
        ReadXMLFile data = new ReadXMLFile(fpath);
        /*if(WriteDatabase(data)){
            Allart("success")
        }*/
    }
}
