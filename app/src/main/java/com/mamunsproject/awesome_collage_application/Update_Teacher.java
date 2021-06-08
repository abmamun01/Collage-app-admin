package com.mamunsproject.awesome_collage_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.mamunsproject.awesome_collage_application.Faculty.AddTeacher;
import com.mamunsproject.awesome_collage_application.Faculty.Update_Faculty;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Update_Teacher extends AppCompatActivity {

    private ImageView updateTeacherImage;
    private Button updateTeacherButton, deleteTeacherButton;
    private EditText updateTeacherName, updateTeacherEmail, updateTeacherPost;

    private String name, email, post, image;
    private final int REQUEST = 1;
    private Bitmap bitmap = null;
    private String downloadUrl, category, uniqueKey;


    private StorageReference storageReference;
    private DatabaseReference reference;
    SweetAlertDialog pDialog;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__teacher);

        init();
        clickListener();


        uniqueKey = getIntent().getStringExtra("key");
        //Aikhane query hobe
        //category name r kono variable nai tai ata create korte hobe
        category = getIntent().getStringExtra("category");


    }

    private void init() {

        updateTeacherImage = findViewById(R.id.updateTeacherImage);
        updateTeacherButton = findViewById(R.id.updateTeacherButton);
        deleteTeacherButton = findViewById(R.id.deleteTeacherButton);
        updateTeacherName = findViewById(R.id.updateTeacherName);
        updateTeacherEmail = findViewById(R.id.updateTeacherEmail);
        updateTeacherPost = findViewById(R.id.updateTeacherPost);

        Context context;
        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");


        updateTeacherName.setText(name);
        updateTeacherEmail.setText(email);
        updateTeacherPost.setText(post);


        try {

            Picasso.get().load(image).rotate(-80).into(updateTeacherImage);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void clickListener() {

        updateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });

        updateTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updateTeacherName.getText().toString();
                email = updateTeacherEmail.getText().toString();
                post = updateTeacherPost.getText().toString();

                checkValidation();

            }
        });

        deleteTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteData();
            }
        });

    }

    private void checkValidation() {

        if (name.isEmpty()) {
            updateTeacherName.setError("Empty");
            updateTeacherName.requestFocus();

        } else if (post.isEmpty()) {

            updateTeacherPost.setError("Empty");

        } else if (email.isEmpty()) {

            updateTeacherEmail.setError("Empty");

        } else if (bitmap == null) {
            updateData(image);
        } else {

            uploadImage();
        }


    }

    private void updateData(String s) {

        HashMap hp = new HashMap();
        hp.put("name", name);
        hp.put("email", email);
        hp.put("post", post);
        hp.put("image", s);


        reference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {


                Toast.makeText(getApplicationContext(), "Teacher Updated Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Update_Faculty.class);

                //jate back press korle update activity te na ase se jonno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Context context;
                CharSequence text;
                Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void uploadImage() {

        //======================Progress========================================
        pd.setMessage("Uploading...");
        pd.setTitle("Please Wait...");
        pd.show();


        //======================Progress========================================


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImg = baos.toByteArray();


        //akhan theke firebase image send korbo
        final StorageReference filePath;

        //child r maddome firebase file create kora hoy
        filePath = storageReference.child("Teacher").child(finalImg + "jpg");

        //akhan theke firebase image upload kora hobe;
        final UploadTask uploadTask = filePath.putBytes(finalImg);

        uploadTask.addOnCompleteListener(Update_Teacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {
                    //jodi successfully upload hoye jay tahole image r path get korte hobe jate database store korte pari
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    updateData(downloadUrl);

                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void deleteData() {

        reference.child(category).child(uniqueKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                Toast.makeText(getApplicationContext(), "Teacher Deleted Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Update_Faculty.class);

                //jate back press korle update activity te na ase se jonno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),"Something Wrong!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void openGallery() {

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
            updateTeacherImage.setImageBitmap(bitmap);
        }
    }


}