package com.axel.tandain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Restaurant implements Parcelable {

    private String name, phoneNo, email, address, openHour, closeHour, rurl, id;
    private int numberOfTable;
    private ArrayList<Menu> menu;
//    private Menu menu;

    Restaurant() {

    }

    public Restaurant(String name, String phoneNo, String email, String address, int numberOfTable, String openHour, String closeHour, String rurl, String id, ArrayList<Menu> menu) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.address = address;
        this.numberOfTable = numberOfTable;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.rurl = rurl;
        this.id = id;
        this.menu = menu;
    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        phoneNo = in.readString();
        email = in.readString();
        address = in.readString();
        openHour = in.readString();
        closeHour = in.readString();
        rurl = in.readString();
        id = in.readString();
        numberOfTable = in.readInt();
        menu = in.createTypedArrayList(Menu.CREATOR);
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberOfTable() {
        return numberOfTable;
    }

    public void setNumberOfTable(int numberOfTable) {
        this.numberOfTable = numberOfTable;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }

    public String getRurl() {
        return rurl;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNo);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(openHour);
        dest.writeString(closeHour);
        dest.writeString(rurl);
        dest.writeString(id);
        dest.writeInt(numberOfTable);
        dest.writeTypedList(menu);
    }
}
