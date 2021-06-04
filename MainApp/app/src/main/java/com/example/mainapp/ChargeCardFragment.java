package com.example.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

//import com.example.mainapp.databinding.FragmentBuyTicketBinding;
import com.example.mainapp.cardform.LightThemeActivity;
import com.example.mainapp.databinding.FragmentChargeCardBinding;
import com.example.mainapp.write.Common;
import com.example.mainapp.write.KeyMapCreator;

public class ChargeCardFragment extends Fragment {

    private FragmentChargeCardBinding binding;
    Spinner durationDropdown;
    Spinner linesDropdown;
    CheckBox decreaseCheckbox;
    TextView moneyCount;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentChargeCardBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyMapCreator keyMapCreator = new KeyMapCreator();
                keyMapCreator.start(getActivity().getIntent());

                float price = RecalculateMoney();
                MainActivity.modifiBalance(-price);

//                NavHostFragment.findNavController(ChargeCardFragment.this)
//                        .navigate(R.id.action_ChargeCardFragment_to_FirstFragment);

//                NavHostFragment.findNavController(ChargeCardFragment.this)
//                        .navigate(R.id.action_ChargeCardFragment_to_LightThemeActivity);
            }
        });

        durationDropdown = getActivity().findViewById(R.id.dropdown_duration);
        String[] durations = new String[]{"Дневна", "Седмична", "Месечна", "Годишна"};
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, durations);
        durationDropdown.setAdapter(durationAdapter);

        linesDropdown = getActivity().findViewById(R.id.dropdown_lines);
        String[] lines = new String[]{"Всички линии", "Метро", "10", "111", "..."};
        ArrayAdapter<String> lineAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lines);
        linesDropdown.setAdapter(lineAdapter);

        decreaseCheckbox = getActivity().findViewById(R.id.checkbox_decrease);
        moneyCount = getActivity().findViewById(R.id.textview_money_count);

        durationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                RecalculateMoney();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                RecalculateMoney();
            }
        });

        linesDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                RecalculateMoney();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                RecalculateMoney();
            }
        });

        decreaseCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                RecalculateMoney();
                if ( isChecked )
                {
                    // perform logic
                }

            }
        });
        RecalculateMoney();
    }

    float RecalculateMoney() {
        int durationIndex = durationDropdown.getSelectedItemPosition();
        int linesIndex = linesDropdown.getSelectedItemPosition();

        float moneys = 0;
        switch (durationIndex) {
            case 0: moneys = 4; break;
            case 1: moneys = 10; break;
            case 2: moneys = 40; break;
            case 3: moneys = 365; break;
        }

        if(linesIndex != 0) {
            moneys = moneys / 2;
        }
        if(decreaseCheckbox.isChecked())
        {
            moneys = moneys / 2;
        }
        String moneyStr = String.valueOf(moneys);
        moneyCount.setText(moneyStr);

        float monewyBalance = Float.parseFloat(MainActivity.moneyCountView.getText().toString());
        if(moneys > monewyBalance) {
            binding.buttonCharge.setEnabled(false);
        } else {
            binding.buttonCharge.setEnabled(true);
        }

        return moneys;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }
}