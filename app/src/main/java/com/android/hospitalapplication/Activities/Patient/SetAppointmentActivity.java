package com.android.hospitalapplication.Activities.Patient;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.hospitalapplication.R;

public class SetAppointmentActivity extends AppCompatActivity {
Spinner Doctor_speciality,Doctor_list;
Button request_Appointment;
EditText preferred_appointment_date;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);

        Doctor_list=findViewById(R.id.doctor_list);
        Doctor_speciality=findViewById(R.id.doctor_speciality);
        request_Appointment=findViewById(R.id.Request_Appointment);
        preferred_appointment_date=findViewById(R.id.preferred_appointment_date);

            Calendar cal = Calendar.getInstance();
            final int year = cal.get(cal.YEAR);
            final int month = cal.get(cal.MONTH);
            final int day = cal.get(cal.DAY_OF_MONTH);

        initSpinner(Doctor_speciality, R.array.speciality);
        //initSpinner(Doctor_list,R.array.Doctor_list);

        preferred_appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepicker=new DatePickerDialog(SetAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        preferred_appointment_date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                datepicker.show();}
        });





        request_Appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String preferred_appointmentdate=preferred_appointment_date.getText().toString();
                    if(preferred_appointmentdate.equals(""))
                    {
                        preferred_appointment_date.setError("Set Preferred Date");
                        preferred_appointment_date.setFocusable(true);
                    }
                    else {
                        startActivity(new Intent(SetAppointmentActivity.this, PatientActivity.class));
                        finish();
                        Toast.makeText(SetAppointmentActivity.this, "Request For The Appointment Is Successfully Sent ", Toast.LENGTH_SHORT).show();
                    }

                }
            });


    }

    public void initSpinner(Spinner s, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), arrayId, R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        s.setAdapter(adapter);
    }
}
