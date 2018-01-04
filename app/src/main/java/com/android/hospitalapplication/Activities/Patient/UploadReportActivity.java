package com.android.hospitalapplication.Activities.Patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.hospitalapplication.Activities.Doctor.UploadPrescriptionActivity;
import com.android.hospitalapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class UploadReportActivity extends AppCompatActivity {
    ImageButton capturePhoto, choosePhone;
    private ProgressDialog mProgressDialog;
    private TextInputEditText reportName;
    String imagePath,report;
    private static final int REQUEST_IMAGE_CAPTURE =1 ;
    private static final int  REQUEST_IMAGE_PICK = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        capturePhoto = findViewById(R.id.capturePhoto);
        choosePhone = findViewById(R.id.choosePhone);
        reportName = findViewById(R.id.report_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.pat_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Reports");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Uploading Image ...");

        capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report = reportName.getText().toString();
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager())!= null) {
                    File imageFile = null;
                    try {
                        imageFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(!TextUtils.isEmpty(report)) {

                        if (imageFile != null) {
                            Uri imageUri = FileProvider.getUriForFile(UploadReportActivity.this, "com.android.hospitalapplication.fileprovider", imageFile);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                    else {
                        reportName.setError("Report Must Possess A Name");
                    }
                }
            }
        });

        choosePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report = reportName.getText().toString();
                if(!TextUtils.isEmpty(report)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_IMAGE_PICK);
                }
                else {
                    reportName.setError("Report Must Possess A Name");
                }
            }
        });
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "report_"+timeStamp+"_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDirectory);
        imagePath= image.getAbsolutePath();
        Log.d("image path :",imagePath);
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Log.d("Gallery addition :","success!! "+imagePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            mProgressDialog.show();
            galleryAddPic();
            File image = new File(imagePath);
            Uri imageUri = Uri.fromFile(image);
            uploadImage(imageUri);
        }
        else if (requestCode==REQUEST_IMAGE_PICK && resultCode==RESULT_OK){
            mProgressDialog.show();
            Uri uri=data.getData();
            uploadImage(uri);
        }
    }

    public void uploadImage(Uri imageUri){

        final String patId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String fileName = imageUri.getLastPathSegment();
        Log.d("file name :",fileName);
        StorageReference strefReports = FirebaseStorage.getInstance().getReference("Reports");

        final DatabaseReference dbrefReports = FirebaseDatabase.getInstance().getReference("Reports");
        strefReports.child(patId).child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                HashMap<String,String> imageDetails = new HashMap<>();
                imageDetails.put("image_name",report);
                imageDetails.put("image_url",downloadUrl);
                dbrefReports.child(patId).push().setValue(imageDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                        Toast.makeText(UploadReportActivity.this,"Report Uploaded Successfully !!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    }

