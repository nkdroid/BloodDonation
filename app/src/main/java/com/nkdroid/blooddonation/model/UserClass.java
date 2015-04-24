package com.nkdroid.blooddonation.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 20-04-2015.
 */
public class UserClass {
    public String user_id;
    public String name;
    public String dob;
    public String gender;
    public String weight;
    public String contact;
    public String privacy;
    public String email;
    public String address;
    public String city;
    public String area;
    public String bgrp;
    public String password;
    public String donation_status;
    public String hiv;
    public String highbp;
    public String tb;
    public String heartd;
    public String amenia;
    public String req_status;

    public UserClass() {
    }

    public UserClass(String user_id, String name, String dob, String gender, String weight, String contact, String privacy, String email, String address, String city, String area, String bgrp, String password, String donation_status, String hiv, String highbp, String tb, String heartd, String amenia, String req_status) {
        this.user_id = user_id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.weight = weight;
        this.contact = contact;
        this.privacy = privacy;
        this.email = email;
        this.address = address;
        this.city = city;
        this.area = area;
        this.bgrp = bgrp;
        this.password = password;
        this.donation_status = donation_status;
        this.hiv = hiv;
        this.highbp = highbp;
        this.tb = tb;
        this.heartd = heartd;
        this.amenia = amenia;
        this.req_status = req_status;
    }
}
