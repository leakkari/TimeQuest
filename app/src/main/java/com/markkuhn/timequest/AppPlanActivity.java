package com.markkuhn.timequest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppPlanActivity extends AppCompatActivity {

    ArrayList<String> selectedItems;
    List<String> installedApps;
    List<String> appName_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_plan);

        //create an ArrayList object to store selected items
        selectedItems = new ArrayList<>();
        appName_list = new ArrayList<>();


        final Button btn = findViewById(R.id.letsgo_btn);
        final Button bts_show = findViewById(R.id.btshow);


        installedApps = new ApkInfoExtractor(AppPlanActivity.this).GetAllInstalledApkInfo();
        for (int i=0; i<installedApps.size(); i++){
                    String appName = new ApkInfoExtractor(AppPlanActivity.this).GetAppName(installedApps.get(i));
                    Drawable appImage = new ApkInfoExtractor(AppPlanActivity.this).getAppIconByPackageName(installedApps.get(i));
                    appName_list.add(appName);

                }

        ListView chl = findViewById(R.id.checkable_list);

        //set multiple selection mode
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //supply data items to ListView
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.recycler_view_item,R.id.txt_title,installedApps);
        chl.setAdapter(aa);

        //set OnItemClickListener
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String selectedItem = ((TextView) view).getText().toString();
                if(selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    selectedItems.add(selectedItem); //add selected item to the list of selected items
            }

        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Get user key
                SharedPreferences sharedPreferences = getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
                String userKey = sharedPreferences.getString("userKey", "");

                // Save app list
                String appList = "";
                for(int i=0; i<selectedItems.size(); i++){
                    if(i == selectedItems.size()-1){
                        appList += selectedItems.get(i);
                    } else {
                        appList += selectedItems.get(i) + ",";
                    }
                }
                System.out.println("SENDING APP LIST:[" + appList + "]");

                LoginActivity.DownloadTask task = new LoginActivity.DownloadTask();
                try {
                    String result = task.execute("https://timequestapi.herokuapp.com/apps/add?key=" + userKey + "&apps=" + appList).get();
                    if(result.equals("0")){
                        // Next screen
                        Intent intent = new Intent(getApplicationContext(), GeneralPlanActivity.class);
                        startActivity(intent);
                    } else {
                        System.out.println("ERROR [-1]");
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("ERROR with saving app list:" + e);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void showSelectedItems(View view){
        String selItems="";
        for(String item:selectedItems){
            if(selItems=="")
                selItems=item;
            else
                selItems+="/"+item;
        }
        Toast.makeText(this, selItems, Toast.LENGTH_LONG).show();
    }

    public List<String> selectedApps(){
        return selectedItems;
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



