package com.markkuhn.timequest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

public class PlanActivity extends AppCompatActivity {
    private final String TAG = "Chips Example";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        Chip general_chip = findViewById(R.id.general_plan);
        Chip app_chip = findViewById(R.id.app_plan);

        ChipGroup chipgroup = findViewById(R.id.choice_chip_group);

        /*Choice Chip Section*/
        ChipGroup choiceChipGroup = findViewById(R.id.choice_chip_group);
        choiceChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, @IdRes int i) {
                Log.i(TAG, i + "");
                Chip chip = chipGroup.findViewById(i);

                if( chip == chipGroup.findViewById(R.id.general_plan)){
                    Intent intent = new Intent(getApplicationContext(), GeneralPlanActivity.class);
                    startActivity(intent);
                }
                else if(chip == chipGroup.findViewById(R.id.app_plan)){
                    Intent intent = new Intent(getApplicationContext(), AppPlanActivity.class);
                    startActivity(intent);
                }
//                else if(chip == chipGroup.findViewById(R.id.category_plan)){
//                    Intent intent = new Intent(getApplicationContext(), AppPlanActivity.class);
//                    startActivity(intent);
//                }
                else {
                    Toast.makeText(PlanActivity.this, "Invalid Selection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
