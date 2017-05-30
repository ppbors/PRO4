package com.example.philippebors.volgjevrienden;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountLogin extends AppCompatActivity {
    private EditText number;
    private Button login;
    private boolean CheckEditText;
    private String GetNUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        number = (EditText)findViewById(R.id.editText3);
        login = (Button)findViewById(R.id.button1);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* We reset the boolean so see if the fields are filled in */
                GetCheckEditTextIsEmptyOrNot();

                /* If so, we sent this data to the database */
                if (CheckEditText) {
                    /*Check of nummer in database is, zo ja verbind, zo nee dan niet*/
                }
                /* Else we show a message */
                else {
                    Toast.makeText(AccountLogin.this, "Please fill in your number.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void GetCheckEditTextIsEmptyOrNot(){

        GetNUMBER = number.getText().toString();

       /* All fields should be filled in*/
        if (TextUtils.isEmpty(GetNUMBER)) {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }
}
