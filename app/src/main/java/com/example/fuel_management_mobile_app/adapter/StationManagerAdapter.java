package com.example.fuel_management_mobile_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuel_management_mobile_app.R;
import com.example.fuel_management_mobile_app.fragment.fuelstation.FuelDetails;
import com.example.fuel_management_mobile_app.model.Station;

import java.util.List;

public class StationManagerAdapter extends RecyclerView.Adapter<StationManagerAdapter.StationManagerViewHolder>{

    private final List<Station> fuelStations;
    private final Context context;

    public StationManagerAdapter(List<Station> fuelStations, Context context) {
        this.fuelStations = fuelStations;
        this.context = context;
    }

    @NonNull
    @Override
    public StationManagerAdapter.StationManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.single_view_station_manager_layout, parent, false);
        return new StationManagerAdapter.StationManagerViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull StationManagerAdapter.StationManagerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView_1.setText(fuelStations.get(position).getFuelStationName());
        holder.textView_2.setText(fuelStations.get(position).getLocation());
        holder.textView_3.setText(fuelStations.get(position).getOpentime());
        holder.textView_4.setText(fuelStations.get(position).getClosetime());
        holder.textView_5.setText(this.checkStationStatus());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.dieselButton.setOnClickListener(new View.OnClickListener() {


            final FuelDetails fuelDetails = new FuelDetails();

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString("fuelType", "Diesel");
                bundle.putString("stationId", String.valueOf(fuelStations.get(position).getFuelStationId()));
                bundle.putString("role","ADMIN");

                fuelDetails.setArguments(bundle);
                FragmentTransaction mFragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.flFragment, fuelDetails).commit();
            }
        });

        holder.petrolButton.setOnClickListener(new View.OnClickListener() {

            final FuelDetails fuelDetails = new FuelDetails();

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("role","ADMIN");
                bundle.putString("fuelType", "Petrol");
                bundle.putString("stationId", String.valueOf(fuelStations.get(position).getFuelStationId()));
                fuelDetails.setArguments(bundle);
                FragmentTransaction mFragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.flFragment, fuelDetails).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return fuelStations.size();
    }

    public static class StationManagerViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_1, textView_2, textView_3, textView_4, textView_5;
        public Button petrolButton, dieselButton;
        public CardView cardView;

        public StationManagerViewHolder(View itemView) {

            super(itemView);
            this.textView_1 = (TextView) itemView.findViewById(R.id.station_name_id);
            this.textView_2 = (TextView) itemView.findViewById(R.id.station_location_id);
            this.textView_3 = (TextView) itemView.findViewById(R.id.station_open_id);
            this.textView_4 = (TextView) itemView.findViewById(R.id.station_close_id);
            this.textView_5 = (TextView) itemView.findViewById(R.id.station_status);
            this.dieselButton = (Button) itemView.findViewById(R.id.fuel_diesel_btn);
            this.petrolButton = (Button) itemView.findViewById(R.id.fuel_petrol_btn);
            cardView = itemView.findViewById(R.id.fuel_station_manager_list_card_view);

        }
    }

    private String checkStationStatus() {

        return "OPEN";
    }
}
