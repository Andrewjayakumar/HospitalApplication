package com.android.hospitalapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PatientActivity extends AppCompatActivity {
public Button set_appointement,profile_info,view_precaution,upload_report,dietplan;
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
        view_precaution=findViewById(R.id.view_precaution);
        dietplan=findViewById(R.id.dietplan);

        set_appointement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,set_appointment.class));
            }
        });
        profile_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                startActivity(new Intent(this,LoginActivity.class));

        }
        return true;
    }

}
