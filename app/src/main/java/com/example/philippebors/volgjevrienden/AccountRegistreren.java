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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountRegistreren extends Activity {

    private EditText name, number;
    private String GetNAME, GetNUMBER;
    private Boolean CheckEditText;

    /* Main function, called upon creation */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register;
        /* The textfields */
        name = (EditText)findViewById(R.id.editText2);
        number = (EditText)findViewById(R.id.editText3);

        /* The button */
        register = (Button)findViewById(R.id.button1) ;

        /* Action peformed on buton click */
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* We reset the boolean so see if the fields are filled in */
                GetCheckEditTextIsEmptyOrNot();

                /* If so, we sent this data to the database */
                if (CheckEditText) {
                    SendDataToServer(GetNAME, GetNUMBER);
                }
                /* Else we show a message */
                else {
                    Toast.makeText(AccountRegistreren.this, "Please fill all form fiels properly.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * GetCheckedEditTestIsEmptyOrNot
     * -> Sets the boolean CheckEditText to true if
     *    all fields are filled in.
     *
     */
   private void GetCheckEditTextIsEmptyOrNot(){

        GetNAME = name.getText().toString();
        GetNUMBER = number.getText().toString();

       /* All fields should be filled in and be correct*/
       CheckEditText = GetNUMBER.contains("06") && GetNUMBER.length() == 10 && !TextUtils.isEmpty(GetNAME) && !TextUtils.isEmpty(GetNUMBER);
    }

    /**
     * SendDataToServer
     * -> Sends the data in the parameters to the database via a POST request.
     * @param name - The name the user entered
     * @param number - The mobile number the user entered
     */
    private void SendDataToServer(final String name, final String number){

        final String currentLongitude = String.valueOf(MapsActivity.myLastLongitude);
        final String currentLatitude = String.valueOf(MapsActivity.myLastLatitude);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                /* Some local variables to use */
                String QuickNAME = name ;
                String QuickNUMBER = number ;
                String QuickLONGITUDE = currentLongitude;
                String QuickLATITUDE = currentLatitude;

                List<NameValuePair> nameValuePairs = new ArrayList<>();


                /* Me make items in the list of pairs */
                nameValuePairs.add(new BasicNameValuePair("name", QuickNAME));
                nameValuePairs.add(new BasicNameValuePair("number", QuickNUMBER));
                nameValuePairs.add(new BasicNameValuePair("longitude", QuickLONGITUDE));
                nameValuePairs.add(new BasicNameValuePair("latitude", QuickLATITUDE));


                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(Config.REGISTER_URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {
                    Toast.makeText(AccountRegistreren.this, "Error: 1" + e.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(AccountRegistreren.this, "Error: 2" + e.toString(), Toast.LENGTH_LONG).show();
                }
                return QuickNAME;
            }

            /* Can be used to ask for a picture in the phone gallery */
            protected Intent getPicture() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
                return intent;
            }

            /* We show a message at the end of a request */
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                /* Reads integer from textfile */
                try {

                    InputStream input = new URL("http://nolden.biz/Android/regStatus.txt").openStream();
                    String myString = IOUtils.toString(input, "UTF-8");
                    if (Objects.equals(myString, "1")) {
                        Toast.makeText(AccountRegistreren.this, "Registration successful", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else Toast.makeText(AccountRegistreren.this, "Number already exists", Toast.LENGTH_LONG).show();
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        /* Here we actually send the data */
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, number, currentLongitude, currentLatitude);
    }
}