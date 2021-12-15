package com.c323FinalProject.colejmetzger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c323FinalProject.colejmetzger.R;
import com.c323FinalProject.colejmetzger.types.Food;


public class CheckoutFragment extends Fragment {

    Food[] foods;
    int[] counts;



    public CheckoutFragment(Food[] foods, int[] counts) {
        this.foods = foods;
        this.counts = counts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }
}