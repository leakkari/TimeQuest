package com.markkuhn.timequest.navBar.history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.markkuhn.timequest.MainActivity;
import com.markkuhn.timequest.R;
import com.markkuhn.timequest.RegisterActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static android.graphics.Color.WHITE;

public class HistoryFragment extends Fragment {
    private HistoryViewModel historyViewModel;
    DatePicker picker;
    Button btnGet;
    TextView tvw;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        View root = inflater.inflate(R.layout.history_frag, container, false);

        tvw= root.findViewById(R.id.history_text);
        picker = root.findViewById(R.id.datePicker1);
        picker.setBackgroundColor(WHITE);
        btnGet = root.findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterActivity.DownloadTask task = new RegisterActivity.DownloadTask();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
                String userKey = sharedPreferences.getString("userKey", "");
                String date = ""+picker.getDayOfMonth()+(picker.getMonth()+1)+picker.getYear();
                try {
                    String result = task.execute("https://timequestapi.herokuapp.com/score?key="+userKey+"&date="+date).get();
                    if(result.equals("-1")){
                        Toast.makeText(getActivity(), "Please enter a username and password", Toast.LENGTH_SHORT).show();
                    } else if(result.equals("-2")) {
                        Toast.makeText(getActivity(), "Invalid login details", Toast.LENGTH_SHORT).show();
                    } else if(result.equals("-3")) {
                        Toast.makeText(getActivity(), "Error - Please try again later", Toast.LENGTH_SHORT).show();
                    } else if(result.equals("-4")){
                        String resultText = "Achieved: -";
                        tvw.setText(resultText);
                    } else if(result.equals("Failed")){
                        Toast.makeText(getActivity(), "Something went wrong: "+result, Toast.LENGTH_SHORT).show();
                    } else {
                        String resultText = "Achieved: " + result + "%";
                        tvw.setText(resultText);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Error getting calendar data: " + e);
                }
            }
        });

//                historyViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }
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
