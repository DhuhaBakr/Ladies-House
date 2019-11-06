package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FreeLancerTab1Myservice extends Fragment {

    private ListView freelancerListView;
    private DatabaseReference mDatabase;
    private FreelancerTab1CustomAdapter adapter;
    private List<FreelancerServices> servicesList ;
    private FirebaseUser user;
    private Button addService ;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.freelancer_myservice, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        freelancerListView = (ListView) rootView.findViewById(R.id.servicesListView);
        addService = (Button) rootView.findViewById(R.id.add_service);
        servicesList = new ArrayList<>();

        if (servicesList!= null){
            servicesList.clear();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("FreelancerServices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //save each child we meet in the var child...
                for (DataSnapshot child : children) {
                    try {
                        FreelancerServices service = child.getValue(FreelancerServices.class);
                        //save the object into a list of service objects.
                        if (service.getFreelancerID().equals( user.getUid() )) {
                            servicesList.add(service);
                            adapter.notifyDataSetChanged();
                        }
                    }catch (ClassCastException e){
                        Toast.makeText(getContext(),  "خطأ لم يتم اضافة الخدمة"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }


                    freelancerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new FreelancerTab1CustomAdapter(getContext(), servicesList);
        freelancerListView.setAdapter(adapter);

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FreeLancerAddNewService.class);
                startActivity(intent);
            }
        });
        return rootView;
    }



}