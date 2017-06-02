package com.example.philippebors.volgjevrienden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {
    /* The text field */
    private EditText number;
    /* Content in the text field */
    private String GetNUMBER;
    /* Is true if the content is correct */
    private boolean CheckEditText;

    /**
     * onCreate
     * -> Method is called upon creation of the activity. It initializes our text field and
     *    button and its listener
     *
     * @param savedInstanceState -  The last saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        number = (EditText)findViewById(R.id.telnumber);
        Button addThisFriend;
        addThisFriend = (Button)findViewById(R.id.tel_sign_in_button);

        addThisFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getCheckEditTextIsEmptyOrNot();

                /* If correct, we send this data to the database */
                if (CheckEditText) {
                    sendDataToServer(GetNUMBER);
                }
                /* Else we show a message */
                else {
                    Toast.makeText(FriendActivity.this, "Please enter a real phone-number.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * getCheckEditTextIsEmptyOrNot
     * -> Checks if our text field is not empty and a mobile number
     */
    private void getCheckEditTextIsEmptyOrNot() {
        /* We get the content of the text field */
        GetNUMBER = number.getText().toString();

       /* All fields should be filled in and the number should be 10 digits and contain 06 */
        CheckEditText = !(TextUtils.isEmpty(GetNUMBER) || !GetNUMBER.contains("06")
                || GetNUMBER.length() != 10);
    }

    /**
     * sendDataToServer
     * -> The number the user gave us will be send in order to check if
     *    this number already exists or not. If it exists, the user
     *    can log in. Else he cannot.
     *
     * @param number  - The number the user gave as input
     */
    private void sendDataToServer(final String number) {

        final String phonenumber1 = Config.MY_NUMBER;
        final String phonenumber2 = number;

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                /* Me make items in the list of pairs */
                nameValuePairs.add(new BasicNameValuePair("phonenumber1", phonenumber1));
                nameValuePairs.add(new BasicNameValuePair("phonenumber2", phonenumber2));

                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(Config.FRIEND_URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                } catch (ClientProtocolException e) {
                    Toast.makeText(FriendActivity.this, "Error: 1" + e.toString(),
                            Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(FriendActivity.this, "Error: 2" + e.toString(),
                            Toast.LENGTH_LONG).show();
                }
                return number;
            }

            /**
             * onPostExecute
             * -> We show a message to indicate that the number has been added
             *    as a friend.
             * @param result -
             */
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(FriendActivity.this, "Friend added", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(phonenumber1, phonenumber2);
    }
}