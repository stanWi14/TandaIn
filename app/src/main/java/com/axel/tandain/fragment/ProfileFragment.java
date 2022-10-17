package com.axel.tandain.fragment;

import static com.axel.tandain.activity.SigninActivity.currentuserid;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axel.tandain.R;
import com.axel.tandain.activity.TopUpActivity;
import com.axel.tandain.adapter.RestaurantAdapter;
import com.axel.tandain.adapter.StatusAdapter;
import com.axel.tandain.model.Menu;
import com.axel.tandain.model.Reservation;
import com.axel.tandain.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class ProfileFragment extends Fragment implements StatusAdapter.StatusListClickListener {

    User user;
    DatabaseReference databaseReference;
    TextView name, wallet;
    Button topUp;
    RecyclerView recyclerView;
    ArrayList<Reservation> reservations;
    StatusAdapter statusAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        name = view.findViewById(R.id.tvUserName);
        wallet = view.findViewById(R.id.tvWallet);
        topUp = view.findViewById(R.id.btnTopUp);

        reservations = new ArrayList<>();
        getDataFromFirebase();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                name.setText(user.getUsername().toString());
                wallet.setText("Rp " + user.getWallet()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        initRecyclerView(view);

        topUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TopUpActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }

        });

        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        statusAdapter = new StatusAdapter(reservations, this);
        recyclerView.setAdapter(statusAdapter);
    }

    private void getDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Reservations");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ArrayList<Menu> menus = new ArrayList<>();
                    if(dataSnapshot.child("userID").getValue(String.class).equals(currentuserid)){
                        Reservation reservation = new Reservation(
                                menus,
                                dataSnapshot.child("date").getValue().toString(),
                                dataSnapshot.child("entryHour").getValue().toString(),
                                dataSnapshot.child("exitHour").getValue().toString(),
                                Integer.parseInt(dataSnapshot.child("numberOfPeople").getValue().toString()),
                                dataSnapshot.child("userID").getValue().toString(),
                                dataSnapshot.child("status").getValue().toString(),
                                dataSnapshot.child("restaurantName").getValue().toString());
                        for(DataSnapshot dataSnapshot1: dataSnapshot.child("menus").getChildren()){
                            Menu menu = dataSnapshot1.getValue(Menu.class);
                            menus.add(menu);
                            reservation.setMenus(menus);
                            reservation.setResvID(dataSnapshot.getKey());
                        }
                        reservations.add(reservation);
                    }

                }
                statusAdapter.updateData(reservations);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRemoveFromList(Reservation reservation) {
        int totalPrice = 0;
        int user_wallet = user.getWallet();
        for(Menu m: reservation.getMenus()){
            totalPrice = totalPrice + m.getPrice();
        }

        user_wallet = user_wallet + totalPrice;
        wallet.setText("Rp " + user_wallet + "");

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("wallet").setValue(user_wallet);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.hide(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, new ProfileFragment());
        transaction.addToBackStack(null);
        transaction.commit();

    }
}