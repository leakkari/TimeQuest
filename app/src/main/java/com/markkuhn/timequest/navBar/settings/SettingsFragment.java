package com.markkuhn.timequest.navBar.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.markkuhn.timequest.ChangePwdDialog;
import com.markkuhn.timequest.LoginActivity;
import com.markkuhn.timequest.MainActivity;
import com.markkuhn.timequest.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.settings_frag, container, false);

        Button changePwd = root.findViewById(R.id.change_pwd);

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new ChangePwdDialog();
                newFragment.show(getFragmentManager(), "Change Passwrod");
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
        String userKey = sharedPreferences.getString("userKey", "");
        LoginActivity.DownloadTask task = new LoginActivity.DownloadTask();
        try {
            String result = task.execute("https://timequestapi.herokuapp.com/username?key="+userKey).get();
            if(result.equals("-1") || result.equals("-2") || result.equals("-3")){
                System.out.println("ERROR:"+result);
            } else {
                // Set username in settings tab
                TextView txtU = root.findViewById(R.id.username_get);
                txtU.setText(result);
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR with clickLogin:" + e);
        }

        return root;
    }
}
