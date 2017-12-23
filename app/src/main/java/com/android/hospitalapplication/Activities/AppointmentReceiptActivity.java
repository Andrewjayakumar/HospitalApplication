package com.android.hospitalapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.hospitalapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentReceiptActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView aptId,docName,patName,aptDate,aptRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_receipt);
        mToolbar=findViewById(R.id.apt_receipt_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appointment Details");

        aptId=findViewById(R.id.apt_id);
        aptDate=findViewById(R.id.date_time);
        aptRemarks=findViewById(R.id.remarks);
        docName=findViewById(R.id.doc_name);
        patName=findViewById(R.id.pat_name);

        String docId = getIntent().getStringExtra("doc_id");
        String patId = getIntent().getStringExtra("pat_id");

        getDetails(docId,patId);

    }

    public void getDetails(final String docId, final String patID){
        final DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();


        dbrefRoot.child("Appointments").child(docId).child(patID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String date = dataSnapshot.child("apt_date").getValue().toString();
                final String time = dataSnapshot.child("apt_time").getValue().toString();
                final String id = dataSnapshot.child("apt_id").getValue().toString();
                final String remark = dataSnapshot.child("apt_remarks").getValue().toString();

                dbrefRoot.child("Users").child(docId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String doctorName = dataSnapshot.child("name").getValue().toString();
                        dbrefRoot.child("Users").child(patID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String patientName = dataSnapshot.child("name").getValue().toString();

                                aptDate.setText(date+" "+time);
                                aptId.setText(id);
                                aptRemarks.setText(remark);
                                docName.setText(doctorName);
                                patName.setText(patientName);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
