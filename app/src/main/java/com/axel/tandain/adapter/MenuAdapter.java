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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.myViewHolder> {

    Context ct;
    ArrayList<Menu> menus;
    MenuListClickListener clickListener;

    public MenuAdapter(Context ct, ArrayList<Menu> menus, MenuListClickListener clickListener) {
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
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.menu_item, parent, false);
        return new MenuAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.itemName.setText(menus.get(position).getName());
        holder.itemPrice.setText("Rp "+menus.get(position).getPrice());
        Glide.with(holder.img.getContext())
                .load(menus.get(position).getImg())
                .placeholder(R.drawable.ic_launcher_background)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.addToCart.setOnClickListener(v -> {
            Menu menu = menus.get(position);
            menu.setTotalInCart(1);
            clickListener.onAddToCart(menu);
            holder.addMoreLayout.setVisibility(View.VISIBLE);
            holder.addToCart.setVisibility(View.GONE);
            holder.tvCount.setText(menu.getTotalInCart()+"");
        });

        holder.imgRemove.setOnClickListener(v -> {
            Menu menu = menus.get(position);
            int total = menu.getTotalInCart();
            total--;
            if(total > 0){
                menu.setTotalInCart(total);
                clickListener.onUpdateCartClick(menu);
                holder.tvCount.setText(total+"");
            }else{
                holder.addMoreLayout.setVisibility(View.GONE);
                holder.addToCart.setVisibility(View.VISIBLE);
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
        TextView itemName;
        TextView itemPrice;
        Button addToCart;
        LinearLayout addMoreLayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.itemImg);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            addToCart = itemView.findViewById(R.id.addToCartBtn);
            imgRemove = itemView.findViewById(R.id.imgRemove);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            tvCount = itemView.findViewById(R.id.tvCount);
            addMoreLayout = itemView.findViewById(R.id.addMoreLayout);
        }
    }

    public interface MenuListClickListener{
        public void onAddToCart(Menu menu);
        public void onUpdateCartClick(Menu menu);
        public void onRemoveFromCart(Menu menu);
    }

}
