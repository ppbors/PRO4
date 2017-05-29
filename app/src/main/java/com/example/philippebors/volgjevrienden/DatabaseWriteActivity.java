package com.example.philippebors.volgjevrienden;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

public class DatabaseWriteActivity extends Activity {

    private EditText name, number;
    private String GetNAME, GetNUMBER;
    private Button register ;
    private String DataParseUrl = "http://nolden.biz/Android/insert-registration-data.php" ;
    private Boolean CheckEditText ;

    /* Main function, called upon creation */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_write);

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
                    Toast.makeText(DatabaseWriteActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
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
   public void GetCheckEditTextIsEmptyOrNot(){

        GetNAME = name.getText().toString();
        GetNUMBER = number.getText().toString();

       /* All fields should be filled in*/
        if (TextUtils.isEmpty(GetNAME) || TextUtils.isEmpty(GetNUMBER)) {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }

    /**
     * SendDataToServer
     * -> Sends the data in the parameters to the database via a POST request.
     * @param name - The name the user entered
     * @param number - The mobile number the user entered
     */
    public void SendDataToServer(final String name, final String number){

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

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                /* Me make items in the list of pairs */
                nameValuePairs.add(new BasicNameValuePair("name", QuickNAME));
                nameValuePairs.add(new BasicNameValuePair("number", QuickNUMBER));
                nameValuePairs.add(new BasicNameValuePair("longitude", QuickLONGITUDE));
                nameValuePairs.add(new BasicNameValuePair("latitude", QuickLATITUDE));


                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(DataParseUrl);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {
                    Toast.makeText(DatabaseWriteActivity.this, "Error: 1" + e.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(DatabaseWriteActivity.this, "Error: 2" + e.toString(), Toast.LENGTH_LONG).show();
                }
                return QuickNAME;
            }

            /* We show a message at the end of a request */
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(DatabaseWriteActivity.this, "Data sent", Toast.LENGTH_LONG).show();

            }
        }
        /* Here we actually send the data */
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, number, currentLongitude, currentLatitude);
    }

}