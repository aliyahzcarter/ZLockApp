package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    Button AddCre, RetCre, GenPass, Profile, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        // binding UI to variables
        AddCre = findViewById(R.id.addcre);
        RetCre = findViewById(R.id.retrieve);
        GenPass = findViewById(R.id.generatePass);
        Profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);

        // button to add credentials
        AddCre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomePage.this, AddCredentials.class);
                startActivity(it);
            }
        });

        // buttton to get credentials
        RetCre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomePage.this, RetrieveCredentials.class);
                startActivity(it);
            }
        });

        // button to generate passcode
        GenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomePage.this, GeneratePass.class);
                startActivity(it);
            }
        });

        // button to go to profile
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomePage.this, Profile.class);
                startActivity(it);
            }
        });

        //button to logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveLogin();
                Intent it = new Intent(HomePage.this, Login.class);
                startActivity(it);
                finish();
            }
        });
    }

    //when a user is logout from the application then we are saving login status as no
    // so that on start of login activity we can get login status as false.
    public void SaveLogin(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login","no");
        editor.apply();
    }
}