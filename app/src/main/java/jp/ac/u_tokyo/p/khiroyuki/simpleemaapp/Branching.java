package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.AlertDialog;
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
    private static String BUTTON_ID;

    static {
        BUTTON_ID = "ButtonId_";
    }

    private String errMsg;
    private ArrayList<Integer> buttonId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branching);

        TextView txt = (TextView)this.findViewById(R.id.branch_title);
        txt.setTextSize(DynamicTextSize.dynamicTextSizeChange(getApplicationContext()));
        if (!makeBranchButton()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning).setMessage(errMsg).show();
            Intent i = new Intent(this, Top.class);
            startActivity(i);
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
        DatabaseHelper helper = new DatabaseHelper(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear_branch);
        if(!helper.getRootItems(this)){
            errMsg = helper.errMsg;
            return false;
        }
        for (String rootItem:helper.returnRootItem()){
            buttonId.add(getResources().getIdentifier(BUTTON_ID + rootItem, "id", "jp.ac.u_tokyo.p.k_hiroyuki.simpleemaapp"));
            Button bt = new Button(this);
            bt.setId(buttonId.get(buttonId.size()-1));
            bt.setText(rootItem);
            bt.setTextSize(DynamicTextSize.dynamicTextSizeChange(this));
            ll.addView(bt);
            findViewById(buttonId.get(buttonId.size()-1)).setOnClickListener(mOnClickListener);
        }
        return true;
    }

    public View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                default:break;
            }
        }
    };
}
