package com.axel.tandain.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.axel.tandain.R;
import com.axel.tandain.activity.HomeActivity;
import com.axel.tandain.activity.RestaurantActivity;

public class HomeFragment extends Fragment {

    Button seeMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        seeMore = view.findViewById(R.id.restoListBtn);
        seeMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RestaurantActivity.class);
            startActivity(intent);
        });
        // Inflate the layout for this fragment
        return view;
    }
}