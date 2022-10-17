package com.axel.tandain.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.axel.tandain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private Button reset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.emailForgotPassText);
        reset = (Button) findViewById(R.id.resetPassBtn);
        mAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetPassBtn:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        String txt_email = email.getText().toString().trim();

        if(txt_email.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Check your email to reset your password", Toast.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(ForgotPasswordActivity.this, SigninActivity.class));
                }else{
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Try again! Something wrong happened", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}