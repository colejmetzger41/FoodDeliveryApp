package com.c323FinalProject.colejmetzger.fragments;


import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.adapters.RestaurantOrdersAdapter;
import com.c323FinalProject.colejmetzger.types.Food;
import com.c323FinalProject.colejmetzger.types.Restaurant;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class CheckoutFragment extends Fragment {

    Food[] foods;
    int[] counts;
    RecyclerView recyclerView;
    RestaurantOrdersAdapter adapter;
    Restaurant restaurant;
    LocationManager myLocationManager;
    LocationListener myLocationListener;
    Location currentLocation;



    public CheckoutFragment(Food[] foods, int[] counts, Restaurant restaurant) {
        this.foods = foods;
        this.counts = counts;
        this.restaurant = restaurant;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLocationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setPowerRequirement(Criteria.POWER_LOW);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
        }

        //Set up my location listener with manager.
        currentLocation = myLocationManager.getLastKnownLocation(myLocationManager.getBestProvider(crit, true));
        myLocationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
                Log.d("asdf", "location changed" + String.valueOf(currentLocation.getLatitude()));

            }
        };
        //Get location updates
        locationUpdates();

    }

    public void locationUpdates() {
        myLocationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
//        }
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
//            return;
        } else {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        TextView instructions, address; Button modifyOrder, placeOrder;
        address = v.findViewById(R.id.address_checkout);
        instructions = v.findViewById(R.id.instructions);


        modifyOrder = v.findViewById(R.id.modify_order_button);
        modifyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get foods that aren't null
                int[] oldCounts = adapter.counts;
                ArrayList addedFoods = new ArrayList<Food>();
                ArrayList counts = new ArrayList<Integer>();

                for (int i = 0; i < oldCounts.length; i++) {
                    if (oldCounts[i]!=0) {
                        addedFoods.add(foods[i]);
                        counts.add(oldCounts[i]);
                    }
                }
                int[] arr = counts.stream().mapToInt(i -> (int) i).toArray();

                // go back to restaurant fragment with current parameters
                RestaurantFragment restaurantFragment = new RestaurantFragment(
                        restaurant.getName(),
                        arr,
                        (Food[]) addedFoods.toArray(new Food[0])
                );
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, restaurantFragment, "restaurant")
                        .commit();
            }
        });

        placeOrder = v.findViewById(R.id.order_button);
        // create order in db and then go to Order fragment
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get foods that aren't null
                int[] oldCounts = adapter.counts;
                ArrayList addedFoods = new ArrayList<Food>();
                ArrayList counts = new ArrayList<Integer>();

                for (int i = 0; i < oldCounts.length; i++) {
                    if (oldCounts[i]!=0) {
                        addedFoods.add(foods[i]);
                        counts.add(oldCounts[i]);
                    }
                }

                int[] arr = counts.stream().mapToInt(i -> (int) i).toArray();

                createOrder(
                        (Food[]) addedFoods.toArray(new Food[0]),
                        arr,
                        address.toString(),
                        instructions.toString()
                );

                // TODO: transition to OrderFragment with address to and from in constructor


            }
        });

        recyclerView = v.findViewById(R.id.checkout_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestaurantOrdersAdapter(getContext(), foods, counts);
        recyclerView.setAdapter(adapter);


        return v;
    }

    public void createOrder(Food[] foods, int[] counts, String address, String instructions) {

        // get next order id
        int orderId = DatabaseHelper.getInstance().getNextOrderId();
        int total = 0;
        // add order items to database
        for (int i = 0; i < foods.length; i++) {
            DatabaseHelper.getInstance().insertOrderItem(
                    foods[i].getName(),
                    counts[i],
                    orderId
            );
            total += foods[i].getPrice() * counts[i];
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateTime = dateFormat.format(date);
        String[] splitTime = dateTime.split(" ");
        // add order to db
        DatabaseHelper.getInstance().insertOrder(
                restaurant.getName(),
                address,
                instructions,
                total,
                splitTime[0],
                splitTime[1]
        );


        Log.d("asdf", String.valueOf(currentLocation.getLatitude()));


//        // get dist to restaurant
//        Geocoder geocoder = new Geocoder(getContext());
////        List<Address> userAddress = geocoder.getFromLocation(lat, long, 1);
//        List<Address> restaurantAddress = null;
//        try {
//            restaurantAddress = geocoder.getFromLocationName(restaurant.getLocation(), 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // rando nubmer betweeen
//        Random r = new Random();
//        int rand = r.nextInt(101-5) + 5;



    }


}