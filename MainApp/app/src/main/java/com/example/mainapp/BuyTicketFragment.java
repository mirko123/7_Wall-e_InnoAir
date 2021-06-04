package com.example.mainapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mainapp.databinding.FragmentBuyTicketBinding;

public class BuyTicketFragment extends Fragment {

    private FragmentBuyTicketBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentBuyTicketBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonBuy30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BuyTicketFragment.this)
                        .navigate(R.id.action_BuyTicketFragment_to_QrFragment);
            }
        });
        binding.buttonBuy60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BuyTicketFragment.this)
                        .navigate(R.id.action_BuyTicketFragment_to_QrFragment);
            }
        });
        binding.buttonBuy120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BuyTicketFragment.this)
                        .navigate(R.id.action_BuyTicketFragment_to_QrFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}