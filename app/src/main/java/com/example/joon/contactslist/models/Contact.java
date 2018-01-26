package com.example.joon.contactslist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joon on 07/01/18.
 */

public class Contact implements Parcelable{

    private String name;
    private String phonenumber;
    private String device;
    private String email;
    private String profileimage;

    public Contact(String name, String phonenumber, String device, String email, String profileimage) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.device = device;
        this.email = email;
        this.profileimage = profileimage;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        phonenumber = in.readString();
        device = in.readString();
        email = in.readString();
        profileimage = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", device='" + device + '\'' +
                ", email='" + email + '\'' +
                ", profileimage='" + profileimage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phonenumber);
        parcel.writeString(device);
        parcel.writeString(email);
        parcel.writeString(profileimage);
    }
}
