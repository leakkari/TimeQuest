package com.markkuhn.timequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view){
        EditText fullNameText = findViewById(R.id.fullNameEditText);
        String fullName = fullNameText.getText().toString();

        EditText emailTextField = findViewById(R.id.email_register);
        String email = emailTextField.getText().toString();

        EditText passwordTextField = findViewById(R.id.pwd_register);
        String password = passwordTextField.getText().toString();

        RegisterActivity.DownloadTask task = new RegisterActivity.DownloadTask();
        try {
            String result = task.execute("https://timequestapi.herokuapp.com/register?fullName=" + fullName + "&username=" + email + "&password=" + password).get();
            if(result.equals("-1")){
                Toast.makeText(this, "Error with request", Toast.LENGTH_SHORT).show();
            } else if(result.equals("-3")){
                Toast.makeText(this, "User Already Exists", Toast.LENGTH_SHORT).show();
            } else if(result.equals("Failed")){
                Toast.makeText(this, "Something went wrong: "+result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Successfully registered - Key:[" + result + "]", Toast.LENGTH_SHORT).show();

                // Save user key
                SharedPreferences sharedPreferences = this.getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("userKey", result).apply();

                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), PlanActivity.class);
                startActivity(intent);
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR with register:" + e);
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
