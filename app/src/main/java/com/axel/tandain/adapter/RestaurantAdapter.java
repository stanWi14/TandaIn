package com.axel.tandain.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axel.tandain.R;
import com.axel.tandain.activity.MenuActivity;
import com.axel.tandain.model.Menu;
import com.axel.tandain.model.Restaurant;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.myViewHolder> {

    Context ct;
    ArrayList<Restaurant> restaurants;
    RestaurantListClickListener clickListener;

    public RestaurantAdapter(Context ct, ArrayList<Restaurant> restaurants, RestaurantListClickListener clickListener) {
        this.ct = ct;
        this.restaurants = restaurants;
        this.clickListener = clickListener;
    }

    public void updateData(ArrayList<Restaurant> restaurants){
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.resto_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.myViewHolder holder, int position) {
        holder.nameRestaurant.setText(restaurants.get(position).getName());
        holder.openHours.setText(restaurants.get(position).getOpenHour()+"-"+restaurants.get(position).getCloseHour());
        holder.addressRestaurant.setText(restaurants.get(position).getAddress());
        Glide.with(holder.img.getContext())
                .load(restaurants.get(position).getRurl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.seeMore.setOnClickListener(v -> {
            clickListener.onItemClick(restaurants.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nameRestaurant, addressRestaurant, openHours;
        Button seeMore;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.idImgRest);
            nameRestaurant = (TextView) itemView.findViewById(R.id.idNameRestaurant);
            addressRestaurant = (TextView) itemView.findViewById(R.id.idAddRestaurant);
            openHours = (TextView) itemView.findViewById(R.id.idHours);
            seeMore = (Button) itemView.findViewById(R.id.idBtnSeeMore);


        }


    }

    public interface RestaurantListClickListener {
        public void onItemClick (Restaurant restaurant);
    }

}
