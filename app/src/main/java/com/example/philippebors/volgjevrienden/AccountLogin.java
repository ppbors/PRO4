package com.example.philippebors.volgjevrienden;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        number = (EditText)findViewById(R.id.editText3);
        login = (Button)findViewById(R.id.button3);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* We reset the boolean so see if the fields are filled in */
                GetCheckEditTextIsEmptyOrNot();

                /* If so, we sent this data to the database */
                if (CheckEditText) {
                    /*Check of nummer in database is, zo ja verbind, zo nee dan niet*/
                    sendDataToServer(GetNUMBER);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (readTextFile()) {
                        Config.MY_NUMBER = GetNUMBER;
                        Intent intent = new Intent(v.getContext(), MapsActivity.class);
                        startActivity(intent);
                    }
                }
                /* Else we show a message */
                else {
                    Toast.makeText(AccountLogin.this, "Please fill in a mobile number.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * readTextFile
     * -> Checks the login status and returns true if it contains 1
     * @return  - True if 1, else false
     */
    private boolean readTextFile() {
        try {

            /* We try to read the textfile for the right number */
            InputStream input = new URL(Config.LOGIN_STATUS_URL).openStream();
            String myString = IOUtils.toString(input, "UTF-8");

            if (myString.contains("1")) {
                Toast.makeText(AccountLogin.this, "Login was successful (code " + myString + ")", Toast.LENGTH_LONG).show();
                return true;
            }
            else {
                Toast.makeText(AccountLogin.this, "Number is not registered (code " + myString + ")", Toast.LENGTH_LONG).show();
                return false;
            }

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * sendDataToServer
     * -> The number the user gave us will be send in order to check if
     *    this number already exists or not. If it does exist, the user
     *    can login. Else he cannot.
     *
     * @param number  - The number the user gave as input
     */
    private void sendDataToServer(final String number) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickNUMBER = number;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                /* Me make items in the list of pairs */
                nameValuePairs.add(new BasicNameValuePair("number", QuickNUMBER));


                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(Config.DATA_PARSE_URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {
                    Toast.makeText(AccountLogin.this, "Error: 1" + e.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(AccountLogin.this, "Error: 2" + e.toString(), Toast.LENGTH_LONG).show();
                }
                return QuickNUMBER;
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(number);
    }

    /**
     * GetCheckEditTextIsEmptyOrNot
     * -> Checks if our textfield is empty and is a mobile number
     */
    public void GetCheckEditTextIsEmptyOrNot(){

        GetNUMBER = number.getText().toString();

       /* All fields should be filled in and the number should be 10 digits and contain 06 */
        if (TextUtils.isEmpty(GetNUMBER) || !GetNUMBER.contains("06") || GetNUMBER.length() != 10) {
            CheckEditText = false;
        }
        else {
            CheckEditText = true;
        }
    }

    /**
     * registerCalled
     * -> This button is called whenever the register button is clicked
     *
     * @param view
     */
    public void registerCalled(View view) {
        Intent intent = new Intent(this, AccountRegistreren.class);
        startActivity(intent);
    }


}

