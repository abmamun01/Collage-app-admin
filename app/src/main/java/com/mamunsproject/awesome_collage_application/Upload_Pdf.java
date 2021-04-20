package com.mamunsproject.awesome_collage_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Upload_Pdf extends AppCompatActivity {


    private CardView addPdf;
    private final int REQUEST = 1;
    private Bitmap bitmap;
    private EditText pdfTitle;
    private Button uploadPdfButton;
    private Uri pdfData;
    private TextView pdfTextview;
    private String pdfName, title;


    //jodi download urle kicu na thake tahole blank chole jabe
    private String downloadUrl="";

    private SweetAlertDialog pDialog;
    private ProgressDialog pd;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__pdf);

        init();
        clickListener();
    }

    private void init() {

        pd=new ProgressDialog(this);
        addPdf = findViewById(R.id.add_pdf);
        pdfTitle = findViewById(R.id.pdf_title);
        uploadPdfButton = findViewById(R.id.uplooadPdfButton);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        pdfTextview=findViewById(R.id.pdfTextView);

    }


    private void clickListener() {

        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallerry();
            }
        });

        uploadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title=pdfTitle.getText().toString();
                if (title.isEmpty()){
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();
                }else if (pdfData==null){

                    Toast.makeText(Upload_Pdf.this,"Please  upload pdf",Toast.LENGTH_SHORT).show();

                }else {
                    uploadPdf();
                }

            }
        });
    }

    private void uploadPdf() {

        pd.setTitle("Please Wait..." );
        pd.setMessage("Uploading Pdf...");
        pd.show();

        StorageReference reference=storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");

        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri=uriTask.getResult();
                uploadData(String.valueOf(uri));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void uploadData(String downloadUrl) {

        String uniqueKey=databaseReference.child("pdf").push().getKey();

        HashMap data=new HashMap();

        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);


        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Pdf Uploaded Successfully!",Toast.LENGTH_SHORT).show();

                pdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to Upload !",Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void openGallerry() {

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQUEST);



    }





    //========================For Sending pdf To Databases========================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK) {


            pdfData=data.getData();

            if (pdfData.toString().startsWith("content://")){

                Cursor cursor=null;

                try {
                    cursor=Upload_Pdf.this.getContentResolver().query(pdfData,null,null,null,null);

                    if (cursor!=null&& cursor.moveToFirst()){
                        pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if (pdfData.toString().startsWith("file://")){
                pdfName=new File(pdfData.toString()).getName();

            }

            pdfTextview.setText(pdfName);


        }

        //========================For Sending pdf To Databases========================================

    }




}