package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;

public class ReadInquiryFile extends CommonActivity {
    private String fpath = "";
    private ImportInquiryDBHelper importHelper = new ImportInquiryDBHelper(this);

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
        fileDialog.showDialog();
    }

    public void btn_importFile(View v) throws FileNotFoundException, XmlPullParserException {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setTitle(R.string.warning)
                .setMessage(R.string.read_warning)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            importFile();
                        } catch (FileNotFoundException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    public void importFile() throws FileNotFoundException, XmlPullParserException {
        ReadXMLFile data = new ReadXMLFile(fpath);
        String errMsg = data.errMsg(this);
        if(!errMsg.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(errMsg).setMessage(errMsg).show();
            return;
        }
        importHelper.initTable();
        if(!importHelper.WriteInq(data)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error).setMessage(importHelper.errMsg).show();
            return;
        }
        AlertDialog.Builder success_loaded = new AlertDialog.Builder(this);
        success_loaded.setTitle(R.string.notice)
                .setMessage(R.string.read_success)
        .show();
    }
}
