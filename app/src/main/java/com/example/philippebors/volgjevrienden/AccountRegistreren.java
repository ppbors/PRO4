package com.example.philippebors.volgjevrienden;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountRegistreren extends Activity {

    /* The text fields */
    private EditText name, number, link;
    /* Content in the text fields */
    private String GetNAME, GetNUMBER, GetURL;
    /* Is true if the fields are filled in properly */
    private Boolean CheckEditText;


    /**
     * onCreate
     * -> We add the text fields and button and its listener and set the layout
     *
     * @param savedInstanceState - The last saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.editText);
        number = (EditText)findViewById(R.id.editText2);
        link = (EditText)findViewById(R.id.editText3);

        Button register;
        register = (Button)findViewById(R.id.button1);

        /* Action performed on button click */
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* We reset the boolean so see if the fields are filled in */
                GetCheckEditTextIsEmptyOrNot();

                /* If so, we sent this data to the database */
                if (CheckEditText) {
                    sendDataToServer(GetNAME, GetNUMBER, GetURL);
                }
                /* Else we show a message */
                else {
                    Toast.makeText(AccountRegistreren.this,
                            "Please fill all form fields properly.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * GetCheckedEditTestIsEmptyOrNot
     * -> Sets the boolean CheckEditText to true if
     *    all fields are filled in.
     */
   private void GetCheckEditTextIsEmptyOrNot() {
        /* We get the users input */
        GetNAME = name.getText().toString();
        GetNUMBER = number.getText().toString();
        GetURL = link.getText().toString();


       /* All fields should be filled in and be correct*/
       CheckEditText = GetNUMBER.contains("06") && GetNUMBER.length() == 10
               && !TextUtils.isEmpty(GetNAME) && !TextUtils.isEmpty(GetNUMBER)
               && GetURL.length() < 100;
    }

    /**
     * sendDataToServer
     * -> Sends the data in the parameters (and the location) to the database via a POST request.
     * @param name - The name the user entered
     * @param number - The mobile number the user entered
     */
    private void sendDataToServer(final String name, final String number, final String link) {

        /* Here we get our location */
        final String currentLongitude = String.valueOf(MapsActivity.myLastLongitude);
        final String currentLatitude = String.valueOf(MapsActivity.myLastLatitude);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                /* Me make items in the list of pairs */
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("number", number));
                nameValuePairs.add(new BasicNameValuePair("longitude", currentLongitude));
                nameValuePairs.add(new BasicNameValuePair("latitude", currentLatitude));
                nameValuePairs.add(new BasicNameValuePair("link", link));

                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(Config.REGISTER_URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                } catch (ClientProtocolException e) {
                    Toast.makeText(AccountRegistreren.this, "Error: 1" + e.toString(),
                            Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(AccountRegistreren.this, "Error: 2" + e.toString(),
                            Toast.LENGTH_LONG).show();
                }
                return name;
            }


            /**
             * onPostExecute
             * -> At the end of a registration, this method is called to ensure
             *    the correctness of the registration. We read our .txt file and
             *    determine the outcome.
             * @param result -
             */
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                /* Reads integer from text file */
                try {
                    InputStream input = new URL(Config.REGISTER_STATUS_URL + GetNUMBER +
                            "regStatus.txt").openStream();
                    String myString = IOUtils.toString(input, "UTF-8");
                    if (Objects.equals(myString, "1")) {
                        Toast.makeText(AccountRegistreren.this, "Registration successful",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else Toast.makeText(AccountRegistreren.this, "Number already exists",
                            Toast.LENGTH_LONG).show();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        /* Here we send the data */
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, number, currentLongitude, currentLatitude, link);
        Config.MY_LINK = link;
    }
}