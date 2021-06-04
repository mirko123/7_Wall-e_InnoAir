package com.example.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        moneyCount = getActivity().findViewById(R.id.textview_money_count);
    }

    void RecalculateMoney() {
        
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