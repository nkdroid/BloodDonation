package com.nkdroid.blooddonation.model;

/**
 * Created by nirav on 24/04/15.
 */
public class BloodBank {

        public String Name;
public String Address;
public String Lattitude;
public String Longitude;
public String City;
public String Contact;
public String Email;

    public BloodBank(String name, String address, String lattitude, String longitude, String city, String contact, String email) {
        Name = name;
        Address = address;
        Lattitude = lattitude;
        Longitude = longitude;
        City = city;
        Contact = contact;
        Email = email;
    }
}
