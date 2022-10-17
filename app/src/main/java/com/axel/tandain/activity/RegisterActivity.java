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
import com.axel.tandain.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private EditText username;
    private EditText phoneNo;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailaddText);
        password = (EditText) findViewById(R.id.passText);
        username = (EditText) findViewById(R.id.usernameText);
        phoneNo = (EditText) findViewById(R.id.phoneText);
        register = (Button) findViewById(R.id.registerBtn);

        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String txt_email = email.getText().toString().trim();
        String txt_password = password.getText().toString().trim();
        String txt_username = username.getText().toString().trim();
        String txt_phoneNo = phoneNo.getText().toString().trim();

        if(txt_email.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }
        if(txt_password.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(txt_password.length() < 6){
            password.setError("Min password length is 6 characters!");
            password.requestFocus();
            return;
        }

        if(txt_username.isEmpty()){
            username.setError("Username is required");
            username.requestFocus();
            return;
        }

        if(txt_phoneNo.isEmpty()){
            phoneNo.setError("Phone Number is required");
            phoneNo.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(txt_username, txt_email, txt_phoneNo, 0);

                    FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://tandain-e0472-default-rtdb.firebaseio.com/")
                            .child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registration successfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, SigninActivity.class));
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}