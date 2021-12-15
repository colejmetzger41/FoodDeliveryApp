package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c323FinalProject.colejmetzger.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentOrdersFragment#} factory method to
 * create an instance of this fragment.
 */
public class RecentOrdersFragment extends Fragment {

    View view;
    RecyclerView recyclerView;

    public RecentOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recent_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRecentOrders);



        return view;
    }
}