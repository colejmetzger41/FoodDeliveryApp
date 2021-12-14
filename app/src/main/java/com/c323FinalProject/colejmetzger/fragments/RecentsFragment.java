package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.adapters.HomeAdapter;
import com.c323FinalProject.colejmetzger.types.Restaurant;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;


public class RecentsFragment extends Fragment {

    Restaurant[] recents;
    RecyclerView recyclerView;

    public RecentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    private void getRecents() {
        DatabaseHelper db = DatabaseHelper.getInstance();
        recents = db.getRecentRestaurants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getRecents();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recents, container, false);

        // temp for cole to test
        Button myButton = v.findViewById(R.id.to_order_test);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.flContent, OrderFragment.class, null)
                        .commit();
            }
        });

        recyclerView = v.findViewById(R.id.recent_restaurants_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter( new HomeAdapter(getContext(), recents));
        return v;
    }
}