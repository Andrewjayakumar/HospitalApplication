package com.android.hospitalapplication.Activities.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.android.hospitalapplication.Activities.LoginActivity;
import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class PatientActivity extends AppCompatActivity {

    public ImageButton set_appointement,profile_info,view_prescription,upload_report,dietplan,appointmentStatus;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pat_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient");

        set_appointement=findViewById(R.id.set_appointment);
        profile_info=findViewById(R.id.profile_info);
        upload_report=findViewById(R.id.upload_report);
        view_prescription=findViewById(R.id.view_prescription);
        dietplan=findViewById(R.id.dietplan);
        appointmentStatus=findViewById(R.id.view_status);

        set_appointement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,SetAppointmentActivity.class));
            }
        });
        profile_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PatientActivity.this,Patient_profile.class));
            }
        });
        upload_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,UploadReportActivity.class));

            }
        });
        view_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,ViewPrescriptionActivity.class));
            }
        });
        dietplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,DietPlanActivity.class));

            }
        });

        appointmentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,AppointmentStatusActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sign_out :
                auth.signOut();
                startActivity(new Intent(PatientActivity.this,LoginActivity.class));

        }
        return true;
    }

}
