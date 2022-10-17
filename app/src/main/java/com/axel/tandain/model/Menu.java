package com.axel.tandain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {

    private String name, img;
    private int price;
    private int totalInCart;

    public Menu(){

    }

    public Menu(String name, int price, String img) {
        this.name = name;
        this.price = price;
        this.img = img;
    }

    public int getTotalInCart() {
        return totalInCart;
    }

    public void setTotalInCart(int totalInCart) {
        this.totalInCart = totalInCart;
    }

    protected Menu(Parcel in) {
        name = in.readString();
        img = in.readString();
        price = in.readInt();
        totalInCart = in.readInt();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(img);
        dest.writeInt(price);
        dest.writeInt(totalInCart);
    }
}
