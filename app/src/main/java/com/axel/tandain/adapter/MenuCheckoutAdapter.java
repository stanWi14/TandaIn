package com.axel.tandain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axel.tandain.R;
import com.axel.tandain.model.Menu;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MenuCheckoutAdapter extends RecyclerView.Adapter<MenuCheckoutAdapter.myViewHolder> {

    Context ct;
    ArrayList<Menu> menus;
    MenuCheckOutListListener clickListener;

    public MenuCheckoutAdapter(Context ct, ArrayList<Menu> menus, MenuCheckOutListListener clickListener) {
        this.ct = ct;
        this.menus = menus;
        this.clickListener = clickListener;
    }

    public void updateData(ArrayList<Menu> menu){
        this.menus = menus;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuCheckoutAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.menu_checkout_row, parent, false);
        return new MenuCheckoutAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuCheckoutAdapter.myViewHolder holder, int position) {
        holder.tvFoodName.setText(menus.get(position).getName());
        holder.tvPrice.setText("Rp "+menus.get(position).getPrice());
        Glide.with(holder.img.getContext())
                .load(menus.get(position).getImg())
                .placeholder(R.drawable.ic_launcher_background)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.tvCount.setText(menus.get(position).getTotalInCart()+"");

        holder.imgRemove.setOnClickListener(v -> {
            Menu menu = menus.get(position);
            int total = menu.getTotalInCart();
            total--;
            if(total > 0){
                menu.setTotalInCart(total);
                clickListener.onUpdateCartClick(menu);
                holder.tvCount.setText(total+"");
            }else{
                holder.itemView.setVisibility(View.GONE);
                menu.setTotalInCart(total);
                clickListener.onRemoveFromCart(menu);
            }
        });

        holder.imgAdd.setOnClickListener(v -> {
            Menu menu = menus.get(position);
            int total = menu.getTotalInCart();
            total++;
            if(total <= 10){
                menu.setTotalInCart(total);
                clickListener.onUpdateCartClick(menu);
                holder.tvCount.setText(total+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView img, imgRemove, imgAdd;
        TextView tvCount;
        TextView tvFoodName;
        TextView tvPrice;
        LinearLayout addRemoveLayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.foodImg);
            tvFoodName = (TextView) itemView.findViewById(R.id.tvFoodName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            imgRemove = itemView.findViewById(R.id.imgRemoveCL);
            imgAdd = itemView.findViewById(R.id.imgAddCL);
            tvCount = itemView.findViewById(R.id.tvCountCL);
            addRemoveLayout = itemView.findViewById(R.id.addRemoveLayout);
        }
    }

    public interface MenuCheckOutListListener{
        public void onUpdateCartClick(Menu menu);
        public void onRemoveFromCart(Menu menu);
    }
}
