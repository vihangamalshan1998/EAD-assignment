package com.example.fuel_management_mobile_app.fragment.fuelstation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuel_management_mobile_app.R;
import com.example.fuel_management_mobile_app.constant.CommonConstant;
import com.example.fuel_management_mobile_app.fragment.fuelstation.admin.AddFuel;
import com.example.fuel_management_mobile_app.model.FuelDetail;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.SneakyThrows;

public class FuelDetails extends Fragment {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private StringRequest stringRequest;
    private FuelDetail fuelDetail = new FuelDetail();
    TextView capacity, totalAvailable, inQueueCount;
    Button addFuel, addQueue, removeBeforeQueue, removeAfterQueue;
    AddFuel addFuelObject = new AddFuel();


    public FuelDetails() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelDetail = getStationDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fuel_details, container, false);
        capacity = view.findViewById(R.id.fuel_liters_value);
        totalAvailable = view.findViewById(R.id.fuel_recived_queue_value);
        inQueueCount = view.findViewById(R.id.fuel_queue_remaning_value);


        capacity.setText(String.valueOf(fuelDetail.getCapacity()));
        totalAvailable.setText(String.valueOf(fuelDetail.getTotalAvailable()));
        inQueueCount.setText(String.valueOf(fuelDetail.getInQueueCount()));
        System.out.println(fuelDetail);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fuelMaster", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "");
        String userId = sharedPreferences.getString("userId", "");
        System.out.println(userId);
        System.out.println(userRole);

        addFuel = view.findViewById(R.id.fuel_add_to_fuel);
        addQueue = view.findViewById(R.id.fuel_add_to_queue);
        removeBeforeQueue = view.findViewById(R.id.fuel_remove_queue);
        removeAfterQueue = view.findViewById(R.id.fuel_finished);

        Bundle bundle = getArguments();
        if (userRole.equals("ADMIN")) {
            addQueue.setVisibility(View.GONE);
            removeBeforeQueue.setVisibility(View.GONE);
            removeAfterQueue.setVisibility(View.GONE);
        } else if (userRole.equals("USER")) {
            addFuel.setVisibility(View.GONE);
        }

        addFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFuelObject.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, addFuelObject).commit();
            }
        });
        addQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    createQueue("IntheQueue");
                    Toast.makeText(getContext(), "Added to the Queue", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        removeBeforeQueue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateQueueDetails("RemoveFromMiddle");
                Toast.makeText(getContext(), "Remove from the queue", Toast.LENGTH_LONG).show();

            }
        });
        removeAfterQueue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateQueueDetails("Finished");
                Toast.makeText(getContext(), "Successfully finished the process", Toast.LENGTH_LONG).show();
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
    //get fuel details
    private FuelDetail getStationDetails() {
        FuelDetail fuelDetail = new FuelDetail();
        String url = null;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fuelMaster", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "");
        String userId = sharedPreferences.getString("userId", "");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String fuelType = bundle.getString("fuelType");
            String fuelId = bundle.getString("stationId");

            if (userRole.equals("ADMIN")) {
                url = CommonConstant.BASE_URL.concat("FuelDetail/").concat(fuelType).concat("/").concat(fuelId);
                System.out.println(url);
            } else if (userRole.equals("USER")) {
                url = CommonConstant.BASE_URL.concat("QueueDetail/").concat(userId).concat("/").concat(fuelType).concat("/").concat(fuelId).concat("/getDetails");
                System.out.println(url);
            }

        }

        //RequestQueue initialized
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequest = new JsonObjectRequest(

                Request.Method.GET,
                url,
                null,

                new Response.Listener<JSONObject>() {

                    @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                    @SneakyThrows
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        Log.i("Response {}", jsonObject.toString());

                        try {
                            capacity.setText(String.valueOf(jsonObject.getInt("Capacity")));
                            totalAvailable.setText(String.valueOf(jsonObject.getInt("Total_Available")));
                            inQueueCount.setText(String.valueOf(jsonObject.getInt("In_Queue_count")));
                            int status = jsonObject.getInt("current_status");
                            if (status == 0) {
                                addQueue.setVisibility(View.GONE);
                            } else {
                                removeBeforeQueue.setVisibility(View.GONE);
                                removeAfterQueue.setVisibility(View.GONE);
                            }


                        } catch (Exception exception) {

                            exception.printStackTrace();
                        }

                        Log.i("filtered station details response {}", jsonObject.toString());

                    }

                }, (Response.ErrorListener) error -> Log.e("Response", error.toString())

        );

        requestQueue.add(jsonObjectRequest);
        return fuelDetail;

    }
    //update record
    private void updateQueueDetails(String status) {
        System.out.println(status);
        String url = null;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fuelMaster", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String fuelType = bundle.getString("fuelType");
            String fuelId = bundle.getString("stationId");

            url = CommonConstant.BASE_URL.concat("QueueDetail/").concat(userId).concat("/").concat(status).concat("/").concat(fuelType).concat("/").concat(fuelId);
            System.out.println(url);

        }

        //RequestQueue initialized
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequest = new JsonObjectRequest(

                Request.Method.PUT,
                url,
                null,

                new Response.Listener<JSONObject>() {

                    @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                    @SneakyThrows
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        Log.i("Response {}", jsonObject.toString());

                        try {
                            capacity.setText(String.valueOf(jsonObject.getInt("Capacity")));
                            totalAvailable.setText(String.valueOf(jsonObject.getInt("Total_Available")));
                            inQueueCount.setText(String.valueOf(jsonObject.getInt("In_Queue_count")));
                            int status = jsonObject.getInt("current_status");
                            addQueue.setVisibility(View.VISIBLE);
                            removeBeforeQueue.setVisibility(View.VISIBLE);
                            removeAfterQueue.setVisibility(View.VISIBLE);
                            if (status == 0) {
                                addQueue.setVisibility(View.GONE);
                            } else {
                                removeBeforeQueue.setVisibility(View.GONE);
                                removeAfterQueue.setVisibility(View.GONE);
                            }


                        } catch (Exception exception) {

                            exception.printStackTrace();
                        }

                        Log.i("filtered station details response {}", jsonObject.toString());

                    }

                }, (Response.ErrorListener) error -> Log.e("Response", error.toString())

        );

        requestQueue.add(jsonObjectRequest);

    }
    //create a record
    @SuppressLint("LongLogTag")
    public void createQueue(String status) throws JSONException {
        String url = null;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fuelMaster", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String fuelType = bundle.getString("fuelType");
            String fuelId = bundle.getString("stationId");

            url = CommonConstant.BASE_URL.concat("QueueDetail/");
            System.out.println(url);

            requestQueue = Volley.newRequestQueue(getContext());

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("vehicleType", "Car");
            jsonBody.put("fuelType", fuelType);
            jsonBody.put("status", status);
            jsonBody.put("fuelStationId", fuelId);
            jsonBody.put("userId", userId);

            Log.i("Station json body {} ", jsonBody.toString());

            jsonObjectRequest = new JsonObjectRequest(

                    Request.Method.POST,
                    url,
                    jsonBody,

                    new Response.Listener<JSONObject>() {

                        @SneakyThrows
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            Log.i("Response is", jsonObject.toString());
                            try {
                                capacity.setText(String.valueOf(jsonObject.getInt("Capacity")));
                                totalAvailable.setText(String.valueOf(jsonObject.getInt("Total_Available")));
                                inQueueCount.setText(String.valueOf(jsonObject.getInt("In_Queue_count")));
                                int status = jsonObject.getInt("current_status");
                                addQueue.setVisibility(View.VISIBLE);
                                removeBeforeQueue.setVisibility(View.VISIBLE);
                                removeAfterQueue.setVisibility(View.VISIBLE);
                                if (status == 0) {
                                    addQueue.setVisibility(View.GONE);
                                } else {
                                    removeBeforeQueue.setVisibility(View.GONE);
                                    removeAfterQueue.setVisibility(View.GONE);
                                }


                            } catch (Exception exception) {

                                exception.printStackTrace();
                            }
                            Toast.makeText(getContext(), "Queue Created", Toast.LENGTH_LONG).show();

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
}