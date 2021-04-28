package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zlock.zlock.AES.AESUtills;
import com.zlock.zlock.DataModel.Credentials;
import com.zlock.zlock.Helper.CreHelper;

import java.util.ArrayList;
import java.util.List;

public class RetrieveCredentials extends AppCompatActivity {

    EditText account;
    Button search;
    TextView showData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_credentials);

        // binding  buttons and entered text to id
        account = findViewById(R.id.account);
        search = findViewById(R.id.searchBtn);
        showData = findViewById(R.id.showdata);


        // button to search
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getText().toString().trim().isEmpty()){
                    account.setError("Required");
                    account.requestFocus();
                    return;
                }

                // search database for account name
                CreHelper helper = new CreHelper(RetrieveCredentials.this);
                List<Credentials> myList = helper.ReadData();
                List<Credentials> FilteredList = new ArrayList<>();
                for (int i = 0; i < myList.size(); i++){
                    if (myList.get(i).getUser_id() == Integer.parseInt(isAlreadyLogin())){
                        FilteredList.add(myList.get(i));
                    }
                }
                if (FilteredList.size() == 0){
                    Toast.makeText(RetrieveCredentials.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                    //list for account passwords
                for (int i = 0; i < FilteredList.size(); i++){
                    if (FilteredList.get(i).getAccount().equals(account.getText().toString())){
                        String pwd;
                        try {
                            pwd = AESUtills.decrypt(FilteredList.get(i).getPassword());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(RetrieveCredentials.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            showData.setText("");
                            return;
                        }

                        // show user account name and password
                        String result = "Username:    " + FilteredList.get(i).getUsername() + "\n" +
                                "Password:    " + pwd;

                        showData.setText(result);
                    }else{
                        Toast.makeText(RetrieveCredentials.this, "No Credentials Found", Toast.LENGTH_SHORT).show();
                        showData.setText("");
                    }
                }
            }
        });
    }
// retrieve user information of user logged in
    public String isAlreadyLogin(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("user_id", "");
    }
}