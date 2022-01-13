package com.aimerneige.nosese;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class SettingsActivity extends AppCompatActivity {

    static int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        RelativeLayout settingsNoseseMode = findViewById(R.id.settings_nosese_mode);
        CheckBox noseseModeCheckBox = findViewById(R.id.nosese_mode_checkbox);
        settingsNoseseMode.setOnClickListener(v -> {
            // TODO save it into local sp
            noseseModeCheckBox.setChecked(num % 2 == 0);
            num++;
        });
    }
}
