package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ExportAnswers extends CommonActivity {
    private String folderPath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_answers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_export_answers, menu);
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

    public void btn_ChoseFolder(View v){
        final TextView text = (TextView)this.findViewById(R.id.txt_folder_path);
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        FileDialog fileDialog = new FileDialog(this, mPath);
        fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
            public void directorySelected(File directory) {
                folderPath = directory.toString();
                text.setText("folder path: " + folderPath);
            }
        });
        fileDialog.setSelectDirectoryOption(true);
        fileDialog.showDialog();
    }

    public void exportBtnOnClick(View view){
        if(folderPath.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning)
                    .setMessage(R.string.empty_folder_path)
                    .show();
            return;
        }
        ReadSubjectAnswer rsa = new ReadSubjectAnswer(this);
        ArrayList<String[]> outputArray = rsa.searchAnswerToArray();
        if(outputArray == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error)
                    .setMessage(getResources().getString(rsa.errMsg))
                    .show();
        } else {
            MakeCSVFile exporter = new MakeCSVFile();
            exporter.saveCSV(folderPath, outputArray);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.notice)
                    .setMessage(R.string.write_csv_success)
                    .show();
        }
    }
}
