package com.mamunsproject.awesome_collage_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Upload_Image extends AppCompatActivity {

    private Spinner imageCategory;
    private CardView selectImage;
    private Button uploadImage;
    private ImageView galleryImageView;

    private DatabaseReference reference;
    private StorageReference storageReference;
    String downloadUrl;



    ProgressDialog pd;
    private String category;
    private final int REQUEST=1;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__image);

        init();
        clickListener();


        String[] items=new String[]{"Select Category","Convocation","Independence Day","Other Events"};
        imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));



    }


    public void init(){
        imageCategory=findViewById(R.id.image_category);
        selectImage=findViewById(R.id.add_Gallery_Image);
        imageCategory=findViewById(R.id.image_category);
        galleryImageView=findViewById(R.id.galleryImageView);
        uploadImage=findViewById(R.id.uploadimageBtn);
        reference=FirebaseDatabase.getInstance().getReference().child("gallery");
        storageReference= FirebaseStorage.getInstance().getReference().child("gallery");
        pd=new ProgressDialog(this);


    }


    public void clickListener(){

        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=imageCategory.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitmap==null){

                    Toast.makeText(getApplicationContext(),"Please Select an Image!",Toast.LENGTH_SHORT).show();


                }else if (category.equals("Select Category")){

                    Toast.makeText(getApplicationContext(),"Please Select an Image!",Toast.LENGTH_SHORT).show();


                }else {

                pd.setMessage("Uploading...");
                pd.show();
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImg = baos.toByteArray();



        //akhan theke firebase image send korbo
        final StorageReference filePath;

        //child r maddome firebase file create kora hoy
        filePath=storageReference.child(finalImg+"jpg");

        //akhan theke firebase image upload kora hobe;
        final UploadTask uploadTask=filePath.putBytes(finalImg);

        uploadTask.addOnCompleteListener(Upload_Image.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                   pd.dismiss();
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
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),"Something went Wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadData() {

                                //user j category select korbe sei onujayi firebase bosbe
        reference=reference.child(category);
        final  String uniqueKey=reference.push().getKey();


        reference.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Succes!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();


            }
        });

    }


    public void openGallery(){

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
            galleryImageView.setImageBitmap(bitmap);
        }
    }

}