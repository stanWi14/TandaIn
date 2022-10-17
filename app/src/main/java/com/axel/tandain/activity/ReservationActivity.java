package com.axel.tandain.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.axel.tandain.model.Reservation;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import com.axel.tandain.R;
import com.axel.tandain.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReservationActivity extends AppCompatActivity {

    Reservation reservation;
    String date;
    EditText startTime, endTime;
    EditText totalPeople;
    CalendarView calendarView;
    Button reserveBtn;

//    TextView myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

//        myDate = findViewById(R.id.myDate);
        calendarView = findViewById(R.id.calendarView);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        totalPeople = findViewById(R.id.totalPeople);
        reserveBtn = findViewById(R.id.checkoutBtn);

        startTime.setInputType(InputType.TYPE_NULL);
        endTime.setInputType(InputType.TYPE_NULL);

        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(calendarView.getDate());
//        myDate.setText(date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if(month < 10){
                    date = dayOfMonth + "-0" + (month+1) + "-" + year;
                }else{
                    date = dayOfMonth + "-" + (month+1) + "-" + year;
                }

//                myDate.setText(date);
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(startTime);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(endTime);
            }
        });

        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveOrder();
            }
        });
    }

    private void reserveOrder() {
        String txt_date = date;
        String txt_entryHour = startTime.getText().toString();
        String txt_exitHour = endTime.getText().toString();
        String numberOfPeople = totalPeople.getText().toString();

        if(txt_entryHour.isEmpty()){
            startTime.setError("Please enter entry hour!");
            startTime.requestFocus();
            return;
        }

        if(txt_exitHour.isEmpty()){
            endTime.setError("Please enter exit hour");
            endTime.requestFocus();
            return;
        }

        if(numberOfPeople.isEmpty()){
            totalPeople.setError("Please enter total people");
            totalPeople.requestFocus();
            return;
        }

        if(numberOfPeople.equals("0")){
            totalPeople.setError("Total people could not be 0!");
            totalPeople.requestFocus();
            return;
        }
        Restaurant restaurant = getIntent().getParcelableExtra("Restaurant");
        reservation = new Reservation(restaurant.getMenu(),txt_date, txt_entryHour,
                txt_exitHour, Integer.parseInt(numberOfPeople),
                FirebaseAuth.getInstance().getCurrentUser().getUid(), "uncooked", restaurant.getName());

        Intent intent = new Intent(ReservationActivity.this, CheckoutActivity.class);
        intent.putExtra("Reservation", reservation);
        intent.putExtra("Restaurant", restaurant);
        startActivity(intent);
    }

    private void showTimeDialog(EditText time) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(ReservationActivity.this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }
}