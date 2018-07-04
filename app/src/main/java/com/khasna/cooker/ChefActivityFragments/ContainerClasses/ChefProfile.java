package com.khasna.cooker.ChefActivityFragments.ContainerClasses;

import android.content.Context;

import com.khasna.cooker.Common.LocationHandler;

/**
 * Created by Sourabh on 13/04/2017.
 */

public class ChefProfile {
    private String fname;
    private String lname;
    private String address;
    private String city;
    private String aptno;
    private String zipcode;
    private String phoneNO;
    private double latitude;
    private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }




    public ChefProfile() {
        fname = "";
        lname = "";
        address = "";
        city = "";
        aptno = "";
        zipcode = "";
        phoneNO = "";
    }

    public ChefProfile(String address, String aptno, String city, String fname,
                        String lname, String phoneNO, String zipcode) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phoneNO = phoneNO;
        this.city = city;
        this.aptno = aptno;
        this.zipcode = zipcode;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setaptno(String aptno) {
        this.aptno = aptno;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getfname() {
        return fname;
    }

    public String getlname() {
        return lname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNO() {
        return phoneNO;
    }

    public String getCity() { return city; }

    public String getaptno() {
        return aptno;
    }

    public String getZipcode() {
        return zipcode;
    }
}
