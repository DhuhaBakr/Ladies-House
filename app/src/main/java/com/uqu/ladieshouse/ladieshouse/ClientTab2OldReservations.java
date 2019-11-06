package com.uqu.ladieshouse.ladieshouse;

/**
 * Created by dhuha on 05/03/18.
 */

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


public class ClientTab2OldReservations extends Fragment {

    private List<Order> ordersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClientTab2OldAdapter mAdapter;


    private static Context context;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser cuttentuser;
    private static DatabaseReference DB;
    private TextView OrderNoTake;
    private List<String> orderList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.client_tab1_oldreservations, container, false);

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
                    clientID = map.get("clientID");
                    if (cuttentuser.getUid().equals(clientID)) {
                        try {


                            state = map.get("state");
                            if (state.equals("old")) {
                                serviceID = map.get("serviceID");
                                freelancerID = map.get("freelancerID");
                                date = map.get("date");
                                time = map.get("time");
                                notes = map.get("notes");
                                location = map.get("location");
                                confirmation = map.get("confirmation");
                                startService = map.get("startService");
                                endService = map.get("endService");
                                startServiceConfirm = map.get("startServiceConfirm");
                                endServiceConfirm = map.get("endServiceConfirm");
                                //////// order
                                Order order = new Order(orderID, serviceID, freelancerID, date, time, notes, location, clientID, confirmation, startService, endService, startServiceConfirm, endServiceConfirm, state);
                                ordersList.add(order);
                                mAdapter.notifyDataSetChanged();
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


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new ClientTab2OldAdapter(ordersList , getContext());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }




}
