package com.example.mainapp.cardform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mainapp.MainActivity;
import com.example.mainapp.R;
import com.example.mainapp.databinding.ActivityMainBinding;
import com.example.mainapp.write.Common;

public class LightThemeActivity extends BaseCardFormActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        findViewById(R.id.procces_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LightThemeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
