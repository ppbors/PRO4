package com.example.philippebors.volgjevrienden;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FriendActivity extends AppCompatActivity {

    private EditText number;
    private Button addThisFriend;
    private String GetNUMBER;
    private boolean CheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        number = (EditText)findViewById(R.id.telnumber);
        addThisFriend = (Button)findViewById(R.id.tel_sign_in_button);

        addThisFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GetCheckEditTextIsEmptyOrNot();

                /* If so, we sent this data to the database */
                if (CheckEditText) {
                    sendDataToServer(GetNUMBER);
                }
                /* Else we show a message */
                else if (!CheckEditText) {
                    Toast.makeText(FriendActivity.this, "Please fill a real phonenumber.", Toast.LENGTH_LONG).show();
                }
            }
        });

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
                nameValuePairs.add(new BasicNameValuePair("phonenumber1", Config.MY_NUMBER));
                nameValuePairs.add(new BasicNameValuePair("phonenumber2", QuickNUMBER));


                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(Config.FRIEND_URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {
                    Toast.makeText(FriendActivity.this, "Error: 1" + e.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(FriendActivity.this, "Error: 2" + e.toString(), Toast.LENGTH_LONG).show();
                }
                return QuickNUMBER;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(FriendActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        String phonenumber1 = Config.MY_NUMBER;
        String phonenumber2 = number;
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(phonenumber1, phonenumber2);
    }


}
