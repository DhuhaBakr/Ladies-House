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

/**
 * Created by dhuha on 09/04/18.
 */

public class AdminTab1 extends Fragment {

    private RecyclerView admin_recycler_view_1;
    private AdminTab1Adapter  tab1adapter;


    private static Context context;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser cuttentuser;
    private static DatabaseReference DB;
    private TextView OrderNoTake;
   // private List<String> orderList;
    private List<Client> ClientssList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_tab1, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        DB =  FirebaseDatabase.getInstance().getReference() ;

        if (ClientssList != null) {
            ClientssList.clear();
        }

        DatabaseReference ClientDB = DB.child("Client");
        // OrderNoTake.setText(orderDB.getKey() );
        ClientDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String ClientID, clientName, clientEmail, clientPhone;
                    Map<String, String> map = (Map<String, String>) postSnapshot.getValue();
                    ClientID = dataSnapshot.getKey();
                    clientName = map.get("name");
                    clientEmail = map.get("email");
                    clientPhone = String.valueOf( map.get("phone"));
                    Client client = new Client(ClientID, clientName, clientEmail, clientPhone);
                    ClientssList.add(client);
                    tab1adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

        admin_recycler_view_1 = (RecyclerView) rootView.findViewById(R.id.admin_recycler_view_1);
        tab1adapter = new AdminTab1Adapter(ClientssList , getContext());
        admin_recycler_view_1.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        admin_recycler_view_1.setLayoutManager(mLayoutManager);
        admin_recycler_view_1.setItemAnimator(new DefaultItemAnimator());
        admin_recycler_view_1.setAdapter(tab1adapter);

        return rootView;
    }

}
