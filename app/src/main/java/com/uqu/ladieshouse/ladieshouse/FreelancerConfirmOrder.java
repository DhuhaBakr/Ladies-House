package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Map;

public class FreelancerConfirmOrder extends AppCompatActivity {

    // private DatabaseReference databaseٍٍReference;
    // private FirebaseAuth firebaseAuth1;
    //private FirebaseUser FirebaseUser;
    //private EditText resDetiles;

    private String orderID;
    private DatabaseReference mDatabase;
    private TextView serviceName,servicePrice;
    private TextView clientName;
    private EditText dateText,timeText;
    private EditText notesTextView , locationText;
    private Button confirmReservationButton , deleteReservationButton;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freelancer_confirmorderpage);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("orderID");
            //The key argument here must match that used in the other activity
            orderID = value;

            confirmReservationButton = (Button) findViewById(R.id.confirmReservationButton);
            deleteReservationButton = (Button) findViewById(R.id.deleteReservationButton);
            serviceName = (TextView) findViewById(R.id.serviceName);
            servicePrice = (TextView) findViewById(R.id.servicePrice);
            clientName = (TextView) findViewById(R.id.clientName);
            locationText = (EditText) findViewById(R.id.locationEditText);
            dateText = (EditText) findViewById(R.id.dateText);
            timeText = (EditText) findViewById(R.id.timeText);
            notesTextView = (EditText) findViewById(R.id.notesEditText);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            try {
                if (orderID != null) {
                    mDatabase.child("Order").child(orderID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                String serviceID, date, time, notes, location, clientID, confirmation ;

                                try {
                                    confirmation = map.get("confirmation");

                                    date = map.get("date");
                                    if (date != null) {
                                        dateText.setText(date);
                                    }
                                    time = map.get("time");
                                    if (time != null) {
                                        timeText.setText(time);
                                    }
                                    notes = map.get("notes");
                                    if (notes == null) {
                                        notesTextView.setText("لايوجد ملاحظات");
                                    } else {
                                        notesTextView.setText(notes);
                                    }
                                    location = map.get("location");
                                    if (location != null) {
                                        locationText.setText(location);
                                    }
                                    clientID = map.get("clientID");
                                    if (clientID != null) {
                                        try {
                                            DatabaseReference FreeLancerDB = mDatabase.child("Client").child(clientID);
                                            FreeLancerDB.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                                    String clientname = map.get("name").toString();
                                                    clientName.setText( clientname);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }catch (Exception e){

                                        }
                                    }
                                    serviceID = map.get("serviceID");
                                    if (serviceID !=null) {
                                        mDatabase.child("FreelancerServices").child(serviceID).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                                String servicename = map.get("serviceName").toString();
                                                if (servicename != null) {
                                                    serviceName.setText(servicename);
                                                }
                                                String price = String.valueOf( map.get("servicePrice"));
                                                if (price != null) {
                                                    servicePrice.setText(price);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            } catch (Exception e) {
            }
        }
    }

    public void deleteReservationButton(View v) {
        mDatabase.child("Order").child(orderID).child("confirmation").setValue("rejected");
        startActivity(new Intent(FreelancerConfirmOrder.this, FreeLancerActivity.class));
        Toast.makeText(getApplicationContext(), "  تم رفض الحجز " , Toast.LENGTH_SHORT).show();
        finish();
    }

    public void confirmReservationButton(View v){

        mDatabase.child("Order").child(orderID).child("confirmation").setValue("YES");
        startActivity(new Intent(FreelancerConfirmOrder.this, FreeLancerActivity.class));
        Toast.makeText(getApplicationContext(), " تم تأكيد الحجز " , Toast.LENGTH_SHORT).show();
        finish();

    }

}
