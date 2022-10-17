package com.axel.tandain.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axel.tandain.R;
import com.axel.tandain.adapter.MenuAdapter;
import com.axel.tandain.model.Menu;
import com.axel.tandain.model.Restaurant;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.MenuListClickListener {

    ArrayList<Menu> menus = null;
    Button reserve;
    MenuAdapter menuAdapter;
    TextView restaurant_name;
    ImageView restaurant_img;
    ArrayList<Menu> itemsInCartList;
    int totalItemInCart = 0;
    TextView totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        totalItem = findViewById(R.id.totalItem);

        Restaurant restaurant = getIntent().getParcelableExtra("Restaurant");
        restaurant_name = findViewById(R.id.restaurantName);
        restaurant_img = findViewById(R.id.imgRestaurant);

        restaurant_name.setText(restaurant.getName().toString());
        Glide.with(restaurant_img.getContext())
                .load(restaurant.getRurl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(restaurant_img);

        menus = restaurant.getMenu();
        initRecyclerView();

        reserve = findViewById(R.id.chooseDateTimeBtn);
        reserve.setOnClickListener(v -> {
            if(itemsInCartList != null && itemsInCartList.size() <= 0){
                Toast.makeText(this, "Please add some items in the cart", Toast.LENGTH_SHORT).show();
                return;
            }
            restaurant.setMenu(itemsInCartList);
            Intent intent = new Intent(MenuActivity.this, ReservationActivity.class);
            intent.putExtra("Restaurant",restaurant);
            startActivityForResult(intent, 1000);
        });

    }

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.rvMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(this, menus, this);
        recyclerView.setAdapter(menuAdapter);
    }

    @Override
    public void onAddToCart(Menu menu) {
        if (itemsInCartList == null){
            itemsInCartList = new ArrayList<>();
        }
        itemsInCartList.add(menu);
        totalItemInCart = 0;

        for( Menu m : itemsInCartList) {
            totalItemInCart = totalItemInCart + m.getTotalInCart();
        }
        totalItem.setText("Total: " + totalItemInCart+ " Item(s)");
    }

    @Override
    public void onUpdateCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)){
            int index = itemsInCartList.indexOf(menu);
            itemsInCartList.remove(index);
            itemsInCartList.add(index, menu);

            totalItemInCart = 0;

            for( Menu m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
            }
            totalItem.setText("Total: " + totalItemInCart+ " Item(s)");
        }
    }

    @Override
    public void onRemoveFromCart(Menu menu) {
        if(itemsInCartList.contains(menu)){
            itemsInCartList.remove(menu);
            totalItemInCart = 0;

            for( Menu m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
            }
            totalItem.setText("Total: " + totalItemInCart+ " Item(s)");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1000 && resultCode == Activity.RESULT_OK){
            finish();
        }
    }
}