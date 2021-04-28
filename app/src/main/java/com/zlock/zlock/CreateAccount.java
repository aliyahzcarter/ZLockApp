package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zlock.zlock.AES.AESUtills;
import com.zlock.zlock.DataModel.User;
import com.zlock.zlock.Helper.UserHelper;

import java.security.cert.CRLReason;
import java.util.List;

public class CreateAccount extends AppCompatActivity {

    EditText accountName, email, password,confirmPass, ques, ans;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        accountName = findViewById(R.id.accountname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_password);
        ques = findViewById(R.id.securityQues);
        ans = findViewById(R.id.securityQuesAns);

        create = findViewById(R.id.submitBtn);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking if all of fields are filled.
                if (accountName.getText().toString().trim().isEmpty()){
                    accountName.setError("Required");
                    accountName.requestFocus();
                    return;
                }
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }

                //getting all the users from database
                UserHelper userHelper = new UserHelper(CreateAccount.this);
                List<User> emailList = userHelper.ReadData();
                for (int i = 0; i < emailList.size(); i++){
                    //checking if the email is already exist or not
                    if (emailList.get(i).getEmail().equals(email.getText().toString())){
                        email.setError("Account Already Exist.");
                        email.requestFocus();
                        return;
                    }
                }

                if (password.getText().toString().trim().length() < 5){
                    password.setError("5 digit password is required.");
                    password.requestFocus();
                    return;
                }
                if (confirmPass.getText().toString().trim().length() < 5){
                    confirmPass.setError("5 digit password is required.");
                    confirmPass.requestFocus();
                    return;
                }
                if (ques.getText().toString().isEmpty()){
                    ques.setError("Required");
                    ques.requestFocus();
                    return;
                }
                if (ans.getText().toString().isEmpty()){
                    ans.setError("Required");
                    ans.requestFocus();
                    return;
                }

                if (!password.getText().toString().trim().equals(confirmPass.getText().toString().trim())){
                    confirmPass.setError("Password should match.");
                    confirmPass.requestFocus();
                    return;
                }


                //saving data.
                UserHelper helper = new UserHelper(CreateAccount.this);
                User user = new User();
                user.setId(-1);
                user.setName(accountName.getText().toString());
                user.setEmail(email.getText().toString());
                user.setQues(ques.getText().toString());
                user.setAns(ans.getText().toString());


                try {
                    user.setPassword(AESUtills.encrypt(password.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CreateAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (helper.insert(user)){
                    Toast.makeText(CreateAccount.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(CreateAccount.this, "Sorry..! Some error has occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}