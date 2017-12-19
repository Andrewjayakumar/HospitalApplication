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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospitalapplication.R;

public class SetAppointmentActivity extends AppCompatActivity {
Spinner typeofproblem,doctor_list;
Button request_Appointment,calldoctor,preferred_appointment_date;
TextView doctorcontactnumber,doctoraddress;
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);

        doctor_list=findViewById(R.id.doctor_list);
        typeofproblem=findViewById(R.id.doctor_speciality);
        request_Appointment=findViewById(R.id.Request_Appointment);
        preferred_appointment_date=findViewById(R.id.preferred_appointment_date);
        doctoraddress=findViewById(R.id.doctoraddress);
        doctorcontactnumber=findViewById(R.id.doctorcontactnumber);
        calldoctor=findViewById(R.id.calldoctor);

            Calendar cal = Calendar.getInstance();
            final int year = cal.get(cal.YEAR);
            final int month = cal.get(cal.MONTH);
            final int day = cal.get(cal.DAY_OF_MONTH);

        initSpinner(typeofproblem, R.array.typeofproblem);
        initSpinner(doctor_list,R.array.doctorname);

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
                    String doctorcontactno=doctorcontactnumber.getText().toString();
                    String doctorsaddress=doctoraddress.getText().toString();
                    String typesofproblem=typeofproblem.getSelectedItem().toString();
                    String doctorname=doctor_list.getSelectedItem().toString();

                    if(preferred_appointmentdate.equals("")||doctorcontactno.equals("")||doctorsaddress.equals("")||typesofproblem.equals("Type of problem")||doctorname.equals("Doctor name"))
                    {
                        Toast.makeText(SetAppointmentActivity.this, "Enter All The fields", Toast.LENGTH_SHORT).show();

                    }
                    else if (typesofproblem.equals("Type Of Problem"))
                    {
                        typeofproblem.setFocusable(true);
                    }
                    else if(preferred_appointmentdate.equals(""))
                    { preferred_appointment_date.setError("Set Preferred Date");
                        preferred_appointment_date.setFocusable(true);
                        startActivity(new Intent(SetAppointmentActivity.this, PatientActivity.class));
                        finish();
                        Toast.makeText(SetAppointmentActivity.this, "Request For The Appointment Is Successfully Sent ", Toast.LENGTH_SHORT).show();
                    }
                    else if(doctorname.equals("Doctor Name"))
                    {
                        doctor_list.setFocusable(true);
                    }
                    else
                    {
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
