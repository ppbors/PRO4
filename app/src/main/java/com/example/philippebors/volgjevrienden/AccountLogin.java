package com.example.philippebors.volgjevrienden;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.Scanner;

public class AccountLogin extends AppCompatActivity {
    private EditText number;
    private Button login;
    private boolean CheckEditText;
    private String GetNUMBER;
    private String DataParseUrl = "http://nolden.biz/Android/loginAccount.php";
    private boolean isOke = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    if (readTextFile()) {
                        isOke = true;
                    }
                }
                /* Else we show a message */
                else {
                    Toast.makeText(AccountLogin.this, "Please fill in your number.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean readTextFile() {
        try {




            InputStream input = new URL("http://nolden.biz/Android/status.txt").openStream();
            String myString = IOUtils.toString(input, "UTF-8");


            if (myString == "1") {
                return true;
            }
            else return false;

        }
        catch(IOException ex) {
            // there was some connection problem, or the file did not exist on the server,
            // or your URL was not in the right format.
            // think about what to do now, and put it here.
            ex.printStackTrace(); // for now, simply output it.
        }
        return false;
    }

    private void sendDataToServer(final String number) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                /* Some local variables to use */
                String QuickNUMBER = number;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                /* Me make items in the list of pairs */
                nameValuePairs.add(new BasicNameValuePair("number", QuickNUMBER));



                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(DataParseUrl);

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

    public void registerCalled(View view) {
        Intent intent = new Intent(this, AccountRegistreren.class);
        startActivity(intent);
    }

    public void loginClicked(View view) {
        if (isOke) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }
}
