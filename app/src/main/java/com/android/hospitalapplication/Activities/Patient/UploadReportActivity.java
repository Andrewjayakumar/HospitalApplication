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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.hospitalapplication.Activities.Doctor.UploadPrescriptionActivity;
import com.android.hospitalapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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


public class UploadReportActivity extends AppCompatActivity {
    ImageButton capturePhoto, choosePhone;
    private Uri fileUri;
    private ProgressDialog mProgressDialog;
    ImageView img;
    Context context;
    private StorageReference mStorageRef;
    String imagePath;
    private static final int REQUEST_IMAGE_CAPTURE =1 ;
    private static final int  REQUEST_IMAGE_PICK = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        capturePhoto = findViewById(R.id.capturePhoto);
        choosePhone = findViewById(R.id.choosePhone);
        img = findViewById(R.id.img);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.pat_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Reports");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        Uri imageUri = FileProvider.getUriForFile(UploadReportActivity.this,"com.android.hospitalapplication.fileprovider",imageFile);
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

        String patId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String fileName = imageUri.getLastPathSegment();
        Log.d("file name :",fileName);
        StorageReference strefPresc = FirebaseStorage.getInstance().getReference("Reports");

        strefPresc.child(patId).child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgressDialog.dismiss();
                Toast.makeText(UploadReportActivity.this,"Report Uploaded Successfully !!",Toast.LENGTH_SHORT).show();
            }
        });
    }


/*
        choosePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);

            }
        });
        capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadReportActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                5);
                    }
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,f);
                startActivityForResult(intent, 1);
                Toast.makeText(context, "Photo Uploaded" , Toast.LENGTH_SHORT).show();
            }


        });


    }


    void upload(Uri f) {
        final Uri file = f;
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp=System.currentTimeMillis();
        StorageReference report = FirebaseStorage.getInstance().getReference().child("report").child(uid).child(String.valueOf(timestamp));
        report.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        String downloadurl = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(UploadReportActivity.this, "Report Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.d("file",""+file);
                        Toast.makeText(UploadReportActivity.this, "Error Uploading Report", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());




                        Uri fileUri= FileProvider.getUriForFile(getBaseContext(), getBaseContext().getApplicationContext().getPackageName() + ".provider", f);;
                        upload(fileUri);



                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);


                    img.setImageBitmap(bitmap);


                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";

                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                        outFile.flush();

                        outFile.close();

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                img.setImageBitmap(thumbnail);
                upload(selectedImage);

            }

        }*/
    }

