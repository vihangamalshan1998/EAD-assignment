package com.example.fuel_management_mobile_app.fragment.fuelstation.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuel_management_mobile_app.R;
import com.example.fuel_management_mobile_app.constant.CommonConstant;
import com.example.fuel_management_mobile_app.fragment.users.ManagerProfile;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.SneakyThrows;


public class AddFuel extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    ManagerProfile managerProfile = new ManagerProfile();
    EditText fuelCapacity;
    Button addFuel;

    public AddFuel() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_fuel, container, false);
        fuelCapacity = view.findViewById(R.id.add_liters);
        addFuel = view.findViewById(R.id.add_fuel_btn);
        fuelCapacity = view.findViewById(R.id.add_liters);


        addFuel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                try {
                    addFuel();
                    Toast.makeText(getContext(), "Fuel added", Toast.LENGTH_LONG).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, managerProfile).commit();

                } catch (Exception e) {
                    Log.i("error getting add Fuel to station {} ", e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            final ManagerProfile managerProfile = new ManagerProfile();
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,managerProfile ).commit();
            }
        };

        return view;
    }


    @SuppressLint("LongLogTag")
    public void addFuel() throws JSONException {
        Bundle bundle = getArguments();

        String url = CommonConstant.BASE_URL.concat("FuelDetail/");

        requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonBody = new JSONObject();
        if (bundle != null) {

            jsonBody.put("fuelType", bundle.get("fuelType"));
            jsonBody.put("capacity", fuelCapacity.getText().toString());
            jsonBody.put("isArrival", true);
            jsonBody.put("fuelStationId", bundle.get("stationId"));

        }
        Log.i("Station json body {} ", jsonBody.toString());

        jsonObjectRequest = new JsonObjectRequest(

                Request.Method.POST,
                url,
                jsonBody,

                new Response.Listener<JSONObject>() {

                    @SneakyThrows
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Response is", response.toString());

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Response {} ", error.toString());

            }
        }

        );

        requestQueue.add(jsonObjectRequest);

    }
}