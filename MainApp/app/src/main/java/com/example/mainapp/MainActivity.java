package com.example.mainapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mainapp.cardform.LightThemeActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.mainapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mainapp.write.Common;
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public static TextView moneyCountView;
//    public static String moneyBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.getMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LightThemeActivity.class);
                startActivity(i);
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.to_card_ui);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        Common.mAppContext = getApplicationContext();

        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String moneyBalance = pref.getString("moneyBalance", "0");
//        MainActivity.moneyBalance =

        moneyCountView = ((TextView)findViewById(R.id.textview_balance));
        moneyCountView.setText(moneyBalance);

    }

    public static void modifiBalance(float diff)
    {
        float currentBalance = Float.parseFloat(moneyCountView.getText().toString());
        float newBalance = currentBalance + diff;
        String balanceStr = String.valueOf(newBalance);

        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(Common.mAppContext);

        SharedPreferences.Editor edit = pref.edit();
        edit.putString("moneyBalance", balanceStr);
        edit.commit();

        moneyCountView.setText(balanceStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Common.getPendingComponentName() != null) {
            intent.setComponent(Common.getPendingComponentName());
            startActivity(intent);
        } else {
            int typeCheck = Common.treatAsNewTag(intent, this);
            if (typeCheck == -1 || typeCheck == -2) {
                int b = 2;
            }
        }
    }

}