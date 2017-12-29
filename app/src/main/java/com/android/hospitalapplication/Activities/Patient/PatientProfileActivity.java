package com.android.hospitalapplication.Activities.Patient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by hp on 24-12-2017.
 */
public class PatientProfileActivity extends AppCompatActivity {

    TextView FnameValue,GenderValue,bloodgrpValue,dobValue,AddressValue,phoneValue,profileDob;
    DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_profile);

        Toolbar toolbar =  findViewById(R.id.profileAppBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("View profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FnameValue=findViewById(R.id.FnameValue);
        GenderValue=findViewById(R.id.genderValue);
        bloodgrpValue=findViewById(R.id.bloodgrpValue);
        AddressValue=findViewById(R.id.AddressValue);
        phoneValue=findViewById(R.id.MobileValue);
        profileDob=findViewById(R.id.profileDob);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchData(uid);
    }

    private void fetchData(String u_id)
    {
        dbrefUsers.child(u_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String fname=dataSnapshot.child("name").getValue().toString();
                String gender=dataSnapshot.child("gender").getValue().toString();
                String bloodgrp=dataSnapshot.child("blood_group").getValue().toString();
                String phone=dataSnapshot.child("phone").getValue().toString();
                String dob=dataSnapshot.child("d_o_b").getValue().toString();
                String Address=dataSnapshot.child("address").getValue().toString();

                if(gender.equals("M"))
                {
                    GenderValue.setText("Male");

                }
                else
                {
                    GenderValue.setText("Female");
                }

                FnameValue.setText(fname);
                profileDob.setText(dob);
                phoneValue.setText(phone);
                bloodgrpValue.setText(bloodgrp);
                AddressValue.setText(Address);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
