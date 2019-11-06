package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class rejectedOrder extends AppCompatActivity {


    private TextView  DateTake , TimeTake ,ServiceTake ,ServiceProviderTake ;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_order);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        DateTake = (TextView) findViewById(R.id.DateTake);
        TimeTake = (TextView) findViewById(R.id.TimeTake);
        ServiceTake = (TextView) findViewById(R.id.ServiceTake);
        ServiceProviderTake = (TextView) findViewById(R.id.ServiceProviderTake);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("key");
            //The key argument here must match that used in the other activity
            orderID= value;
            try {
                DatabaseReference billDB = databaseReference.child("Order").child(value);

                billDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        DateTake.setText(map.get("date"));
                        TimeTake.setText(map.get("time"));
                        String serviceID = map.get("serviceID");


                        try {
                            DatabaseReference ServicesDB = databaseReference.child("FreelancerServices").child(serviceID);
                            ServicesDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                    String serviceName = map.get("serviceName");

                                    ServiceTake.setText(serviceName);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } catch (Exception e) {
                        }
                        String FreelancerID = map.get("freelancerID");
                        DatabaseReference FreeLancerDB = databaseReference.child("FreeLancer").child(FreelancerID);
                        FreeLancerDB.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                String name = map.get("name");
                                ServiceProviderTake.setText(name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }  }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }catch (Exception e){

            }

    }
}

public void deleteOrder(View view ){
    databaseReference = FirebaseDatabase.getInstance().getReference();
    databaseReference.child("Order").child(orderID).removeValue();
    Intent intent = new Intent(rejectedOrder.this, ClientActivity.class);
    startActivity(intent);
    finish();

}

}
