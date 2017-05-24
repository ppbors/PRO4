package com.example.philippebors.volgjevrienden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
    }

    public void buttonClicked(View view) {

        //TODO: Maak een connectie met de database en kijk of het telefoonnummber bestaat
        //TODO: Zo ja, voeg deze gebruiker toe aan onze gebruiker zijn vriendenlijst
        //TODO: Zo nee, dan geef warning

    }
}
