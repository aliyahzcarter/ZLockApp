package com.zlock.zlock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GeneratePass extends AppCompatActivity {

    TextView textJson;
    Button generate, copybtn;
    String genPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pass);
        //binding buttons to variables
        textJson = findViewById(R.id.textjson);
        generate = findViewById(R.id.generatebtn);
        copybtn = findViewById(R.id.copybtn);
        new JsonTask().execute("https://passwordwolf.com/api/?length=8&upper=on&lower=on&special=on&repeat=1");

        //button to generate password
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Execute url to password wolf using async task.
                new JsonTask().execute("https://passwordwolf.com/api/?length=8&upper=on&lower=on&special=on&repeat=1");
            }
        });


        // button to copy  password to clipboard
        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genPwd.length() == 8) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("pass", genPwd);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(GeneratePass.this, "Text Copied", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(GeneratePass.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    ProgressDialog pd;

    //implementation of AsyncTask ---> class for doing running tasks in background

    private class JsonTask extends AsyncTask<String, String, String> {

        //on preExecute calls when class is ready to execute the stuff.
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(GeneratePass.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }


        protected String doInBackground(String... params) {

            // create an http connection
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                // http url connection to get data from api
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                //input stream to recieve data
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   // response to api here
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            try {

                //json array to store results of generated password
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                //  json object converted to string ans stored into password field
                genPwd = jsonObject.getString("password");


                //textJson is an editText to show generated password
                textJson.setText(genPwd);
            } catch (JSONException e) {
                e.printStackTrace();

                //display password
                Toast.makeText(GeneratePass.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

}