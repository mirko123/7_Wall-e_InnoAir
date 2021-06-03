package com.example.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

//import com.example.mainapp.databinding.FragmentBuyTicketBinding;
import com.example.mainapp.cardform.LightThemeActivity;
import com.example.mainapp.databinding.FragmentChargeCardBinding;

public class ChargeCardFragment extends Fragment {

    private FragmentChargeCardBinding binding;

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

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChargeCardFragment.this)
                        .navigate(R.id.action_ChargeCardFragment_to_FirstFragment);

//                NavHostFragment.findNavController(ChargeCardFragment.this)
//                        .navigate(R.id.action_ChargeCardFragment_to_LightThemeActivity);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}