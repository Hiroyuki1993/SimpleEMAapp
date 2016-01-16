package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordDialog extends Dialog{
    private String correctPass = "simple";
//Constructor
    public PasswordDialog(Context context) {
        super(context);
        this.setContentView(R.layout.dialog_password);
    }

    void setPasswordDialog(){
        final EditText txtPass = (EditText)this.findViewById(R.id.password);
        final TextView txtMsg = (TextView)this.findViewById(R.id.txtMsg);
        Button btnOK = (Button)this.findViewById(R.id.OK_btn);
        Button btnCancel = (Button)this.findViewById(R.id.cancel_btn);
        this.setTitle(R.string.ps_dialog_title);
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (txtPass.getText().toString().equals(correctPass)) {
                    GotoAdminPage();
                } else {
                    txtMsg.setText(R.string.incorrect_password);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    void GotoAdminPage() {

    }
}
