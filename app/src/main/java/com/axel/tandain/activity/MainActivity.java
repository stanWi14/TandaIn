package com.axel.tandain.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.axel.tandain.R;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progbar);
        int currentProgress = 0;

        for(int i = 0; i<100 ; i++ ){
            currentProgress++;
            ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
                    .setDuration(20000).start();
            if(currentProgress == 100){
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        }
    }
}