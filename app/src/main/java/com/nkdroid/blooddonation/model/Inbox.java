package com.nkdroid.blooddonation.model;

/**
 * Created by Android on 24-04-2015.
 */
public class Inbox {

public String Sender_UserId;
public String Required_BloodGroup;
public String City;
public String Area;
public String Date_of_Request;

    public Inbox(String sender_UserId, String required_BloodGroup, String city, String area, String date_of_Request) {
        Sender_UserId = sender_UserId;
        Required_BloodGroup = required_BloodGroup;
        City = city;
        Area = area;
        Date_of_Request = date_of_Request;
    }
}
