package com.uqu.ladieshouse.ladieshouse;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.Map;


public class FreelancerTab3OldReservation extends Fragment {

    private List<Order> ordersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FreelancerTab2OldAdapter mAdapter;


    private static Context context;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser cuttentuser;
    private static DatabaseReference DB;
    private TextView OrderNoTake;
    private List<String> orderList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.freelancer_tab2_old_reservations, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        DB =  FirebaseDatabase.getInstance().getReference() ;
        cuttentuser = firebaseAuth.getCurrentUser();

        if (!ordersList.isEmpty()){
            ordersList.clear();
            mAdapter.notifyDataSetChanged();
        }

        DatabaseReference orderDB = DB.child("Order");
        orderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot orderSnapshot) {

                for (DataSnapshot dataSnapshot : orderSnapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    String orderID = dataSnapshot.getKey();
                    String serviceID, freelancerID, date, time, notes, location, clientID, confirmation, startService, endService, startServiceConfirm, endServiceConfirm, state;
                    freelancerID = map.get("freelancerID");
                    if (cuttentuser.getUid().equals(freelancerID)) {
                        try {
                            confirmation = map.get("confirmation");
                            state = map.get("state");
                            if (state.equals("old")) {
                                if( !confirmation.equals("rejected")) {
                                    serviceID = map.get("serviceID");
                                    clientID = map.get("clientID");
                                    date = map.get("date");
                                    time = map.get("time");
                                    notes = map.get("notes");
                                    location = map.get("location");
                                    startService = map.get("startService");
                                    endService = map.get("endService");
                                    startServiceConfirm = map.get("startServiceConfirm");
                                    endServiceConfirm = map.get("endServiceConfirm");
                                    //////// order
                                    Order order = new Order(orderID, serviceID, freelancerID, date, time, notes, location, clientID, confirmation, startService, endService, startServiceConfirm, endServiceConfirm, state);
                                    ordersList.add(order);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (Exception e){}
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view3);
        mAdapter = new FreelancerTab2OldAdapter(ordersList , getContext());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }




}
