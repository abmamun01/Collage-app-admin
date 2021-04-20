package com.mamunsproject.awesome_collage_application.Notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mamunsproject.awesome_collage_application.Model.Notice_Data;
import com.mamunsproject.awesome_collage_application.R;

import java.util.ArrayList;
import java.util.List;

public class Delete_Notice_Activite extends AppCompatActivity {

    private RecyclerView deleteNoticeRecyclerView;
    private ProgressBar progressBar;
    private ArrayList<Notice_Data> list;
    private Notice_Adapter adapter;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__notice__activite);

        deleteNoticeRecyclerView=findViewById(R.id.deleteNoticeRecyclerviewid);
        progressBar=findViewById(R.id.progressbar_id);
        reference= FirebaseDatabase.getInstance().getReference().child("Notice");
        setRecyclerview();
        getNotice();

    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Notice_Data data=dataSnapshot.getValue(Notice_Data.class);
                    list.add(data);
                }

                adapter=new Notice_Adapter(Delete_Notice_Activite.this,list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                //Mane Recycclervewi theke jegula katbe jevabe katbe thik sei recyclerview ta k e aikhane pass kore dilam jate
                //delete gula o delete thaka
                deleteNoticeRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerview(){


        deleteNoticeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deleteNoticeRecyclerView.setHasFixedSize(true);



    }
}