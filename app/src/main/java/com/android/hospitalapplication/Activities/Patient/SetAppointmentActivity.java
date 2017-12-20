package com.android.hospitalapplication.Activities.Patient;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospitalapplication.R;

public class SetAppointmentActivity extends AppCompatActivity {
Spinner typeofproblem,doctor_list;
LinearLayout doctordetails;
Button request_Appointment,calldoctor,preferred_appointment_date;
TextView doctorcontactnumber,doctoraddress;
EditText describe_problem;
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);
           doctordetails=findViewById(R.id.doctordetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pat_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set Appointment");
        doctor_list = findViewById(R.id.doctor_list);
        typeofproblem = findViewById(R.id.typeofproblem);
        request_Appointment = findViewById(R.id.Request_Appointment);
        preferred_appointment_date = findViewById(R.id.preferred_appointment_date);
        doctoraddress = findViewById(R.id.doctoraddress);
        doctorcontactnumber = findViewById(R.id.doctorcontactnumber);
        calldoctor = findViewById(R.id.calldoctor);
        describe_problem = findViewById(R.id.describe);

        Calendar cal = Calendar.getInstance();
        final int year = cal.get(cal.YEAR);
        final int month = cal.get(cal.MONTH);
        final int day = cal.get(cal.DAY_OF_MONTH);

        initSpinner(typeofproblem, R.array.problem);
        initSpinner(doctor_list, R.array.doctorname);
      //date picker is set
        preferred_appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepicker = new DatePickerDialog(SetAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        preferred_appointment_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datepicker.show();
            }
        });
typeofproblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String problem = typeofproblem.getSelectedItem().toString();
        if (problem.equals("Cough and Cold") || problem.equals("Fever") || problem.equals("Migrane"))
        {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("Ear,Nose and Throat Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("female reproductive System Related Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("infant related Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("Eyes Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("Skin Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("Heart related Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("Brain and nervous system related problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("Tooth Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if (problem.equals("Stomach related Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if(problem.equals("Male reproductive System Related Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if(problem.equals("Muscular Related Problem")) {
            doctordetails.setVisibility(View.VISIBLE);
        }
        else if(problem.equals("Mental and Emotion Disorder Related Problem"))
        {doctordetails.setVisibility(View.VISIBLE);
        }
        else if(problem.equals("Face Related Problem"))
        {doctordetails.setVisibility(View.VISIBLE);
        }
        else if(problem.equals("Allergy"))
        {doctordetails.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});

        //call buuton deatils
        calldoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctornumber=doctorcontactnumber.getText().toString();
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",doctornumber,null));
                startActivity(intent);
            }
        });

          //appointment button is set
        request_Appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String preferred_appointmentdate=preferred_appointment_date.getText().toString();
                    String doctorcontactno=doctorcontactnumber.getText().toString();
                    String doctorsaddress=doctoraddress.getText().toString();
                    String typesofproblem=typeofproblem.getSelectedItem().toString();
                    String doctorname=doctor_list.getSelectedItem().toString();
                    String describe=describe_problem.getText().toString();

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
