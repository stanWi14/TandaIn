package com.axel.tandain.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.axel.tandain.R;
import com.axel.tandain.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        logout = (Button) findViewById(R.id.logOutBtn);

        logout.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://tandain-e0472-default-rtdb.firebaseio.com/")
                .child("Users");
        userID = user.getUid();

        final TextView username = (TextView) findViewById(R.id.userText);
        final TextView email = (TextView) findViewById(R.id.emailprofileText);
        final TextView wallet = (TextView) findViewById(R.id.walletText);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String txt_username = userProfile.getUsername();
                    String txt_email  = userProfile.getEmail();
                    int int_wallet = userProfile.getWallet();

                    username.setText(txt_username);
                    email.setText(txt_email);
                    wallet.setText(int_wallet+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logOutBtn:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, SigninActivity.class));
        }
    }
}