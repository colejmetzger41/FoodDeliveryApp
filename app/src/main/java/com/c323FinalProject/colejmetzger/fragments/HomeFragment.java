package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c323FinalProject.colejmetzger.R;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.topView, RecentsFragment.class, null)
                .commit();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.bottomView, AllFragment.class, null)
                .commit();

        return v;
    }
}