package com.android.hospitalapplication.Activities.Doctor;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.hospitalapplication.Activities.AppointmentReceiptActivity;
import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleAppointmentActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Button setDate,setTime,schApt;
    EditText remarks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);

        mToolbar = findViewById(R.id.apt_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Schedule Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDate = findViewById(R.id.set_date);
        setTime = findViewById(R.id.set_time);
        schApt = findViewById(R.id.schedule_apt);
        remarks=findViewById(R.id.remarks);

        String prefDate = getIntent().getStringExtra("pref_date");
        final String pat_id = getIntent().getStringExtra("pat_id");

        setDate.setText(prefDate);
        setDate.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(c.YEAR);
                int month = c.get(c.MONTH);
                int day = c.get(c.DAY_OF_MONTH);

                DatePickerDialog datepicker = new DatePickerDialog(ScheduleAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year,month,day);
                datepicker.show();
            }

        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                 int hourOfDay = c.get(c.HOUR_OF_DAY);
                 int minutes = c.get(c.MINUTE);

                TimePickerDialog tp = new TimePickerDialog(ScheduleAppointmentActivity.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String AM_PM ;
                        if(i < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }

                        setTime.setText(""+(i%12)+":"+i1+AM_PM);
                    }
                },hourOfDay,minutes,false);
                tp.show();
            }
        });
        setTime.setText("Set Appointment time");


        schApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = setDate.getText().toString();
                String time = setTime.getText().toString();
                String remark = remarks.getText().toString();
                String doc_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(!(date.equals(null)) && !(time.equals(null))) {
                    setAppointment(pat_id, doc_id, date, time, remark);
                    finish();
                }
                else {
                    Toast.makeText(ScheduleAppointmentActivity.this,"Please select date & time for the appointment",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setAppointment(final String patId, final String docId, String date, String time, String remarks){
        final DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();


        Map aptDetails = new HashMap();

        aptDetails.put("Appointments/"+patId+"/"+docId+"/"+"apt_date",date);
        aptDetails.put("Appointments/"+docId+"/"+patId+"/"+"apt_date",date);
        aptDetails.put("Appointments/"+patId+"/"+docId+"/"+"apt_time",time);
        aptDetails.put("Appointments/"+docId+"/"+patId+"/"+"apt_time",time);
        aptDetails.put("Appointments/"+patId+"/"+docId+"/"+"apt_remarks",remarks);
        aptDetails.put("Appointments/"+docId+"/"+patId+"/"+"apt_remarks",remarks);
        aptDetails.put("Appointments/"+patId+"/"+docId+"/"+"apt_id",ServerValue.TIMESTAMP);
        aptDetails.put("Appointments/"+docId+"/"+patId+"/"+"apt_id",ServerValue.TIMESTAMP);


        dbrefRoot.updateChildren(aptDetails, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    Map remove = new HashMap();
                    remove.put("Requests/"+patId+"/"+docId,null);
                    remove.put("Requests/"+docId+"/"+patId,null);
                    dbrefRoot.updateChildren(remove, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null){
                                Toast.makeText(getApplicationContext(),"Appointment Set",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ScheduleAppointmentActivity.this,AppointmentReceiptActivity.class);
                                i.putExtra("doc_id",docId);
                                i.putExtra("pat_id",patId);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            }
        });

    }
}