
package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zlock.zlock.AES.AESUtills;
import com.zlock.zlock.DataModel.User;
import com.zlock.zlock.Helper.UserHelper;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {


    //variables for button and user input
    private EditText Login_Email, LoginPassword;
    private ImageView PassToggle;
    private TextView ForgotPasswordTv;
    private Button LoginButton;
    private TextView SignUpButton;



    // check to see if user is logged in
    @Override
    protected void onStart() {
        super.onStart();

        //here we have saved our login status to shared preferences we are calling isAlready login method which will
        //get login status from shared preferences and this conditions check if yes. it means that user has already
        //logged in. and we are calling it on start of the first activity. so if user is already logged in then
        //simply go to home page.
        if (isAlreadyLogin().equals("yes")){
            Intent it = new Intent(Login.this, HomePage.class);
            startActivity(it);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // binding UI attributes to variables
        Login_Email = findViewById(R.id.loginemail);
        LoginPassword = findViewById(R.id.LoginPassword);
        SignUpButton = findViewById(R.id.createacc);
        LoginButton = findViewById(R.id.loginBtn);
        ForgotPasswordTv = findViewById(R.id.forgotpass);
        PassToggle = findViewById(R.id.show_pass_btn);

        //its a password toggle. which will check if input type is password then convert it to text
        //and if input type is text then change is to password. simply for revealing password.
        PassToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LoginPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    PassToggle.setImageResource(R.drawable.password_show);

                    //Show Password
                    LoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    LoginPassword.setSelection(LoginPassword.getText().length());
                } else {
                    PassToggle.setImageResource(R.drawable.password_hide);
                    //Hide Password
                    LoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    LoginPassword.setSelection(LoginPassword.getText().length());
                }

            }
        });

        // button for login
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //it will check if email is empty or not
                if (Login_Email.getText().toString().trim().isEmpty()){
                    Login_Email.setError("Required");
                    Login_Email.requestFocus();
                    return;
                }
                //check for password is empty or not.
                if (LoginPassword.getText().toString().trim().isEmpty()){
                    LoginPassword.setError("Required");
                    LoginPassword.requestFocus();
                    return;
                }
                //database helper to get all the data of users from  database
                UserHelper helper = new UserHelper(Login.this);
                List<User> myList = new ArrayList<>();
                myList = helper.ReadData();


                // decrypt to check password
                for (int i = 0; i < myList.size(); i ++){
                    String decryptedpass = "";
                    try {
                        decryptedpass = AESUtills.decrypt(myList.get(i).getPassword());
                    }catch (Exception e){
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //it is comparing the entered login and password to all of the indexes of arraylist.
                    //and if found simply save login status and show login successful message.
                    if (myList.get(i).getEmail().equals(Login_Email.getText().toString()) &&
                        decryptedpass.equals(LoginPassword.getText().toString())
                    ){
                        SaveLogin();
                        SaveID(Integer.toString(myList.get(i).getId()));
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(Login.this, HomePage.class);
                        startActivity(it);
                        finish();
                        break;
                    }else{
                        if (i == myList.size() - 1){
                            Toast.makeText(Login.this, "Account not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        // button for sign up
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent it = new Intent(Login.this, CreateAccount.class);
                startActivity(it);

            }
        });

        // button click for forgot password
        ForgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent it = new Intent(Login.this, ForgotPass.class);
                startActivity(it);

            }
        });

    }

    //here is the save login method which will simply save yes as login status.
    public void SaveLogin(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login","yes");
        editor.apply();
    }

    //we are also saving current user's ID. whenever a user is logged in we call this message to save user id to shared preferences.
    public void SaveID(String id){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id",id);
        editor.apply();
    }

    //method to check if user is already logged in. we are caling this method on start of activity.
    public String isAlreadyLogin(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("login", "");
    }

}