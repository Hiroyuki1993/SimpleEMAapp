package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class Branching extends CommonActivity {
    private int errMsg;
    private ArrayList<Integer> buttonId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branching);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(R.string.app_name);

        TextView txt = (TextView)this.findViewById(R.id.branch_title);
        txt.setTextSize(DynamicTextSize.dynamicTextSizeChange(getApplicationContext()));
        if (!makeBranchButton()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning).setMessage(errMsg)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), Top.class);
                            startActivity(i);
                        }
                    }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_branching, menu);
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

    public boolean makeBranchButton(){
        RootItemsDBHelper rootHelper = new RootItemsDBHelper(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear_branch);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 20, 0, 0);

        if(!rootHelper.getRootItems()){
            errMsg = rootHelper.errMsg;
            return false;
        }
        String[] rootItems = rootHelper.returnRootItem();
        Integer[] rootId = rootHelper.returnRootId();
        for (int i = 0; i < rootItems.length; i++) {
            buttonId.add(View.generateViewId());
            Button bt = new Button(this);
            bt.setId(buttonId.get(buttonId.size()-1));
            bt.setText(rootItems[i]);
            bt.setTextSize(DynamicTextSize.dynamicTextSizeChange(this));
            bt.setTag(R.string.rootIdTag, rootId[i]);
            bt.setTag(R.string.rootItemTag, rootItems[i]);
            bt.setBackground(getResources().getDrawable(R.drawable.custom_button));
            ll.addView(bt, layoutParams);
            findViewById(buttonId.get(buttonId.size()-1)).setOnClickListener(mOnClickListener);
        }
        return true;
    }

    public View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for(int id:buttonId){
                if(v.getId() == id){
                    SaveAnswer sa = new SaveAnswer(getApplicationContext());
                    int trialId = sa.AddNewTrial(v.getTag(R.string.rootItemTag).toString());
                    Intent i = new Intent(getApplicationContext(), Question.class);
                    i.putExtra("questionType", Integer.parseInt(v.getTag(R.string.rootIdTag).toString()));
                    i.putExtra("trialId", trialId);
                    int index = 0;
                    i.putExtra("questionIndex", index);
                    startActivity(i);
                }
            }
        }
    };
}
