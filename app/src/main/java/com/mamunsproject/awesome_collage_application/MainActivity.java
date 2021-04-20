package com.mamunsproject.awesome_collage_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mamunsproject.awesome_collage_application.Faculty.Update_Faculty;
import com.mamunsproject.awesome_collage_application.Notice.Delete_Notice_Activite;
import com.mamunsproject.awesome_collage_application.Notice.Upload_Notice;

public class MainActivity extends AppCompatActivity  {

    private CardView uploadnotice,add_Gallery_Image,uploadEbookCardView,faculti,delete_notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        clickListener();

    }

    public void init(){

        uploadnotice=findViewById(R.id.add_Noticeid);
        add_Gallery_Image=findViewById(R.id.add_Gallery_Image);
        uploadEbookCardView=findViewById(R.id.add_Ebook);
        faculti=findViewById(R.id.faculti);
        delete_notice=findViewById(R.id.delete_notice);


    }


    public void clickListener(){
        uploadnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Upload_Notice.class));

            }
        });


        add_Gallery_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Upload_Image.class));

            }
        });

        uploadEbookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Upload_Pdf.class));

            }
        });

        faculti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Update_Faculty.class));

            }
        });

        delete_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Delete_Notice_Activite.class));

            }
        });


    }

}