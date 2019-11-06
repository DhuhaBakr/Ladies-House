package com.uqu.ladieshouse.ladieshouse;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ClientTab1ServicesView extends Fragment {
    private ListView freelancerListView;
    private DatabaseReference mDatabase;
    private ClientTab1CustomAdapter adapter;
    private List<FreelancerServices> servicesList;
    private Spinner serviceCatagory;
    private String selectedCatagory;
    private ArrayAdapter<CharSequence> catagoryAdapter;

    Context context;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.client_tab1, container, false);
        //  private String serviceID;

        context = getContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        serviceCatagory = (Spinner) rootView.findViewById(R.id.serviceCatagory);
        freelancerListView = (ListView) rootView.findViewById(R.id.servicesListView);
        servicesList = new ArrayList<>();


        try {
            // catagoryAdapter=ArrayAdapter.createFromResource(this,R.array.catagory,android.R.layout.simple_spinner_item);
            String[] categories = getResources().getStringArray(R.array.catagory);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            serviceCatagory.setAdapter(dataAdapter);
            serviceCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCatagory = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getContext(), selectedCatagory, Toast.LENGTH_LONG).show();
                    if (servicesList != null) {
                        servicesList.clear();
                    }
                    if (position != 0) {
                        prepareData(selectedCatagory);
                    }
                    else{

                        mDatabase.child("FreelancerServices").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                                //save each child we meet in the var child...
                                for (DataSnapshot child : children) {
                                    final FreelancerServices service = child.getValue(FreelancerServices.class);
                                    service.setServiceID(child.getKey());
                                    servicesList.add(service);
                                    adapter = new ClientTab1CustomAdapter(getContext(), servicesList);
                                    freelancerListView.setAdapter(adapter);

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {

        }


        freelancerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(mainpage.this,"serviceID"+view.getTag(),Toast.LENGTH_LONG).show();
                String servID = view.getTag().toString();
                // Toast.makeText(mainpage.this, servID, Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(context, OrderInformation.class).putExtra("serviceID", servID);
                startActivity(startIntent);

            }
        });
        return rootView;
    }


    void prepareData(final String serviceType) {
        if (servicesList != null) {
            servicesList.clear();
            adapter.notifyDataSetChanged();
        }
        DatabaseReference ServicesDB = mDatabase.child("FreelancerServices");
        ServicesDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot serviceSnapshot) {
                for (DataSnapshot dataSnapshot : serviceSnapshot.getChildren()) {
                    String ServiceID;
                    ServiceID = dataSnapshot.getKey();
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    if (map.get("serviceCatagory").equals(serviceType)) {
                        final FreelancerServices service = dataSnapshot.getValue(FreelancerServices.class);
                        service.setServiceID(dataSnapshot.getKey());
                        servicesList.add(service);
                        adapter = new ClientTab1CustomAdapter(getContext(), servicesList);
                        freelancerListView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

