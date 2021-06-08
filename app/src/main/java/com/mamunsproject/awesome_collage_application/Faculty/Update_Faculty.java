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
    public RecyclerView banglaDepartment, englishDepartment, physicsDepartment, chemistryDepartment,historyDepartment,commerceDeparment,ictDepartment,higherMathDepartment,biologyDepartment;
    public LinearLayout banglaNoData, englishNoData, physicsNoData, chemistryNoData,historyNoData,commerceNoData,ictNoData,higherMathNoData,biologyNoData;
    private List<TeacherData> list1, list2, list3, list4,list5,list6,list7,list8,list9;
    private TeacherAdapter adapter;

    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__faculty);

        banglaDepartment = findViewById(R.id.banglaDepartment);
        englishDepartment = findViewById(R.id.englishDepartment);
        physicsDepartment = findViewById(R.id.physicsDepartment);
        chemistryDepartment = findViewById(R.id.chemistryDepartment);
        historyDepartment = findViewById(R.id.historyDepartment);
        commerceDeparment = findViewById(R.id.commerceDepartment);
        ictDepartment = findViewById(R.id.ictDepartment);
        biologyDepartment = findViewById(R.id.bioLogyDepartment);
        higherMathDepartment=findViewById(R.id.higherMahtDepartment);



        banglaNoData = findViewById(R.id.banglaNoData);
        englishNoData = findViewById(R.id.englishNoData);
        physicsNoData = findViewById(R.id.physicsNoData);
        chemistryNoData = findViewById(R.id.chemistryNoData);
        historyNoData = findViewById(R.id.historyNoData);
        commerceNoData = findViewById(R.id.commerceNoData);
        ictNoData = findViewById(R.id.ictNoData);
        biologyNoData = findViewById(R.id.bioLogyNoData);
        higherMathNoData=findViewById(R.id.higherMathNoData);



        reference = FirebaseDatabase.getInstance().getReference().child("teacher");

        banglaDepartment();
        historyDepartment();
        physicsDepartment();
        chemistryDepartment();
        higherMathDepartment();
        englishDepartment();
        commerceDepartment();
        iCTDepartment();
        biologyDepartment();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Update_Faculty.this, AddTeacher.class));
            }
        });
    }

    private void banglaDepartment() {
        dbRef = reference.child("Bangla");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    banglaNoData.setVisibility(View.VISIBLE);
                    banglaDepartment.setVisibility(View.GONE);
                } else {
                    banglaNoData.setVisibility(View.GONE);
                    banglaDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    banglaDepartment.setHasFixedSize(true);
                    banglaDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list1, Update_Faculty.this, "Bangla");
                    banglaDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void historyDepartment() {
        dbRef = reference.child("History");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    historyNoData.setVisibility(View.VISIBLE);
                    historyDepartment.setVisibility(View.GONE);
                } else {
                    historyNoData.setVisibility(View.GONE);
                    historyDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    historyDepartment.setHasFixedSize(true);
                    historyDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list2, Update_Faculty.this, "History");
                    historyDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void higherMathDepartment() {
        dbRef = reference.child("Higher Math");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    historyNoData.setVisibility(View.VISIBLE);
                    higherMathDepartment.setVisibility(View.GONE);
                } else {
                    historyNoData.setVisibility(View.GONE);
                    higherMathDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    higherMathDepartment.setHasFixedSize(true);
                    higherMathDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list3, Update_Faculty.this, "Higher Math");
                    higherMathDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void englishDepartment() {
        dbRef = reference.child("English");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    englishNoData.setVisibility(View.VISIBLE);
                    englishDepartment.setVisibility(View.GONE);
                } else {
                    englishNoData.setVisibility(View.GONE);
                    englishDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    englishDepartment.setHasFixedSize(true);
                    englishDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list4, Update_Faculty.this, "English");
                    englishDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void commerceDepartment() {
        dbRef = reference.child("Commerce");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list5 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    commerceNoData.setVisibility(View.VISIBLE);
                    commerceDeparment.setVisibility(View.GONE);
                } else {
                    commerceNoData.setVisibility(View.GONE);
                    commerceDeparment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list5.add(data);
                    }
                    banglaDepartment.setHasFixedSize(true);
                    banglaDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list5, Update_Faculty.this, "Commerce");
                    commerceDeparment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void iCTDepartment() {
        dbRef = reference.child("ICT");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list6 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    ictNoData.setVisibility(View.VISIBLE);
                    ictDepartment.setVisibility(View.GONE);
                } else {
                    ictNoData.setVisibility(View.GONE);
                    ictDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list6.add(data);
                    }
                    ictDepartment.setHasFixedSize(true);
                    ictDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list6, Update_Faculty.this, "ICT");
                    ictDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Update_Faculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void biologyDepartment() {
        dbRef = reference.child("Biology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list7 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    biologyNoData.setVisibility(View.VISIBLE);
                    biologyDepartment.setVisibility(View.GONE);
                } else {
                    biologyNoData.setVisibility(View.GONE);
                    biologyDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list7.add(data);
                    }
                    biologyDepartment.setHasFixedSize(true);
                    biologyDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list7, Update_Faculty.this, "Biology");
                    biologyDepartment.setAdapter(adapter);
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
                list8 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    physicsNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                } else {
                    physicsNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list8.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list8, Update_Faculty.this, "Physics");
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
                list9 = new ArrayList<>();
                if (!dataSnapshot.exists()) {
                    chemistryNoData.setVisibility(View.VISIBLE);
                    chemistryDepartment.setVisibility(View.GONE);
                } else {
                    chemistryNoData.setVisibility(View.GONE);
                    chemistryDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list9.add(data);
                    }
                    chemistryDepartment.setHasFixedSize(true);
                    chemistryDepartment.setLayoutManager(new LinearLayoutManager(Update_Faculty.this));
                    adapter = new TeacherAdapter(list9, Update_Faculty.this, "Chemistry");
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