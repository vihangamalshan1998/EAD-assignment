package com.example.fuel_management_mobile_app.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fuel_management_mobile_app.R;
import com.example.fuel_management_mobile_app.fragment.dashboard.Dashboard;
import com.example.fuel_management_mobile_app.fragment.fuelstation.FuelStation;
import com.example.fuel_management_mobile_app.fragment.users.ManagerProfile;
import com.example.fuel_management_mobile_app.model.User;
import com.example.fuel_management_mobile_app.sql.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity = MainActivity.this;
    private DatabaseHelper databaseHelper;
    private List<User> listUsers = new ArrayList<>();
    private String emailFromIntent = null;
    private Bundle bundle;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("fuelMaster", MODE_PRIVATE);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        databaseHelper = new DatabaseHelper(activity);
        emailFromIntent = getIntent().getStringExtra("EMAIL");
        System.out.println(emailFromIntent);

        user = databaseHelper.getUserData(emailFromIntent);
        System.out.println(user);

        bundle = new Bundle();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor fuelMaster = sharedPreferences.edit();
        fuelMaster.clear();
        fuelMaster.putString("userRole", user.getUserRole());
        fuelMaster.putString("userId", String.valueOf(user.getId()));
        fuelMaster.apply();



        dashboard.setArguments(bundle);
        bottomNavigationView.setSelectedItemId(R.id.fuel);

    }


    Dashboard dashboard = new Dashboard();
    FuelStation fuelStation = new FuelStation();
    ManagerProfile managerProfile = new ManagerProfile();

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("fuelMaster", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "");
        System.out.println(userRole);
        switch (item.getItemId()) {

            case R.id.fuel:
                if (userRole.equals("USER")) {
                    findViewById(R.id.users).setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fuelStation).commit();
                    return true;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fuelStation).commit();
                return true;

            case R.id.users:
                if (userRole.equals("USER")) {
                    findViewById(R.id.users).setVisibility(View.GONE);
                    return false;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, managerProfile).commit();
                return true;

        }
        return false;
    }

}