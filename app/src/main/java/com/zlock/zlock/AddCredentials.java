package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zlock.zlock.AES.AESUtills;
import com.zlock.zlock.DataModel.Credentials;
import com.zlock.zlock.DataModel.User;
import com.zlock.zlock.Helper.CreHelper;

public class AddCredentials extends AppCompatActivity {

    EditText accountName, Username, Password, C_pass;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credentials);

        accountName = findViewById(R.id.account);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        addBtn = findViewById(R.id.addCreBtn);
        C_pass = findViewById(R.id.c_password);

        //button for adding credentials
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking for all the required fields
                if (accountName.getText().toString().trim().isEmpty()){
                    accountName.setError("Required");
                    accountName.requestFocus();
                    return;
                }
                if (Username.getText().toString().trim().isEmpty()){
                    Username.setError("Required");
                    Username.requestFocus();
                    return;
                }
                if (Password.getText().toString().trim().isEmpty()){
                    Password.setError("Required");
                    Password.requestFocus();
                    return;
                }
                if (C_pass.getText().toString().trim().isEmpty()){
                    C_pass.setError("Required");
                    C_pass.requestFocus();
                    return;
                }
                if (!Password.getText().toString().trim().equals(C_pass.getText().toString().trim())){
                    C_pass.setError("Password Should Match");
                    C_pass.requestFocus();
                    return;
                }

                //crehelper is a class to deal with credentials table.
                //in sqlite we have use different table to store of user and credentials.
                //UserHelper class is for Users Table and CreHelper is for Credentials
                CreHelper creHelper = new CreHelper(AddCredentials.this);
                Credentials credentials = new Credentials();

                credentials.setId(-1);
                credentials.setAccount(accountName.getText().toString());
                credentials.setUsername(Username.getText().toString());
                credentials.setUser_id(Integer.parseInt(isAlreadyLogin()));
                try {
                    //encrypting pass using AES algorithem
                    credentials.setPassword(AESUtills.encrypt(Password.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddCredentials.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (creHelper.insert(credentials)){
                    Toast.makeText(AddCredentials.this, "Credentials Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddCredentials.this, "Sorry..! Some error has occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public String isAlreadyLogin(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("user_id", "");
    }
}