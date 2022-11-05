package com.example.fuel_management_mobile_app.fragment.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fuel_management_mobile_app.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {

    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fuelMaster", Context.MODE_PRIVATE);
       String userRole = sharedPreferences.getString("userRole","");
        String userId = sharedPreferences.getString("userId","");
        System.out.println("=========================");
        System.out.println(userId);
        System.out.println(userRole);
        Bundle bundle = getArguments();
        System.out.println(bundle);
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }
}