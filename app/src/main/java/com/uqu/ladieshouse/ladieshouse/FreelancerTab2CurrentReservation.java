 package com.uqu.ladieshouse.ladieshouse;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

 public class FreelancerTab2CurrentReservation extends Fragment {



     private RecyclerView recyclerView2;
     private FreelancerTab3CurrentAdapter  tab2adapter;


     private static Context context;
     private static FirebaseAuth firebaseAuth;
     private static FirebaseUser cuttentuser;
     private static DatabaseReference DB;

     //////////// order ///////////////
     private List<Order> OrdersList = new ArrayList<>();

     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.freelancer_tab3_current_reservations, container, false);

         context =getContext();
         firebaseAuth = FirebaseAuth.getInstance();
         DB =  FirebaseDatabase.getInstance().getReference() ;
         cuttentuser = firebaseAuth.getCurrentUser();

         if (!OrdersList.isEmpty()){
             OrdersList.clear();
             tab2adapter.notifyDataSetChanged();
         }
         DatabaseReference orderDB = DB.child("Order");
         orderDB.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot orderSnapshot) {
                 for (DataSnapshot dataSnapshot : orderSnapshot.getChildren()) {
                     Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                     String orderID ,serviceID, freelancerID, date, time, notes, location, clientID, confirmation, startService, endService, startServiceConfirm, endServiceConfirm, state;
                     orderID =dataSnapshot.getKey();
                     freelancerID = map.get("freelancerID");
                     if (cuttentuser.getUid().equals(freelancerID)) {
                         state = map.get("state");
                         confirmation = String.valueOf(map.get("confirmation"));
                         try {
                             if (state.equals("current")) {
                                 if( ! confirmation.equals("rejected")) {
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
                                     OrdersList.add(order);
                                     tab2adapter.notifyDataSetChanged();
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

         recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recycler_view4);
         tab2adapter = new FreelancerTab3CurrentAdapter(OrdersList , getContext());
         recyclerView2.setHasFixedSize(true);
         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
         recyclerView2.setLayoutManager(mLayoutManager);
         recyclerView2.setItemAnimator(new DefaultItemAnimator());
         recyclerView2.setAdapter(tab2adapter);


         return rootView;
     }


 }
