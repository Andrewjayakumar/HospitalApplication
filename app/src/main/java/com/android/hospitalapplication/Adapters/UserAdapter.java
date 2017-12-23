package com.android.hospitalapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hospitalapplication.ModelClasses.Patient;
import com.android.hospitalapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav on 20-12-2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.PatientViewHolder> {
    private Context ctx;
    private ArrayList<Patient> patients = new ArrayList<>();


    public UserAdapter(){

    }

    public UserAdapter(ArrayList<Patient> p, Context ctx){
        this.ctx=ctx;
        this.patients=p;
    }

    @Override
    public UserAdapter.PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_appointment_list,parent,false);
       return new PatientViewHolder(v);
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder{

        private TextView time,name,desription;
        public PatientViewHolder(View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.appointment_time);
            name = itemView.findViewById(R.id.name);

        }
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
              Patient patient = patients.get(position)  ;
              holder.name.setText(patient.getName());
              holder.desription.setText(patient.getBloodGroup());
              holder.time.setText(patient.getAge());
    }



    @Override
    public int getItemCount() {
        return patients.size();
    }
}
