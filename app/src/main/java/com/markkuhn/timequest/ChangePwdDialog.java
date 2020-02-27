package com.markkuhn.timequest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.markkuhn.timequest.navBar.settings.SettingsFragment;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;

public class ChangePwdDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.change_pwd_layout, null));


        builder.setTitle("Change Password").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // change password
                        changePwd();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        ChangePwdDialog.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void changePwd(){

        EditText oldPwdEdit = getDialog().findViewById(R.id.old_pwd);
        String oldPwd = oldPwdEdit.getText().toString();
        EditText newpwdEdit = getDialog().findViewById(R.id.new_pwd);
        String password = newpwdEdit.getText().toString();

        ChangePwdDialog.DownloadTask task = new ChangePwdDialog.DownloadTask();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);

        // Get user key
        String userKey = sharedPreferences.getString("userKey", "");

        Toast.makeText(getActivity(), userKey, Toast.LENGTH_SHORT).show();

        try {
            String result = task.execute("https://timequestapi.herokuapp.com/password/edit?key="+ userKey + "&oldPassword=" + oldPwd + "&newPassword=" + password).get();
            if(result.equals("-1")){
                Toast.makeText(getActivity(), "Please enter your old and new password", Toast.LENGTH_SHORT).show();
            } else if(result.equals("-2")) {
                Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
            } else if(result.equals("-3")) {
                Toast.makeText(getActivity(), "Error - Please try again later", Toast.LENGTH_SHORT).show();
            } else if(result.equals("1")){
                Toast.makeText(getActivity(), "Wrong old password", Toast.LENGTH_SHORT).show();
            } else if(result.equals("0")){
                Toast.makeText(getActivity(), "Successfully changed password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Something went wrong["+result+"]", Toast.LENGTH_SHORT).show();
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
