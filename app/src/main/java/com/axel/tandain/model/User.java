package com.axel.tandain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String username;
    private String email;
    private String phoneNo;
    private int wallet;

    public User(){

    }

    public User(String username, String email, String phoneNo, int wallet) {
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.wallet = wallet;
    }

    protected User(Parcel in) {
        username = in.readString();
        email = in.readString();
        phoneNo = in.readString();
        wallet = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(phoneNo);
        dest.writeInt(wallet);
    }
}
