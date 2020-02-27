package com.markkuhn.timequest;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.w3c.dom.Text;

import java.util.Map;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Map<String, UsageStats> aUsageStatsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nav bar
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.ring_frag,R.id.badges_frag, R.id.history_frag,R.id.settings_frag).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_ring);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Check if user is logged in
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
        String userKey = sharedPreferences.getString("userKey", "");
        Toast.makeText(this, "MainActivity: userKey:["+userKey+"]", Toast.LENGTH_SHORT).show();
        if(userKey.equals("")){
            // If not logged in goto LoginActivity
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "User already logged in", Toast.LENGTH_SHORT).show();
        }
    }
    public void clickLogout(View view){
        // Remove userKey
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("userKey", "").apply();
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        // Go to login screen
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    public void saveNewGoalTime(View view){
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.markkuhn.timequest", Context.MODE_PRIVATE);

        String oldTime = sharedPreferences.getString("timeGoal", "");

        EditText newTime = findViewById(R.id.goalTextInput);
        sharedPreferences.edit().putString("timeGoal", newTime.getText().toString()).apply();
        Toast.makeText(this, "Goal changed from "+ oldTime +" to " + newTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}