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
import android.util.Log;
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

import com.android.hospitalapplication.ModelClasses.Doctor;
import com.android.hospitalapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SetAppointmentActivity extends AppCompatActivity {

    Spinner typeofproblem, doctor_list;
    LinearLayout doctordetails;
    Button request_Appointment, calldoctor, preferred_appointment_date;
    TextView doctorcontactnumber, doctoraddress;
    EditText describe_problem;
    ArrayList<Doctor> docs;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);
        doctordetails = findViewById(R.id.doctordetails);
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

        typeofproblem = initSpinner(typeofproblem, R.array.problem);
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
                // String problem = typeofproblem.getSelectedItem().toString();
                Log.d("Position", "" + position);
                if (!(position == 0)) {
                    doctordetails.setVisibility(View.VISIBLE);
                }

                switch (position) {
                    case 1:
                    case 2:
                    case 3:
                        fetchDoctor("General Physician");
                        break;
                    case 4:
                        fetchDoctor("ENT");
                        break;
                    case 5:
                         fetchDoctor("Ophthalmologist");
                        break;
                    case 6:
                         fetchDoctor("Dermatologist");
                        break;
                    case 7:
                         fetchDoctor("Cardiology");
                        break;
                    case 8:
                         fetchDoctor("Neurology");
                        break;
                    case 9:
                        fetchDoctor("Dentistry");
                        break;
                    case 10:
                        fetchDoctor("Gastroenterologist");
                        break;
                    case 11:
                         fetchDoctor("Urology");
                        break;
                    case 12:
                        fetchDoctor("Urology");
                        break;
                    default:
                        Toast.makeText(SetAppointmentActivity.this, "No Doctor Found", Toast.LENGTH_SHORT);


                }
            }
       /* if(problem.equals("Cough and Cold")||problem.equals("Fever")||problem.equals("Migrane"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Ear,Nose and Throat Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("female reproductive System Related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("infant related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Eyes Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Skin Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Heart related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Brain and nervous system related problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Tooth Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Stomach related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Male reproductive System Related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Muscular Related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Mental and Emotion Disorder Related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Face Related Problem"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        else if(problem.equals("Allergy"))

            {
                doctordetails.setVisibility(View.VISIBLE);
            }
        }*/

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //call buuton deatils
        calldoctor.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                String doctorNumber = doctorcontactnumber.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", doctorNumber, null));
                startActivity(intent);
            }
        });

        //appointment button is set
        request_Appointment.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                String preferred_appointmentdate = preferred_appointment_date.getText().toString();
                String doctorcontactno = doctorcontactnumber.getText().toString();
                String doctorsaddress = doctoraddress.getText().toString();
                String typesofproblem = typeofproblem.getSelectedItem().toString();
                String doctorname = doctor_list.getSelectedItem().toString();
                String describe = describe_problem.getText().toString();

                if (preferred_appointmentdate.equals("") || doctorcontactno.equals("") || doctorsaddress.equals("") || typesofproblem.equals("Type of problem") || doctorname.equals("Doctor name")) {
                    Toast.makeText(SetAppointmentActivity.this, "Enter All The fields", Toast.LENGTH_SHORT).show();

                } else if (typesofproblem.equals("Type Of Problem")) {
                    typeofproblem.setFocusable(true);
                } else if (preferred_appointmentdate.equals("")) {
                    preferred_appointment_date.setError("Set Preferred Date");
                    preferred_appointment_date.setFocusable(true);
                    startActivity(new Intent(SetAppointmentActivity.this, PatientActivity.class));
                    finish();
                    Toast.makeText(SetAppointmentActivity.this, "Request For The Appointment Is Successfully Sent ", Toast.LENGTH_SHORT).show();
                } else if (doctorname.equals("Doctor Name")) {
                    doctor_list.setFocusable(true);
                } else {
                    startActivity(new Intent(SetAppointmentActivity.this, PatientActivity.class));
                    finish();
                    Toast.makeText(SetAppointmentActivity.this, "Request For The Appointment Is Successfully Sent ", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    public Spinner initSpinner(Spinner s, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), arrayId, R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        s.setAdapter(adapter);
        return s;
    }

    public void fetchDoctor(final String spec) {
        final DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");
        dbrefUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String uid = dataSnapshot.getKey();
                if (!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    dbrefUsers.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<CharSequence> docNames = new ArrayList<>();
                            String type = dataSnapshot.child("type").getValue().toString();
                            if (type.equals("Doctor")) {

                                String speciality = dataSnapshot.child("speciality").getValue().toString();
                                if (speciality.equals(spec)) {
                                  docNames.add(dataSnapshot.child("name").getValue().toString());

                                }
                            }
                            Log.d("User ID :", ""+docNames.size());
                            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_style,docNames);
                            adapter.setDropDownViewResource(R.layout.spinner_style);
                            doctor_list.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
