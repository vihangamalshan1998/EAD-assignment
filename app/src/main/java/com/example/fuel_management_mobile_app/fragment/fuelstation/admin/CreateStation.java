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

public class CreateStation extends Fragment {


    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private EditText fuelStationName, location, openTime, closeTime;
    private Button createStation;
    int userId = 1;

    public CreateStation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_station, container, false);
        fuelStationName = view.findViewById(R.id.create_station_name_id);
        location = view.findViewById(R.id.create_station_location_id);
        openTime = view.findViewById(R.id.create_station_opent_time_id);
        closeTime = view.findViewById(R.id.create_station_close_time_id);
        createStation = view.findViewById(R.id.create_station_btn);

        createStation.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                final ManagerProfile managerProfile = new ManagerProfile();
                try {
                    createFuelStation();
                    Toast.makeText(getContext(), "Station Created", Toast.LENGTH_LONG).show();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, managerProfile).commit();

                } catch (Exception exception) {

                    exception.printStackTrace();
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

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }


    @SuppressLint("LongLogTag")
    public void createFuelStation() throws JSONException {
        ManagerProfile managerProfile = new ManagerProfile();

        String url = CommonConstant.BASE_URL.concat("FuelStation/");

        requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonBody = new JSONObject();

        jsonBody.put("fuelStationName", fuelStationName.getText().toString());
        jsonBody.put("location", location.getText().toString());
        jsonBody.put("opentime", openTime.getText().toString());
        jsonBody.put("closetime", closeTime.getText().toString());
        jsonBody.put("userId", userId);

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
                Toast.makeText(getContext(), "Station Created", Toast.LENGTH_LONG).show();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, managerProfile).commit();
                Log.e("Response {} ", error.toString());

            }
        }

        );

        requestQueue.add(jsonObjectRequest);

    }
}