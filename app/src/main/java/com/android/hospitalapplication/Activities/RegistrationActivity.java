package com.android.hospitalapplication.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.hospitalapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout genDetails, docDetails;
    private RelativeLayout patDetails;
    private Spinner blood_group, speciality;
    private RadioGroup genderButtons;
    private String email, name, password, confirmPassword, address, phone, gender, registrationId, bloodGroup, specialisation;
    private TextInputEditText e_mail, name_user, pass, confirmPass, contact, add, regId;
    private Button register;
    private RadioButton doctor, patient;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mToolbar = findViewById(R.id.register_app_bar_layout);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        genDetails = findViewById(R.id.gen_details);
        docDetails = findViewById(R.id.doctor_details);
        patDetails = findViewById(R.id.patient_details);
        blood_group = findViewById(R.id.blood_group);
        speciality = findViewById(R.id.doctor_speciality);
        genderButtons = findViewById(R.id.gender_buttons);
        doctor = findViewById(R.id.doctor);
        patient = findViewById(R.id.patient);

        e_mail = findViewById(R.id.email_register);
        name_user = findViewById(R.id.name);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_password);
        contact = findViewById(R.id.phone);
        add = findViewById(R.id.address);
        regId = findViewById(R.id.registration_no);

        register = findViewById(R.id.register_button);

        blood_group=initSpinner(blood_group, R.array.blood_groups);
        speciality=initSpinner(speciality, R.array.speciality);

        blood_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodGroup = adapterView.getItemAtPosition(i).toString();
                Log.d("Blood Group :",""+bloodGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                specialisation = speciality.getSelectedItem().toString();
                Log.d("Speciality :",""+specialisation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(patient.isChecked() || doctor.isChecked())) {
                    Toast.makeText(RegistrationActivity.this, "Please Select A Category", Toast.LENGTH_SHORT).show();
                } else {
                    email = e_mail.getText().toString().trim();
                    name = name_user.getText().toString().trim();
                    password = pass.getText().toString().trim();
                    phone = contact.getText().toString().trim();
                    address = add.getText().toString().trim();
                    confirmPassword = confirmPass.getText().toString().trim();

                    if (validateDetails(name, email, password, confirmPassword, address, phone)) {
                       if (genderButtons.getCheckedRadioButtonId() < 0) {
                            Toast.makeText(RegistrationActivity.this, "Please Select A Gender", Toast.LENGTH_SHORT).show();
                        } else if (patient.isChecked()) {
                           if(!bloodGroup.equals(null)) {
                               createAccount(name, email, password, phone, address, gender, bloodGroup);
                           }
                           else{
                               Toast.makeText(RegistrationActivity.this,"Please Select A Blood Group !",Toast.LENGTH_SHORT).show();
                           }
                       } else if (doctor.isChecked()) {
                            registrationId = regId.getText().toString().trim();
                            if (!(registrationId.startsWith("DOC") && registrationId.length()==5)) {
                                regId.setError("PLease Enter a Valid Registration Id");
                            } else {
                                if(!specialisation.equals(null)) {
                                    createAccount(name, email, password, phone, address, gender, specialisation, registrationId);
                                }
                                else{
                                    Toast.makeText(RegistrationActivity.this,"Please Select A Blood Group !",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
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

    public void isSelected(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.doctor:
                if (checked) {
                    genDetails.setVisibility(View.VISIBLE);
                    docDetails.setVisibility(View.VISIBLE);
                    patDetails.setVisibility(View.GONE);
                    gender = null;
                    genderButtons.clearCheck();
                    Log.d("Gender", "" + gender);

                }
                break;
            case R.id.patient:
                if (checked)
                {
                    genDetails.setVisibility(View.VISIBLE);
                    patDetails.setVisibility(View.VISIBLE);
                    docDetails.setVisibility(View.GONE);
                    gender = null;
                    genderButtons.clearCheck();
                    Log.d("Gender", "" + gender);

                }
                break;
            case R.id.gender_male:
                if (checked) {
                    gender = "M";
                    Log.d("Gender", "" + gender);
                }
                break;
            case R.id.gender_female:
                if (checked) {
                    gender = "F";
                    Log.d("Gender", "" + gender);
                }
                break;
            default:
                genDetails.setVisibility(View.GONE);
                patDetails.setVisibility(View.GONE);
                docDetails.setVisibility(View.GONE);
        }
    }

    public void createAccount(final String name, String email, String password, final String phone, final String address, final String gender, final String bg) {

        final ProgressDialog loading = ProgressDialog.show(RegistrationActivity.this, "Creating Account", "Please Wait...", false, false);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = auth.getCurrentUser();
                    String uid = currentUser.getUid();

                    dbref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userDetails = new HashMap<String, String>();
                    userDetails.put("name", name);
                    userDetails.put("phone", phone);
                    userDetails.put("address", address);
                    userDetails.put("gender", gender);
                    userDetails.put("blood_group", bg);
                    userDetails.put("type", "Patient");
                    dbref.setValue(userDetails).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loading.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });

                }
            }
        });
    }

    public void createAccount(final String name, String email, String password, final String phone, final String address, final String gender, final String speciality, final String regId) {

        final ProgressDialog loading = ProgressDialog.show(RegistrationActivity.this, "Creating Account", "Please Wait...", false, false);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = auth.getCurrentUser();
                    String uid = currentUser.getUid();

                    dbref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userDetails = new HashMap<String, String>();
                    userDetails.put("name", name);
                    userDetails.put("phone", phone);
                    userDetails.put("address", address);
                    userDetails.put("gender", gender);
                    userDetails.put("speciality", speciality);
                    userDetails.put("doctor_reg_id", regId);
                    userDetails.put("type", "Doctor");
                    dbref.setValue(userDetails).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loading.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });

                }
            }
        });
    }

    private boolean emailIsValid(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        Log.d("email", email + " " + matcher.matches());
        return matcher.matches();

    }

    private boolean passIsValid(String pass) {
        if (pass.length() > 4) return true;
        else return false;
    }

    public boolean validateDetails(String name, String email, final String password, String confirmPassword, String address, String phone) {

        boolean check = true;

        if (check) {
            if (!emailIsValid(email)) {
                e_mail.setError("Invalid e-Mail");
                check = false;
            }
            if (name.isEmpty()) {
                name_user.setError("Name Cannot Be Empty");
                check = false;
            }
            if (phone.isEmpty() || !(phone.length() == 10)) {
                contact.setError("Invalid Contact No.(length should be 10)");
                check = false;
            }
            if (address.isEmpty()) {
                add.setError("Address Cannot Be Empty");
                check = false;
            }
            if (!passIsValid(password)) {
                Log.d("password", password);
                pass.setError("Password should have minimum 4 characters");
                check = false;
            }
            if (!(confirmPassword.equals(password))) {
                confirmPass.setError("Passwords Do Not Match");
                confirmPass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String confirmpass = confirmPass.getText().toString().trim();
                        Log.d("pass", password + "\n" + confirmpass);
                        if (!(confirmpass.equals(password))) {
                            confirmPass.setError("Passwords Do Not Match");
                        }
                    }
                });
                check = false;
            }
            if (genderButtons.getCheckedRadioButtonId() < 0) {
                Toast.makeText(RegistrationActivity.this, "Please Select A Gender", Toast.LENGTH_LONG).show();
                check = false;
            }
        } else {
            check = true;
        }
        return  check;
    }

}
