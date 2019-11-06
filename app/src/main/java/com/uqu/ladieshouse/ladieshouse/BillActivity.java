package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class BillActivity extends AppCompatActivity {

    private DatabaseReference databaseReference ;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser ;
    private TextView OrderNoTake , DateTake , TimeTake ,ServiceTake ,ServiceProviderTake ,BillValueTake;
    private Button startServiseBtn;
    private String startService ,endService ;
    private String orderID , clientID , freelancerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_bill);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser =firebaseAuth.getCurrentUser();

        OrderNoTake = (TextView) findViewById(R.id.OrderNoTake);
        DateTake = (TextView) findViewById(R.id.DateTake);
        TimeTake = (TextView) findViewById(R.id.TimeTake);
        ServiceTake = (TextView) findViewById(R.id.ServiceTake);
        ServiceProviderTake = (TextView) findViewById(R.id.ServiceProviderTake);
        BillValueTake = (TextView) findViewById(R.id.BillValueTake);
        startServiseBtn = (Button) findViewById(R.id.start_service);

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
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        OrderNoTake.setText(value);
                        DateTake.setText(map.get("date"));
                        TimeTake.setText(map.get("time"));
                        String serviceID = map.get("serviceID");
                        String date = map.get("date");
                        String state = map.get("state");
                        clientID =map.get("clientID");
                        freelancerID = map.get("freelancerID");
                        if (date!= null &&  !state.equals("old")) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date thedate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(date);
                                String thedateString = sdf.format(thedate);
                                Calendar calendar = Calendar.getInstance();
                                Date datetoday =  calendar.getTime();
                                String today = sdf.format(datetoday);

                                Log.d("today  :", today);

                                if ( thedateString.equals(today)){

                                    startServiseBtn.setVisibility(View.VISIBLE);
                                    Log.d("istToday ? :", " date is today !!");
                                }
                                else {
                                    Log.d("istToday ? :", " date is  NOT today !!");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(" error in format", " catch error in format date");
                            }
                        }
                        try {
                            DatabaseReference ServicesDB = databaseReference.child("FreelancerServices").child(serviceID);
                            ServicesDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                    String serviceName = map.get("serviceName");
                                    String servicePrice = String.valueOf( map.get("servicePrice"));
                                    ServiceTake.setText(serviceName);
                                    BillValueTake.setText(servicePrice);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        catch (Exception e){ }
                        String FreelancerID = map.get("freelancerID");
                        DatabaseReference FreeLancerDB = databaseReference.child("FreeLancer").child(FreelancerID);
                        FreeLancerDB.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                String name = map.get("name");
                                ServiceProviderTake.setText( name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }catch (Exception e){

            }

            databaseReference.child("Order").child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                     startService = map.get("startService");
                     endService = map.get("endService");

                     if (startService != null && endService!= null  ) {
                         if (startService.equals("YES") ) {
                             startServiseBtn.setText("إنهاء الخدمة");
                         }
                         if (firebaseUser.getUid().equals(clientID)) {

                             startServiseBtn.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {

                                     if (startService.equals("NO") ) {
                                         databaseReference.child("Order").child(orderID).child("startService").setValue("YES");
                                         Toast.makeText(getApplicationContext(), "تم بدء الخدمة", Toast.LENGTH_LONG).show();
                                     }
                                      else {
                                         Toast.makeText(getApplicationContext(), "يتم إنهاء الخدمة من قبل مقدم الخدمة", Toast.LENGTH_LONG).show();
                                     }
                                 }
                             });
                         }
                         if (firebaseUser.getUid().equals(freelancerID)) {
                             startServiseBtn.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     if (startService.equals("YES") ) {
                                         databaseReference.child("Order").child(orderID).child("endService").setValue("YES");
                                         Toast.makeText(getApplicationContext(), "تم إنهاء الخدمة", Toast.LENGTH_LONG).show();
                                         databaseReference.child("Order").child(orderID).child("state").setValue("old");
                                         startActivity(new Intent(BillActivity.this, FreeLancerActivity.class));
                                         finish();
                                     } else {
                                         Toast.makeText(getApplicationContext(), "لم يتم بدء الخدمة من قبل العميل", Toast.LENGTH_LONG).show();
                                     }
                                 }
                             });
                         }

                     }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    public void serviceStarted(View view) {


            databaseReference.child("Order").child(orderID).child("startService").setValue("YES");
            if (startServiseBtn.getText().toString().equals("بدء الخدمة")) {
                startServiseBtn.setText("إنهاء الخدمة");
                Intent startIntent = new Intent(BillActivity.this, ClientActivity.class);
                startActivity(startIntent);
                finish();
                Toast.makeText(getApplicationContext(), "تم بدء الخدمة", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "إنهاء الخدمة عن طريق مقدم الخدمة", Toast.LENGTH_LONG).show();
            }

        if(firebaseUser.getUid().equals(freelancerID)) {
            databaseReference.child("Order").child(orderID).child("startServiceConfirm").setValue("YES");

            databaseReference.child("Order").child(orderID).child("state").setValue("old");
            Intent startIntent = new Intent(BillActivity.this, FreeLancerActivity.class);
            startActivity(startIntent);
            finish();
            Toast.makeText(getApplicationContext(), "تم إنهاء الخدمة", Toast.LENGTH_LONG).show();

        }

    }
}



