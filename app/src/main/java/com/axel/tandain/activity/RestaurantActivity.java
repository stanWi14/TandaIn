package com.axel.tandain.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.axel.tandain.R;
import com.axel.tandain.adapter.RestaurantAdapter;
import com.axel.tandain.model.Menu;
import com.axel.tandain.model.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity implements RestaurantAdapter.RestaurantListClickListener {

    RecyclerView recyclerView;
    RestaurantAdapter restaurantAdapter;
    ArrayList<Restaurant> restaurants;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        initRecyclerView();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ArrayList<Menu> menus = new ArrayList<>();
                    Restaurant restaurant = new Restaurant(
                            dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("phoneNo").getValue().toString(),
                            dataSnapshot.child("email").getValue().toString(),
                            dataSnapshot.child("address").getValue().toString(),
                            Integer.valueOf(dataSnapshot.child("numberOfTable").getValue().toString()),
                            dataSnapshot.child("openHour").getValue().toString(),
                            dataSnapshot.child("closeHour").getValue().toString(),
                            dataSnapshot.child("rurl").getValue().toString(),
                            dataSnapshot.child("id").getValue().toString(),
                            menus
                    );
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("menu").getChildren()){
                        Menu menu = dataSnapshot1.getValue(Menu.class);
                        menus.add(menu);
                        restaurant.setMenu(menus);
                    }

                    restaurants.add(restaurant);
                }
                restaurantAdapter.updateData(restaurants);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.rvRest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        restaurants = new ArrayList<>();

        restaurantAdapter = new RestaurantAdapter(this, restaurants, this);
        recyclerView.setAdapter(restaurantAdapter);
    }

    @Override
    public void onItemClick(Restaurant restaurant) {
        Intent intent = new Intent(RestaurantActivity.this, MenuActivity.class);
        intent.putExtra("Restaurant", restaurant);
        startActivity(intent);
    }
}