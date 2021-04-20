package com.mamunsproject.awesome_collage_application.Faculty;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mamunsproject.awesome_collage_application.Adapter.TeacherAdapter;
import com.mamunsproject.awesome_collage_application.Model.TeacherData;
import com.mamunsproject.awesome_collage_application.R;

import java.util.ArrayList;
import java.util.List;

public class Update_Faculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment, mechanicalDepartment, physicsDepartment, chemistryDepartment;
    private LinearLayout csNoData, mechNoData, physicsNoData,chemistryNoData;
    private List<TeacherData> list1, list2, list3 , list4;
    private TeacherAdapter adapter;

    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__faculty);

        csDepartment = findViewById(R.id.csDepartment);
        mechanicalDepartment = findViewById(R.id.mechanicalDepartment);
        physicsDepartment = findViewById(R.id.physicsDepartment);
        chemistryDepartment = findViewById(R.id.chemistryDepartment);

        csNoData = findViewById(R.id.csNoData);
        mechNoData = findViewById(R.id.mechNoData);
        physicsNoData = findViewById(R.id.physicsNoData);
        chemistryNoData = findViewById(R.id.chemistryNoData);


        reference = FirebaseDatabase.getInstance().getReference().child("teacher");

        csDepartment();
        mechanicalDepartment();
        physicsDepartment();
        chemistryDepartment();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_Faculty.this, AddTeacher.class));
            }
        });
    }

    private void csDepartment() {
        dbRef = reference.child("Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else {
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list1, Update_Faculty.this, "Computer Science");
                    csDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mechanicalDepartment() {
        dbRef = reference.child("Mechanical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    mechNoData.setVisibility(View.VISIBLE);
                    mechanicalDepartment.setVisibility(View.GONE);
                }else {
                    mechNoData.setVisibility(View.GONE);
                    mechanicalDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    mechanicalDepartment.setHasFixedSize(true);
                    mechanicalDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list2, Update_Faculty.this,"Mechanical");
                    mechanicalDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void physicsDepartment() {
        dbRef = reference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    physicsNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                }else {
                    physicsNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list3, Update_Faculty.this, "Physics");
                    physicsDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chemistryDepartment() {
        dbRef = reference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    chemistryNoData.setVisibility(View.VISIBLE);
                    chemistryDepartment.setVisibility(View.GONE);
                }else {
                    chemistryNoData.setVisibility(View.GONE);
                    chemistryDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    chemistryDepartment.setHasFixedSize(true);
                    chemistryDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list4, Update_Faculty.this, "Chemistry");
                    chemistryDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}