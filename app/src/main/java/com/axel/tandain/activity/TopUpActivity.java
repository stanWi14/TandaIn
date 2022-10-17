package com.axel.tandain.activity;

import static com.axel.tandain.activity.SigninActivity.currentuserid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.axel.tandain.R;
import com.axel.tandain.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class TopUpActivity extends AppCompatActivity {

    EditText topUpAmount;
    Button topUpBtn;
    int wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        User user = getIntent().getParcelableExtra("User");

        topUpAmount = findViewById(R.id.inputTopUp);
        topUpBtn = findViewById(R.id.topUpBtn);

        topUpBtn.setOnClickListener(v -> {
            String txt_amount = topUpAmount.getText().toString();

            if(txt_amount.isEmpty()){
                topUpBtn.setError("Please input the amount!");
                topUpBtn.requestFocus();
                return;
            }

            wallet = user.getWallet() + Integer.parseInt(txt_amount);

            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("wallet").setValue(wallet);

            finish();

        });
    }
}