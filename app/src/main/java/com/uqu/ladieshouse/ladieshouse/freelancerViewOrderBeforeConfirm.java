package com.uqu.ladieshouse.ladieshouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class freelancerViewOrderBeforeConfirm extends AppCompatActivity {
    private String orderID;
    private DatabaseReference mDatabase;
    private TextView serviceName,servicePrice, date,time,notesTextView , location;
    private Order order ;
    private TextView clientName;
    private Button acceptOrder , rejectOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_view_order_before_confirm);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("orderID");
            //The key argument here must match that used in the other activity
            orderID = value;
            serviceName = (TextView) findViewById(R.id.serviceName);
            servicePrice = (TextView) findViewById(R.id.servicePrice);
            //clientName = (TextView) findViewById(R.id.clientName);
            location = (TextView) findViewById(R.id.locationEditText);
            date = (TextView) findViewById(R.id.dateText);
            time = (TextView) findViewById(R.id.timeText);
            notesTextView = (TextView) findViewById(R.id.notesEditText);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            try {
                if (orderID!= null) {
                    mDatabase.child("Order").child(orderID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                order = dataSnapshot.getValue(Order.class);
                                date.setText(order.getDate());
                                time.setText(order.getTime());
                                if (order.getNotes() == null) {
                                    notesTextView.setText("لايوجد ملاحظات");
                                } else {
                                    notesTextView.setText(order.getNotes());
                                }

                                if (order.getLocation() != null) {
                                    location.setText(order.getLocation());
                                }

                                mDatabase.child("Client").child(order.getClientID()).child("name").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String clientNameS = dataSnapshot.getValue().toString();
                                        clientName.setText(clientNameS);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                mDatabase.child("FreelancerServices").child(order.getServiceID()).child("serviceName").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String servicename = dataSnapshot.getValue().toString();
                                        serviceName.setText(servicename);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                mDatabase.child("FreelancerServices").child(order.getServiceID()).child("servicePrice").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String price = dataSnapshot.getValue().toString();
                                        servicePrice.setText(price);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }catch (Exception e){ }
        }
    }







    public void rejectOrder(View view){
        mDatabase.child("Order").child(orderID).child("confirmation").setValue("rejected");

    }
    public void acceptOrder(View view){

        mDatabase.child("Order").child(orderID).child("confirmation").setValue("YES");
    }
}
