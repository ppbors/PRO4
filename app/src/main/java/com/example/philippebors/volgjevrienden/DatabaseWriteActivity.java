package com.example.philippebors.volgjevrienden;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DatabaseWriteActivity extends Activity {

    EditText id, name, number, longitude, latitude;
    String GetID, GetNAME, GetNUMBER, GetLONGITUDE, GetLATITUDE;
    Button register ;
    String DataParseUrl = "http://nolden.biz/Android/insert-registration-data.php" ;
    Boolean CheckEditText ;
    String Response;
    HttpResponse response ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_write);

        id = (EditText)findViewById(R.id.editText1);
        name = (EditText)findViewById(R.id.editText2);
        number = (EditText)findViewById(R.id.editText3);
        longitude = (EditText)findViewById(R.id.editText4);
        latitude = (EditText)findViewById(R.id.editText5);
        //password = (EditText)findViewById(R.id.editText3);

        register = (Button)findViewById(R.id.button1) ;

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SendDataToServer(GetID, GetNAME, GetNUMBER, GetLONGITUDE, GetLATITUDE);

/*                GetCheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    SendDataToServer(GetID, GetNAME, GetNUMBER, GetLONGITUDE, GetLATITUDE);

                }
                else {

                    Toast.makeText(DatabaseWriteActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }*/

            }
        });
    }

/*    public void GetCheckEditTextIsEmptyOrNot(){

        GetID = id.getText().toString();
        GetNAME = name.getText().toString();
        GetNUMBER = number.getText().toString();
        GetLONGITUDE = longitude.getText().toString();
        GetLATITUDE = latitude.getText().toString();

        if(TextUtils.isEmpty(GetID) || TextUtils.isEmpty(GetNAME) || TextUtils.isEmpty(GetNUMBER) || TextUtils.isEmpty(GetLONGITUDE) || TextUtils.isEmpty(GetLATITUDE))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }*/

    public void SendDataToServer(final String id, final String name, final String number, final String longitude, final String latitude){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickID = id ;
                String QuickNAME = name ;
                String QuickNUMBER = number ;
                String QuickLONGITUDE = longitude;
                String QuickLATITUDE = latitude ;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", QuickID));
                nameValuePairs.add(new BasicNameValuePair("name", QuickNAME));
                nameValuePairs.add(new BasicNameValuePair("number", QuickNUMBER));
                nameValuePairs.add(new BasicNameValuePair("longitude", QuickLONGITUDE));
                nameValuePairs.add(new BasicNameValuePair("latitude", QuickLATITUDE));


                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(DataParseUrl);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                //return "Data Submit Successfully";
                return QuickID;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(DatabaseWriteActivity.this, id, Toast.LENGTH_LONG).show();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, name, number, longitude, latitude);
    }

}