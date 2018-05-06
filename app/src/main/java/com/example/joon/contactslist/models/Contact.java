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
    private String note;
    private String profileimage;
    private String tabcolour;

    public Contact(String name, String phonenumber, String device, String note, String profileimage, String tabcolour) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.device = device;
        this.note = note;
        this.profileimage = profileimage;
        this.tabcolour = tabcolour;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        phonenumber = in.readString();
        device = in.readString();
        note = in.readString();
        profileimage = in.readString();
        tabcolour = in.readString();
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
    public String getTabcolour() {
        return tabcolour;
    }
    public void setTabcolour(String tabcolour){
        this.tabcolour = tabcolour;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", device='" + device + '\'' +
                ", note='" + note + '\'' +
                ", profileimage='" + profileimage + '\'' +
                ", tabcolour='" + tabcolour + '\'' +
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
        parcel.writeString(note);
        parcel.writeString(profileimage);
        parcel.writeString(tabcolour);
    }
}
