package com.android.hospitalapplication.Activities.Patient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.hospitalapplication.R;

/**
 * Created by hp on 24-12-2017.
 */
public class Patient_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
