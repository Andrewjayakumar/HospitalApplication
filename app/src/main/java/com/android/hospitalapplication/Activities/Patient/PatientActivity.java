package com.android.hospitalapplication.Activities.Patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.hospitalapplication.Activities.AboutUs;
import com.android.hospitalapplication.Activities.LoginActivity;
import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class PatientActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public CardView set_appointement,profile_info,upload_report,appointmentStatus,dietplan;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient");

        set_appointement=findViewById(R.id.set_appointment);
//        profile_info=findViewById(R.id.profile_info);
        upload_report=findViewById(R.id.upload_report);
        dietplan=findViewById(R.id.dietplan);
        appointmentStatus=findViewById(R.id.view_status);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        set_appointement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientActivity.this,RequestAppointmentActivity.class));
            }
        });
//        profile_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PatientActivity.this,PatientProfileActivity.class));
//            }
//        });
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
                Emergency.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogs, int which) {
                        dialogs.dismiss();

                    }
                });
                Emergency.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String doctorNumber ="123456789";
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", doctorNumber, null));
                        startActivity(intent);                   }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_profile) {
            startActivity(new Intent(PatientActivity.this,PatientProfileActivity.class));

        } else if (id == R.id.about_us) {

            startActivity(new Intent(PatientActivity.this,AboutUs.class));

        }  else if (id == R.id.sign_out) {

            auth.signOut();
            startActivity(new Intent(PatientActivity.this,LoginActivity.class));
            finish();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.rate_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
