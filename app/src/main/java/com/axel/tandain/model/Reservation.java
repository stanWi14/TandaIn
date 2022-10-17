package com.axel.tandain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Vector;

public class Reservation implements Parcelable {

    ArrayList<Menu> menus;
    String date;
    String entryHour;
    String exitHour;
    int numberOfPeople;
    String status;
    String userID;
    String restaurantName;
    String resvID;

    public Reservation(ArrayList<Menu> menus, String date, String entryHour, String exitHour, int numberOfPeople, String userID, String status, String restaurantName) {
        this.menus = menus;
        this.date = date;
        this.entryHour = entryHour;
        this.exitHour = exitHour;
        this.numberOfPeople = numberOfPeople;
        this.userID = userID;
        this.status = status;
        this.restaurantName = restaurantName;
    }

    public String getResvID() {
        return resvID;
    }

    public void setResvID(String resvID) {
        this.resvID = resvID;
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<Menu> menus) {
        this.menus = menus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntryHour() {
        return entryHour;
    }

    public void setEntryHour(String entryHour) {
        this.entryHour = entryHour;
    }

    public String getExitHour() {
        return exitHour;
    }

    public void setExitHour(String exitHour) {
        this.exitHour = exitHour;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    protected Reservation(Parcel in) {
        menus = in.createTypedArrayList(Menu.CREATOR);
        date = in.readString();
        entryHour = in.readString();
        exitHour = in.readString();
        numberOfPeople = in.readInt();
        status = in.readString();
        userID = in.readString();
        restaurantName = in.readString();
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(menus);
        dest.writeString(date);
        dest.writeString(entryHour);
        dest.writeString(exitHour);
        dest.writeInt(numberOfPeople);
        dest.writeString(status);
        dest.writeString(userID);
        dest.writeString(restaurantName);
    }
}
