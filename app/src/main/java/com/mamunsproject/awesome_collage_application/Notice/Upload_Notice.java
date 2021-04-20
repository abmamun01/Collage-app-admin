package com.mamunsproject.awesome_collage_application.Notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mamunsproject.awesome_collage_application.Model.Notice_Data;
import com.mamunsproject.awesome_collage_application.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Upload_Notice extends AppCompatActivity {


    private CardView addImage;
    private final int REQUEST = 1;
    private Bitmap bitmap;
    private ImageView noticeImageView;
    private EditText noticeTitle;
    private Button uploadNoticeButton;

    //jodi download urle kicu na thake tahole blank chole jabe
    private String downloadUrl="";

   private SweetAlertDialog pDialog;


    private DatabaseReference reference,dbRef;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__notice);

        init();
        clickListener();
    }


    public void init() {
        addImage = findViewById(R.id.add_Image);
        noticeImageView = findViewById(R.id.noticeImageView);
        noticeTitle = findViewById(R.id.notice_title);
        uploadNoticeButton = findViewById(R.id.uplooadNoticeButton);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


    }

    public void clickListener() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallerry();
            }
        });

        uploadNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (noticeTitle.getText().toString().isEmpty()) {
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                } else if (bitmap == null) {
                    uploadData();
                } else {
                    uploadImage();
                }
            }
        });

    }

    private void uploadImage() {

        //======================Progress========================================
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Uploading...");
        pDialog.setCancelable(false);
        pDialog.show();


        //======================Progress========================================



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImg = baos.toByteArray();



        //akhan theke firebase image send korbo
        final StorageReference filePath;

        //child r maddome firebase file create kora hoy
        filePath=storageReference.child("Notice").child(finalImg+"jpg");

        //akhan theke firebase image upload kora hobe;
        final UploadTask uploadTask=filePath.putBytes(finalImg);

        uploadTask.addOnCompleteListener(Upload_Notice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    //jodi successfully upload hoye jay tahole image r path get korte hobe jate database store korte pari
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    uploadData();

                                }
                            });
                        }
                    });
                }else {
                    pDialog.dismissWithAnimation();
                    Toast.makeText(getApplicationContext(),"Something went Wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void uploadData() {

        reference=reference.child("Notice");
        final String uniqueKey=reference.push().getKey();

        //Notice Model r title pass korar jonno
        String title=noticeTitle.getText().toString();


        //Current Date o Timing find korar jonno
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        String date=currentDate.format(calForDate.getTime());

        Calendar calForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("h:mm a");
        String time=currentTime.format(calForTime.getTime());


        Notice_Data notice_data=new Notice_Data(title,downloadUrl,date,time,uniqueKey);

        //Firebase data store korar jonno

        reference.child(uniqueKey).setValue(notice_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                pDialog.dismissWithAnimation();
                Toast.makeText(getApplicationContext(),"Notice Uploaded!",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pDialog.dismissWithAnimation();
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();

                Log.d("TAG", "onFailure: ");

            }
        });


    }


    private void openGallerry() {

        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeImageView.setImageBitmap(bitmap);
        }
    }


}