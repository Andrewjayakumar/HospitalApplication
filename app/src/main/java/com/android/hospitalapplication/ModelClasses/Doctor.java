package com.android.hospitalapplication.ModelClasses;

/**
 * Created by Gaurav on 20-12-2017.
 */

public class Doctor extends User {
    private String regId,speciality;

    public Doctor(String name,String address,String age,String gender,String contact,String regId, String speciality) {
        super(name,age,gender,address,"Doctor", contact);
        this.regId = regId;
        this.speciality = speciality;
    }
}
