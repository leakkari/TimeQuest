package com.markkuhn.timequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void clickRegister(View view){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void clickLogin(View view){
        EditText emailTextField = findViewById(R.id.emailEditText);
        String email = emailTextField.getText().toString();
        EditText passwordTextField = findViewById(R.id.passwordEditText);
        String password = passwordTextField.getText().toString();

        LoginActivity.DownloadTask task = new LoginActivity.DownloadTask();
        try {
            String result = task.execute("https://timequestapi.herokuapp.com/login?username="+email+"&password="+password).get();
            if(result.equals("-1")){
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
            } else if(result.equals("-2")) {
                Toast.makeText(this, "Invalid login details", Toast.LENGTH_SHORT).show();
            } else if(result.equals("-3")) {
                Toast.makeText(this, "Error - Please try again later", Toast.LENGTH_SHORT).show();
            } else if(result.equals("Failed")){
                Toast.makeText(this, "Something went wrong: "+result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                // Save user key
                SharedPreferences sharedPreferences = this.getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("userKey", result).apply();

                // Get user key
                String userKey = sharedPreferences.getString("userKey", "");
                Toast.makeText(this, "LoginActivity: userKey:"+userKey, Toast.LENGTH_SHORT).show();

                // Go to home screen
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR with clickLogin:" + e);
        }
    }




    // HTTP GET request from url
    public static class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }
            catch(Exception e) {
                System.out.println("ERROR:"+e);
                return "Failed";
            }
        }
    }


}