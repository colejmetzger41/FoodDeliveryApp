package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.adapters.HomeAdapter;
import com.c323FinalProject.colejmetzger.adapters.RecentAdapter;
import com.c323FinalProject.colejmetzger.types.Restaurant;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;


public class AllFragment extends Fragment {

    Restaurant[] all;
    RecyclerView recyclerView;

    public AllFragment() {
        // Required empty public constructor
    }

    private void getAll() {
        DatabaseHelper db = DatabaseHelper.getInstance();
        all = db.getRestaurants();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getAll();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView = v.findViewById(R.id.all_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter( new RecentAdapter(v.getContext(), all));
        return v;
    }
}