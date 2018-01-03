package com.android.hospitalapplication.Activities.Doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.hospitalapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UploadPrescriptionActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    ImageButton capturePhoto, choosePhone;
    String imagePath;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    String docId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

        capturePhoto = findViewById(R.id.capturePhoto);
        choosePhone = findViewById(R.id.choosePhone);

        Toolbar toolbar = (Toolbar) findViewById(R.id.pat_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Prescriptions");


         mProgressDialog = new ProgressDialog(this);
         mProgressDialog.setMessage("Uploading Image ...");
         capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager())!= null) {
                    File imageFile = null;
                    try {
                        imageFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imageFile!=null){
                        Uri imageUri = FileProvider.getUriForFile(UploadPrescriptionActivity.this,"com.android.hospitalapplication.fileprovider",imageFile);
                        i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                    }

                }
            }
        });

        choosePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new   Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "presc_"+timeStamp+"_";
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

        final String patId = getIntent().getStringExtra("pat_id");
        final String fileName = imageUri.getLastPathSegment();
        Log.d("file name :",fileName);
        StorageReference strefPresc = FirebaseStorage.getInstance().getReference("Prescriptions");

        strefPresc.child(patId).child(docId).child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgressDialog.dismiss();
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                DatabaseReference dbrefPresc = FirebaseDatabase.getInstance().getReference("Prescriptions");
                HashMap<String,String> imageDetails = new HashMap<>();
                imageDetails.put("image_name",fileName);
                imageDetails.put("image_url",downloadUrl);
                dbrefPresc.child(docId).child(patId).setValue(imageDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UploadPrescriptionActivity.this,"Prescription Uploaded Successfully !!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String patId = getIntent().getStringExtra("pat_id");
        Intent i = new Intent(UploadPrescriptionActivity.this,AppointmentDetailsActivity.class);
        i.putExtra("pat_id",patId);
        startActivity(i);
        finish();
    }
}
