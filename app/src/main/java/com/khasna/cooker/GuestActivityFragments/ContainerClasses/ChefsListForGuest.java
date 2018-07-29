package com.khasna.cooker.GuestActivityFragments.ContainerClasses;


import android.net.Uri;

/**
 * Created by nachiket on 8/27/2017.
 */

public class ChefsListForGuest {

    private String address;
    private String aptno;
    private String city;
    private String fname;
    private String lname;
    private String phoneNO;
    private String zipcode;
    private String uID;
    private String fullName;
    private String fullAddress;
    private Uri uriProfilePic;

    public ChefsListForGuest() {
    }

    public ChefsListForGuest(String address, String aptno, String city, String fname, String lname, String phoneNO, String zipcode) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.city = city;
        this.aptno = aptno;
        this.zipcode = zipcode;
        this.phoneNO = phoneNO;
    }

    public void SetUnknownFields(String uID)
    {
        this.uID = uID;
        this.fullAddress = address + " " + aptno + " " + city + " " + zipcode;
        this.fullName = fname + " " + lname;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getuID() {
        return uID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAptno() {
        return aptno;
    }

    public void setAptno(String aptno) {
        this.aptno = aptno;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhoneNO() {
        return phoneNO;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Uri getUriProfilePic() {
        return uriProfilePic;
    }

    public void setUriProfilePic(Uri uriProfilePic) {
        this.uriProfilePic = uriProfilePic;
    }
}
