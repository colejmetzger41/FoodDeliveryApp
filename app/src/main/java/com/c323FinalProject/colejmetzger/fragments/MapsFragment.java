package com.c323FinalProject.colejmetzger.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.c323FinalProject.colejmetzger.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userAddress;
    private String restaurantAddress;

    MapView mapView;
    private GoogleMap mMap;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        if (getArguments() != null) {
            userAddress = getArguments().getString("userAddress");
            restaurantAddress = getArguments().getString("restaurantAddress");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 13);
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                //Add zoom, mylocation, and compass options
                UiSettings settings = mMap.getUiSettings();
                settings.setMyLocationButtonEnabled(true);
                settings.setZoomControlsEnabled(true);
                settings.setCompassEnabled(true);

                //Set markers on restaurant and current location
                Geocoder gc = new Geocoder(getContext());
                try{
                    //Destination setup marker
                    List<Address> addresses = gc.getFromLocationName(restaurantAddress, 1);
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    Marker marker1 = mMap.addMarker(new MarkerOptions().position(latLng).title("Destination Marker"));

                    //Current Location marker
                    List<Address> list = gc.getFromLocationName(userAddress, 1);
                    Address currentAddress = list.get(0);
                    LatLng currentLatLng = new LatLng(currentAddress.getLatitude(), currentAddress.getLongitude());
                    Marker currentLocMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location Marker"));

                    //Get distance between
                    Location destinationLoc = new Location("destination");
                    Location currentAddressLoc = new Location("currentAddress");
                    destinationLoc.setLatitude(address.getLatitude());
                    destinationLoc.setLongitude(address.getLongitude());
                    currentAddressLoc.setLatitude(currentAddress.getLatitude());
                    currentAddressLoc.setLongitude(currentAddress.getLongitude());
                    float distance = currentAddressLoc.distanceTo(destinationLoc);
                    distance = (float) (distance / 1609.34);
                    TextView distanceText = rootView.findViewById(R.id.textViewDistanceOnMap);
                    distanceText.setText("Distance: " + distance + " miles");

                    //Set up polyline
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.add(marker1.getPosition());
                    polylineOptions.add(currentLocMarker.getPosition());
                    polylineOptions.color(Color.RED);
                    mMap.addPolyline(polylineOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 6.0f));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // For dropping a marker at a point on the Map
                //LatLng sydney = new LatLng(-34, 151);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}