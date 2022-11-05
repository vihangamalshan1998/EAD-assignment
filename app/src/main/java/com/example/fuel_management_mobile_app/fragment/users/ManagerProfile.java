package com.example.fuel_management_mobile_app.fragment.users;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuel_management_mobile_app.R;
import com.example.fuel_management_mobile_app.adapter.StationManagerAdapter;
import com.example.fuel_management_mobile_app.constant.CommonConstant;
import com.example.fuel_management_mobile_app.fragment.fuelstation.FuelStation;
import com.example.fuel_management_mobile_app.fragment.fuelstation.admin.CreateStation;
import com.example.fuel_management_mobile_app.model.Station;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;


public class ManagerProfile extends Fragment {

    StationManagerAdapter fuelStationAdapter;
    RequestQueue requestQueue;
    private final List<Station> fuelStations = new ArrayList<>();
    RecyclerView recyclerView;
    Button dieselButton, petrolButton,createFuelStation;
    CreateStation createStation = new CreateStation();



    public ManagerProfile() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getStations();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maneger_profile, container, false);
        recyclerView = view.findViewById(R.id.fuel_stations_manager_recycle_view);
        dieselButton = view.findViewById(R.id.fuel_diesel_btn);
        petrolButton = view.findViewById(R.id.fuel_petrol_btn);
        createFuelStation = view.findViewById(R.id.create_fuel_station);
        fuelStationAdapter = new StationManagerAdapter(fuelStations, getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(fuelStationAdapter);

        Bundle bundle = getArguments();
        System.out.println(bundle);

        createFuelStation.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, createStation).commit();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            final FuelStation fuelStation = new FuelStation();
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,fuelStation ).commit();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    private void getStations() {
        String url = CommonConstant.BASE_URL.concat("FuelStation/").concat("1/").concat("getRelatedFuelStations");
        System.out.println(url);
        //RequestQueue initialized
        requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(

                Request.Method.GET,
                url,
                null,

                new Response.Listener<JSONArray>() {

                    @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                    @SneakyThrows
                    @Override
                    public void onResponse(JSONArray stations) {

                        Log.i("Response {}", stations.toString());

                        fuelStations.clear();

                        for (int i = 0; i < stations.length(); i++) {

                            try {

                                Station station = new Station();
                                JSONObject object = stations.getJSONObject(i);

                                station.setFuelStationId(object.getInt("fuelStationId"));
                                station.setFuelStationName(object.getString("fuelStationName"));
                                station.setLocation(object.getString("location"));
                                station.setOpentime(object.getString("opentime"));
                                station.setClosetime(object.getString("closetime"));

                                fuelStations.add(station);

                            } catch (Exception exception) {

                                exception.printStackTrace();
                            }

                        }
                        Log.i("filtered station response {}", fuelStations.toString());

                        fuelStationAdapter.notifyDataSetChanged();
                    }

                }, (Response.ErrorListener) error -> Log.e("Response", error.toString())

        );
        requestQueue.add(jsonArrayRequest);

    }
}