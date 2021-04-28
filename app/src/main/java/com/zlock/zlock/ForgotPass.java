package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zlock.zlock.AES.AESUtills;
import com.zlock.zlock.DataModel.User;
import com.zlock.zlock.Helper.UserHelper;
import com.github.florent37.shapeofview.ShapeOfView;

import java.util.List;

public class ForgotPass extends AppCompatActivity {

    EditText email, Password, answer;
    Button resetBtn, searchBtn;
    TextView question;
    ShapeOfView passShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);


        // binding id button on UI to variable
        email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        resetBtn = findViewById(R.id.resetBtn);
        searchBtn = findViewById(R.id.searchBtn);
        passShape = findViewById(R.id.passShape);
        question = findViewById(R.id.questionText);
        answer = findViewById(R.id.answer);


        //button to search database


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }

                // database helper to seatch databse for user credentials 
                UserHelper helper  = new UserHelper(ForgotPass.this);
                List<User> myList = helper.ReadData();
                for (int i = 0; i < myList.size(); i++){
                    if (myList.get(i).getEmail().equals(email.getText().toString())){
                        question.setText(myList.get(i).getQues());
                        passShape.setVisibility(View.VISIBLE);
                        resetBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgotPass.this, "account found", Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        if (i == myList.size() -1){
                            Toast.makeText(ForgotPass.this, "No account found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        //check to see if required text is filled
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }
                if (answer.getText().toString().trim().isEmpty()){
                    answer.setError("Required");
                    answer.requestFocus();
                    return;
                }
                if (Password.getText().toString().trim().isEmpty()){
                    Password.setError("Required");
                    Password.requestFocus();
                    return;
                }
                //database hekper to search databse for question and answer
                UserHelper helper  = new UserHelper(ForgotPass.this);
                List<User> myList = helper.ReadData();

                for (int i = 0; i < myList.size(); i++){
                    if (myList.get(i).getEmail().equals(email.getText().toString())){
                        if (!myList.get(i).getAns().equals(answer.getText().toString())){
                            Toast.makeText(ForgotPass.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        User user = new User();
                        user.setId(myList.get(i).getId());
                        user.setName(myList.get(i).getName());
                        user.setEmail(myList.get(i).getEmail());
                        user.setQues(myList.get(i).getQues());
                        user.setAns(myList.get(i).getAns());

                        try {
                            user.setPassword(AESUtills.encrypt(Password.getText().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotPass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //password update
                        helper.UpdateData(user, myList.get(i).getId());
                        Toast.makeText(ForgotPass.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        if (i == myList.size() -1){
                            Toast.makeText(ForgotPass.this, "No account found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

    }
}