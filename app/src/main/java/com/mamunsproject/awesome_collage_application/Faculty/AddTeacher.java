package com.mamunsproject.awesome_collage_application.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.mamunsproject.awesome_collage_application.Model.TeacherData;
import com.mamunsproject.awesome_collage_application.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddTeacher extends AppCompatActivity {

    private ImageView addTeacherImage;
    private EditText addtecherName ,addtecherEmail,addtecherPost;
    private Spinner addTecherCategory;
    private Button addTeacherButton;
    private final int REQUEST=1;
    private Bitmap bitmap;
    private SweetAlertDialog pDialog;


    private String category;
    private String name,email,post,downloadUrl="";
    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference,db_ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        init();
        clickListener();

        String[] items=new String[]{"Select Category","Physics","Chemistry","Biology","Bangla","Commerce","ICT","Higher Math","English","History"};
        addTecherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));


    }

    private void init() {

        addTeacherImage=findViewById(R.id.add_techer_image);
        addtecherName=findViewById(R.id.add_teachername);
        addtecherEmail=findViewById(R.id.add_teacherEmail);
        addtecherPost=findViewById(R.id.add_teacherPost);
        addTeacherButton=findViewById(R.id.addTeacherButton);
        addTecherCategory=findViewById(R.id.add_teacher_category);

        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference= FirebaseStorage.getInstance().getReference();

        pd=new ProgressDialog(this);




    }


    private void clickListener() {

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });

        addTecherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=addTecherCategory.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();

            }
        });
    }

    private void checkValidation() {

        name=addtecherName.getText().toString();
        email=addtecherEmail.getText().toString();
        post=addtecherPost.getText().toString();

        if (name.isEmpty()){
            addtecherName.setError("Empty");
            addtecherName.requestFocus();
        }

               else if (email.isEmpty()){
            addtecherEmail.setError("Empty");
            addtecherEmail.requestFocus();
        }

               else if (post.isEmpty()){
            addtecherPost.setError("Empty");
            addtecherPost.requestFocus();
        }else if (category.equals("Select Category")){
                   Toast.makeText(getApplicationContext(),"Please Provide a category",Toast.LENGTH_SHORT ).show();

        }else if (bitmap==null){

                   insertData();
        } else {
                   uploadImage();

        }

    }



    private void uploadImage() {

        //======================Progress========================================
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
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
        filePath=storageReference.child("Teacher").child(finalImg+"jpg");

        //akhan theke firebase image upload kora hobe;
        final UploadTask uploadTask=filePath.putBytes(finalImg);

        uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    insertData();

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



    private void insertData() {

        reference=reference.child(category);
        final String uniqueKey=reference.push().getKey();

        //Notice Model r title pass korar jonno


        TeacherData teacherData=new TeacherData(name, email, post, downloadUrl, uniqueKey);

        //Firebase data store korar jonno

        reference.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                pd.dismiss();


                Toast.makeText(getApplicationContext(),"Teacher Added!",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();

                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();

                Log.d("TAG", "onFailure: ");

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
            addTeacherImage.setImageBitmap(bitmap);
        }
    }

}