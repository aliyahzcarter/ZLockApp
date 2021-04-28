package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.zlock.zlock.AES.AESUtills;
import com.zlock.zlock.DataModel.User;
import com.zlock.zlock.Helper.CreHelper;
import com.zlock.zlock.Helper.UserHelper;

import java.util.List;

public class Profile extends AppCompatActivity {


    //input from UI
    TextView name, email, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //binding input from ui to variable
        name = findViewById(R.id.nameshow);
        email = findViewById(R.id.emailshow);
        pwd = findViewById(R.id.passshow);


        UserHelper helper = new UserHelper(Profile.this);
        List<User> myList = helper.ReadData();

        // check the list and display the user information
        for (int i = 0; i < myList.size(); i++){
            if (Integer.toString(myList.get(i).getId()).equals(isAlreadyLogin())){
                name.setText(myList.get(i).getName());
                email.setText(myList.get(i).getEmail());


                try {
                    pwd.setText(AESUtills.decrypt(myList.get(i).getPassword()));
                } catch (Exception e) {
                    e.printStackTrace();
                    pwd.setText(myList.get(i).getPassword());
                }

                break;
            }
        }


    }
//check to see which user logged in
    public String isAlreadyLogin(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("user_id", "");
    }
}