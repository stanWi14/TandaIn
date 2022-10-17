package com.axel.tandain.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.axel.tandain.R;
import com.axel.tandain.adapter.MenuCheckoutAdapter;
import com.axel.tandain.model.Menu;
import com.axel.tandain.model.Reservation;
import com.axel.tandain.model.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity implements MenuCheckoutAdapter.MenuCheckOutListListener {

    TextView date, time, duration, people, total;
    int totalPrice = 0;
    int wallet = 0;
    Button reserve;
    Restaurant restaurant;
    Reservation reservation;
    RecyclerView recyclerView;
    ArrayList<Menu> menus;
    MenuCheckoutAdapter menuCheckoutAdapter;
    DatabaseReference databaseReference, databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference2 = FirebaseDatabase.getInstance().getReferenceFromUrl
                ("https://tandain-e0472-default-rtdb.firebaseio.com").child("Reservations");

        checkMyWallet();

        date = findViewById(R.id.tvDate);
        time = findViewById(R.id.tvTime);
        duration = findViewById(R.id.tvDuration);
        people = findViewById(R.id.tvPeople);
        total = findViewById(R.id.tvTotal);
        reserve = findViewById(R.id.btnReserve);

        restaurant = getIntent().getParcelableExtra("Restaurant");
        reservation = getIntent().getParcelableExtra("Reservation");

        date.setText("Date : " + reservation.getDate());
        time.setText("Time : " + reservation.getEntryHour());

        String[] entry = reservation.getEntryHour().split(":");
        int hourEntry = Integer.parseInt(entry[0]) * 60 + Integer.parseInt(entry[1]);
        String[] exit = reservation.getExitHour().split(":");
        int hourExit = Integer.parseInt(exit[0]) * 60 + Integer.parseInt(exit[1]);
        int durasi = hourExit-hourEntry;
        duration.setText("Duration : " + durasi + " minutes");

        people.setText("People : " + reservation.getNumberOfPeople());

        initRecyclerView();
        countTotalPrice();

        reserve.setOnClickListener(v -> {
            if (wallet < totalPrice){
                Toast.makeText(this, "You need to top up your wallet!", Toast.LENGTH_SHORT).show();
                return;
            }


            databaseReference2.push().setValue(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        wallet = wallet - totalPrice;
                        updateMyWallet();
                        Toast.makeText(CheckoutActivity.this, "Reservation successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(CheckoutActivity.this, "Reservation could not be made", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        });

    }

    private void checkMyWallet() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallet = Integer.parseInt(snapshot.child("wallet").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateMyWallet() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("wallet").setValue(wallet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countTotalPrice() {
        totalPrice = 0;
        for (Menu m : menus){
            totalPrice = totalPrice + (m.getTotalInCart()*m.getPrice());
        }
        total.setText("Total : " + totalPrice);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.rvMenuList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        menus = new ArrayList<>();
        menus = reservation.getMenus();

        menuCheckoutAdapter = new MenuCheckoutAdapter(this, menus, this);
        recyclerView.setAdapter(menuCheckoutAdapter);
    }

    @Override
    public void onUpdateCartClick(Menu menu) {
        if(menus.contains(menu)){
            int index = menus.indexOf(menu);
            menus.remove(index);
            menus.add(index, menu);
            countTotalPrice();
        }
        reservation.setMenus(menus);
    }

    @Override
    public void onRemoveFromCart(Menu menu) {
        menus.remove(menu);
        countTotalPrice();
        reservation.setMenus(menus);
    }
}