package com.android.hospitalapplication.Activities.Patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.hospitalapplication.Activities.LoginActivity;
import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class PatientActivity extends AppCompatActivity {

    public FrameLayout set_appointement,profile_info,upload_report,dietplan,appointmentStatus;
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
        dietplan=findViewById(R.id.dietplan);
        appointmentStatus=findViewById(R.id.view_status);

        set_appointement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,RequestAppointmentActivity.class));
            }
        });
        profile_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,PatientProfileActivity.class));
            }
        });
        upload_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,UploadReportActivity.class));

            }
        });

        dietplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Emergency= new AlertDialog.Builder(PatientActivity.this);
                Emergency.setTitle("Do You Want To Call Ambulance ?");
               Emergency.setPositiveButton("Call Ambulance", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       String doctorNumber ="123456789";
                       Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", doctorNumber, null));
                       startActivity(intent);                   }
               });
               Emergency.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogs, int which) {
                       dialogs.dismiss();

                   }
               });
                AlertDialog alert= Emergency.create();
                alert.show();

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
                finish();
        }
        return true;
    }

}
