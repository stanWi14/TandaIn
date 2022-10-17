package com.axel.tandain.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.axel.tandain.R;
import com.axel.tandain.model.Menu;
import com.axel.tandain.model.Reservation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.grpc.Context;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.myViewHolder> {

    ArrayList<Reservation> reservations;
    StatusListClickListener clickListener;

    public StatusAdapter(ArrayList<Reservation> reservations, StatusListClickListener clickListener) {
        this.reservations = reservations;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent, false);
        return new StatusAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.namaRestaurant.setText(reservations.get(position).getRestaurantName());
        holder.datatanggal.setText(reservations.get(position).getDate());
        holder.datastart.setText(reservations.get(position).getEntryHour());
        holder.dataend.setText(reservations.get(position).getExitHour());
        holder.dataperson.setText(reservations.get(position).getNumberOfPeople()+"");
        holder.dataStatus.setText(reservations.get(position).getStatus());

        holder.cancelOrder.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.namaRestaurant.getContext());
            builder.setTitle("Are you sure?");
            builder.setMessage("Deleted data can't be undo");
            int pos = position;
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase.getInstance().getReference().child("Reservations")
                            .child(reservations.get(pos).getResvID()).removeValue();
                    holder.itemView.setVisibility(View.GONE);
                    clickListener.onRemoveFromList(reservations.get(pos));

                }
            });
            builder.show();
        });

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public void updateData(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView namaRestaurant, datatanggal, datastart, dataend, dataperson, dataStatus;
        Button cancelOrder;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            namaRestaurant = itemView.findViewById(R.id.namaRestaurant);
            datatanggal = itemView.findViewById(R.id.datatanggal);
            datastart = itemView.findViewById(R.id.datastart);
            dataend = itemView.findViewById(R.id.dataend);
            dataperson = itemView.findViewById(R.id.dataperson);
            dataStatus = itemView.findViewById(R.id.dataStatus);
            cancelOrder = itemView.findViewById(R.id.btnCancel);
        }
    }

    public interface StatusListClickListener{
        public void onRemoveFromList(Reservation reservation);
    }

}
