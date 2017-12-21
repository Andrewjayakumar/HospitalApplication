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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospitalapplication.ModelClasses.Doctor;
import com.android.hospitalapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SetAppointmentActivity extends AppCompatActivity {

    Spinner typeofproblem, doctor_list;
    LinearLayout doctordetails;
    Button request_Appointment,  preferred_appointment_date;
    ImageButton calldoctor;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                         fetchDoctor("Gynecology");
                        break;
                    case 6:
                         fetchDoctor("Pediatrics");
                        break;
                    case 7:
                         fetchDoctor("Ophthalmologist");
                        break;
                    case 8:
                         fetchDoctor("Dermatology");
                        break;
                    case 9:
                        fetchDoctor("Cardiology");
                        break;
                    case 10:
                        fetchDoctor("Neurology");
                        break;
                    case 11:
                         fetchDoctor("Dentistry");
                        break;
                    case 12:
                        fetchDoctor("Gastroenterologist");
                        break;
                    case 13:
                        fetchDoctor("Urology");
                        break;
                    case 14:
                        fetchDoctor("Orthopedics");
                        break;
                    default:
                        Toast.makeText(SetAppointmentActivity.this, "No Doctor Found", Toast.LENGTH_SHORT);


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //doc spinner
        doctor_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                DatabaseReference dbrefUser = FirebaseDatabase.getInstance().getReference("Users");
                String docName = adapterView.getItemAtPosition(i).toString().trim();

                dbrefUser.orderByChild("name").equalTo(docName).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String contact = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        doctoraddress.setText(address);
                        doctorcontactnumber.setText(contact);

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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                if (preferred_appointmentdate.equals("") || doctorcontactno.equals("") || doctorsaddress.equals("") || typesofproblem.equals("Type of problem") || doctorname.equals("Doctor name")||describe.equals("")) {
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
        final ArrayList<String> doctors = new ArrayList<>();

        dbrefUsers.orderByChild("speciality").equalTo(spec).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                doctors.add(dataSnapshot.child("name").getValue().toString());
                Log.d("No. of Docs :",""+doctors.size());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_style,doctors);
                adapter.setDropDownViewResource(R.layout.spinner_style);
                doctor_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
