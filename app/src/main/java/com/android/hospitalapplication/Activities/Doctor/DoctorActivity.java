package com.android.hospitalapplication.Activities.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.hospitalapplication.Activities.LoginActivity;
import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar =  findViewById(R.id.doc_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doctor");

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
