package com.markkuhn.timequest;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GeneralPlanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_plan);
        final Button btn =  findViewById(R.id.submit_btn);
        final EditText txt = findViewById(R.id.time_entered); //TODO only allow numbers
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                // Check permission
                AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
                if(mode == AppOpsManager.MODE_ALLOWED){
                    // Save time entered
                    SharedPreferences sharedPreferences = GeneralPlanActivity.this.getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("timeGoal", txt.getText().toString()).apply();

                    // Next screen
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(GeneralPlanActivity.this, "Please give app permissions to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendToSettingsPermission(View view){
        Intent getUsagePermission = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(getUsagePermission , 1);
    }
}