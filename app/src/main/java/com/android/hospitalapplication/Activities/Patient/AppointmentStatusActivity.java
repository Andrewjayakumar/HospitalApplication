package com.android.hospitalapplication.Activities.Patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospitalapplication.Activities.AppointmentReceiptActivity;
import com.android.hospitalapplication.ModelClasses.User;
import com.android.hospitalapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AppointmentStatusActivity extends AppCompatActivity
{

    Toolbar mToolBar;
    RecyclerView reqList,aptList;
    final String pat = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbrefRequests = FirebaseDatabase.getInstance().getReference("Requests").child(pat);
    DatabaseReference dbrefApt = FirebaseDatabase.getInstance().getReference("Appointments").child(pat);
    DatabaseReference dbrefUsers = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_status);

        mToolBar=findViewById(R.id.apt_app_bar);
        setSupportActionBar(mToolBar);

        getSupportActionBar().setTitle("Appointment Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reqList =findViewById(R.id.apt_list);
        aptList=findViewById(R.id.cnf_apt_list);
        reqList.setLayoutManager(new LinearLayoutManager(this));
        aptList.setLayoutManager(new LinearLayoutManager(this));


        getStatus();
        getConfirmedAppointments();

    }

    public  void getStatus(){

        FirebaseRecyclerAdapter<User,RequestsViewHolder> adapter= new FirebaseRecyclerAdapter<User,RequestsViewHolder>(User.class,R.layout.user_apt_list,RequestsViewHolder.class,dbrefRequests){
            @Override
            protected void populateViewHolder(final RequestsViewHolder viewHolder, User model, int position) {
                final String u_id = getRef(position).getKey();
                dbrefRequests.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(u_id)){
                            final String reqStat = dataSnapshot.child(u_id).child("req_status").getValue().toString();
                            Log.d("user id:",u_id+"\n"+reqStat);
                            final String date = dataSnapshot.child(u_id).child("req_date").getValue().toString();
                            if(reqStat.equals("sent")){
                                dbrefUsers.child(u_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String name = dataSnapshot.child("NAME").getValue().toString();
                                        viewHolder.setName(name);
                                        viewHolder.setDate(date);
                                        viewHolder.setStatus(reqStat);
                                        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AlertDialog.Builder bd = new AlertDialog.Builder(AppointmentStatusActivity.this);
                                                bd.setTitle("Cancel Request").setMessage("Are you sure you want to cancel the request ?");
                                                bd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Map remove = new HashMap();
                                                        remove.put("Requests/"+pat+"/"+u_id,null);
                                                        remove.put("Requests/"+u_id+"/"+pat,null);
                                                        dbrefRoot.updateChildren(remove, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                if(databaseError==null){
                                                                    Toast.makeText(getApplicationContext(),"Request Cancelled",Toast.LENGTH_LONG).show();
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                                }

                                                            }
                                                        });
                                                    }
                                                });
                                                bd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                                AlertDialog ad = bd.create();
                                                ad.show();

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else if(reqStat.equals("declined")){
                                final String reason = dataSnapshot.child(u_id).child("reason").getValue().toString();
                                dbrefUsers.child(u_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot Snapshot) {
                                        String name = Snapshot.child("name").getValue().toString();
                                        viewHolder.setName(name);
                                        viewHolder.setDate(reason);
                                        viewHolder.setStatus(reqStat);

                                        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Map remove = new HashMap();
                                                remove.put("Requests/"+pat+"/"+u_id,null);
                                                dbrefRoot.updateChildren(remove, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        if(databaseError==null){
                                                            Toast.makeText(getApplicationContext(),"Request Removed",Toast.LENGTH_LONG).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        reqList.setAdapter(adapter);

    }


    public void getConfirmedAppointments(){
                FirebaseRecyclerAdapter<User,RequestsViewHolder> adapter = new FirebaseRecyclerAdapter<User, RequestsViewHolder>(User.class,R.layout.user_apt_list,RequestsViewHolder.class,dbrefApt) {
                    @Override
                    protected void populateViewHolder(final RequestsViewHolder viewHolder, User model, int position) {
                             final String doc_id = getRef(position).getKey();
                             dbrefApt.addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     if(dataSnapshot.hasChild(doc_id)){
                                         final String aptDate = dataSnapshot.child(doc_id).child("apt_date").getValue().toString();
                                         final String aptTime = dataSnapshot.child(doc_id).child("apt_time").getValue().toString();
                                         dbrefUsers.child(doc_id).addValueEventListener(new ValueEventListener()
                                         {
                                             @Override
                                             public void onDataChange(DataSnapshot Snapshot) {
                                                 String docName = Snapshot.child("name").getValue().toString();
                                                 Log.d("name of doc :",docName);
                                                 viewHolder.setName(docName);
                                                 viewHolder.setDate(aptDate+" "+aptTime);
                                                 viewHolder.setStatus("");
                                                 viewHolder.cancel.setVisibility(View.INVISIBLE);
                                                 viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         Intent i = new Intent(AppointmentStatusActivity.this, AppointmentReceiptActivity.class);
                                                         i.putExtra("doc_id",doc_id);
                                                         i.putExtra("pat_id",pat);
                                                         startActivity(i);
                                                     }
                                                 });
                                             }

                                             @Override
                                             public void onCancelled(DatabaseError databaseError) {

                                             }
                                         });

                                     }
                                 }

                                 @Override
                                 public void onCancelled(DatabaseError databaseError) {

                                 }
                             });
                    }
                };
                aptList.setAdapter(adapter);

    }
    public static class RequestsViewHolder extends RecyclerView.ViewHolder {
       public  View view;
        public ImageButton cancel;


        public RequestsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            cancel=view.findViewById(R.id.cancel_req);
        }


        public void setName(String name) {
            TextView docName = view.findViewById(R.id.name);
            docName.setText(name);
        }

        public void setDate(String date) {
            TextView prefDate = view.findViewById(R.id.pref_date);
            prefDate.setText(date);

        }

        public void setStatus(String status) {
            TextView stat = view.findViewById(R.id.req_status);
            stat.setText(status);
        }

    }

}
