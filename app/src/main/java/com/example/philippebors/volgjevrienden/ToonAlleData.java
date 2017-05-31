package com.example.philippebors.volgjevrienden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ToonAlleData extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    /* Declaring an Spinner */
    private Spinner spinner;

    /* An ArrayList for Spinner Items */
    private ArrayList<String> persons;

    /* JSON Array */
    private JSONArray result;

    /* TextViews to display details */
    private TextView textViewName;
    private TextView textViewNumber;
    private TextView textViewLongitude;
    private TextView textViewLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toonalledata);

        /* Initializing the ArrayList */
        persons = new ArrayList<>();

        /* Initializing Spinner */
        spinner = (Spinner) findViewById(R.id.spinner);

        /* Adding an Item Selected Listener to our Spinner
           As we have implemented the class Spinner.OnItemSelectedListener to
           this class iteself we are passing this to setOnItemSelectedListener */
        spinner.setOnItemSelectedListener(this);

        /* Initializing TextViews */
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);

        /* This method will fetch the data from the URL */
        getData();
    }


    /**
     * getData
     * -> Requests person data from the url and puts in in the JSON array
     */
    private void getData() {
        /* Creating a string request */
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            /* Parsing the fetched Json String to JSON Object */
                            j = new JSONObject(response);

                            /* Storing the Array of JSON String to our JSON Array */
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            /* Calling method getPersons to get the persons from the JSON Array */
                            getPersons(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        /* Creating a request queue*/
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        /* Adding request to the queue */
        requestQueue.add(stringRequest);
    }


    private void getPersons(JSONArray j) {
        /* Traversing through all the items in the json array */
        for (int i = 0; i < j.length(); i++) {
            try {
                /* Getting json object */
                JSONObject json = j.getJSONObject(i);

                /* Adding the name of the student to array list */
                persons.add(json.getString(Config.TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /* Setting adapter to show the items in the spinner */
        spinner.setAdapter(new ArrayAdapter<>(ToonAlleData.this, android.R.layout.simple_spinner_dropdown_item, persons));
    }


    /* Doing the same with this method as we did with getName() */
    private String getNumber(int position) {
        String number="";
        try {
            JSONObject json = result.getJSONObject(position);
            number = json.getString(Config.TAG_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return number;
    }

    /* Doing the same with this method as we did with getName() */
    private String getLongitude(int position) {
        String Longitude="";
        try {
            JSONObject json = result.getJSONObject(position);
            Longitude = json.getString(Config.TAG_LONGITUDE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Longitude;
    }

    /* Doing the same with this method as we did with getName() */
    private String getLatitude(int position) {
        String latitude="";
        try {
            JSONObject json = result.getJSONObject(position);
            latitude = json.getString(Config.TAG_LATITUDE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latitude;
    }


    /* This method will execute when we pick an item from the spinner */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /* Setting the values to textviews for a selected item */
        textViewNumber.setText(getNumber(position));
        textViewLongitude.setText(getLongitude(position));
        textViewLatitude.setText(getLatitude(position));
    }

    /* When no item is selected this method would execute */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textViewName.setText("");
        textViewNumber.setText("");
        textViewLongitude.setText("");
        textViewLatitude.setText("");
    }
}