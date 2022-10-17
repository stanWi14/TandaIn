package com.axel.tandain.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.axel.tandain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button signIn;
    private TextView createAcc;
    private TextView forgotPass;
    public static String currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);
        signIn = (Button) findViewById(R.id.signInBtn);
        createAcc = (TextView) findViewById(R.id.createText);
        forgotPass = (TextView) findViewById(R.id.forgotText);

        signIn.setOnClickListener(this);
        createAcc.setOnClickListener(this);
        forgotPass.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInBtn:
                signInUser();
                break;
            case R.id.createText:
                startActivity(new Intent(SigninActivity.this, RegisterActivity.class));
                break;
            case R.id.forgotText:
                startActivity(new Intent(SigninActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void signInUser() {
        String txt_email = email.getText().toString().trim();
        String txt_password = password.getText().toString().trim();

        if(txt_email.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
            email.setError("Please enter valid email!");
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

        mAuth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        Toast.makeText(SigninActivity.this, "Sign In succesfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(SigninActivity.this, "Check your spam email to verify account!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SigninActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}