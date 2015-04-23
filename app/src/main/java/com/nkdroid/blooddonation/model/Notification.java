package com.nkdroid.blooddonation.model;

/**
 * Created by Android on 23-04-2015.
 */
public class Notification {

    public String Title;

    public  String Description;

    public String End_Date;

    public String City;

    public Notification(String title, String description, String end_Date, String city) {
        Title = title;
        Description = description;
        End_Date = end_Date;
        City = city;
    }
}
