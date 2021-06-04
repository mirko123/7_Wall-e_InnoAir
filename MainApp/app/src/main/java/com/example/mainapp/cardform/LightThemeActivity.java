package com.example.mainapp.cardform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.mainapp.MainActivity;
import com.example.mainapp.R;
import com.example.mainapp.cardform.view.MobileNumberEditText;
import com.example.mainapp.databinding.ActivityMainBinding;
import com.example.mainapp.write.Common;

public class LightThemeActivity extends BaseCardFormActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        findViewById(R.id.procces_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences pref =
//                        PreferenceManager.getDefaultSharedPreferences(Common.mAppContext);
//                String moneyBalance = pref.getString("moneyBalance", "0");
//
//                Float currentMoney = Float.parseFloat(moneyBalance);
//                Float addedMoney =  Float.parseFloat(((MobileNumberEditText)findViewById(R.id.bt_card_form_mobile_number)).getMobileNumber());
//                Float sum = currentMoney + addedMoney;
//                String sumStr = String.valueOf(sum);
////                MainActivity.moneyBalance = sumStr;
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putString("moneyBalance", sumStr);
//                edit.commit();
//
////                TextView moneyCountView = ((TextView)findViewById(R.id.textview_balance));
//                MainActivity.moneyCountView.setText(sumStr);
                Float addedMoney =  Float.parseFloat(((MobileNumberEditText)findViewById(R.id.bt_card_form_mobile_number)).getMobileNumber());
                MainActivity.modifiBalance(addedMoney);

                Intent i = new Intent(LightThemeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
