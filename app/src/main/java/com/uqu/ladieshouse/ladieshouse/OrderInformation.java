package com.uqu.ladieshouse.ladieshouse;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.GregorianCalendar;

public class OrderInformation extends AppCompatActivity {
String serviceID;
    private DatabaseReference mDatabase;
private TextView serviceNameTextView,freelancerNameTextView,servicePriceTextView;
private EditText locationEditText ,date,time,notesEditText;
    FreelancerServices service;
    FirebaseUser user;
    private Order order;
    FirebaseAuth firebaseAuth;
    private Calendar c;
    EditText timePickerText;
    private EditText dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_new_order_information);
        //to get the serviceID from the previous interface
        firebaseAuth=FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
       Bundle extras = getIntent().getExtras();
        serviceID = extras.getString("serviceID");

                serviceNameTextView=(TextView)findViewById(R.id.serviceNameTextView);
                freelancerNameTextView=(TextView)findViewById(R.id.freelancerNameTextView);
                servicePriceTextView=(TextView)findViewById(R.id.servicePriceTextView);
                locationEditText =(EditText) findViewById(R.id.locationEditText);
                date=(EditText) findViewById(R.id.dateText);
                time=(EditText)findViewById(R.id.timeText);
                notesEditText=(EditText)findViewById(R.id.notesEditText);



        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("FreelancerServices").child(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                service= dataSnapshot.getValue(FreelancerServices.class);

                servicePriceTextView.setText((""+service.getServicePrice()));
                serviceNameTextView.setText(service.getServiceName());


                mDatabase.child("FreeLancer").child(service.getFreelancerID()).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String freelancerName = dataSnapshot.getValue().toString();
                        Toast.makeText(OrderInformation.this,freelancerName,Toast.LENGTH_LONG).show();
                        freelancerNameTextView.setText(freelancerName);
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

        dateText =(EditText) findViewById(R.id.dateText) ;
        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        timePickerText = (EditText) findViewById(R.id.timeText);
        timePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });

    }
    public void reserveButton(View v) {

        mDatabase=FirebaseDatabase.getInstance().getReference();
        order = new Order();



        String orderKey =  mDatabase.child("Order").push().getKey();
        order.setDate(date.getText().toString());
        order.setFreelancerID(service.getFreelancerID());
        order.setLocation(locationEditText.getText().toString());
        order.setNotes(notesEditText.getText().toString());
        order.setServiceID(serviceID);
        order.setTime(time.getText().toString());
        order.setClientID(user.getUid());
        order.setConfirmation("NO");
        order.setStartService("NO");
        order.setEndService("NO");
        order.setState("current");

        mDatabase.child("Order").child(orderKey).setValue(order);
        Toast.makeText(OrderInformation.this,"تم عمل طلب",Toast.LENGTH_LONG).show();
        startActivity(new Intent(OrderInformation.this, ClientActivity.class).putExtra("orderID",orderKey));


    }


    private void datePicker() {
        c  = Calendar.getInstance();
        //Use the current date as the default date in the date picker
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int month, int day) {

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date thedate =  new GregorianCalendar(year, month, day).getTime();
                                String stringOfDate = sdf.format(thedate);
                                date.setText(stringOfDate);
                                Toast.makeText(getApplicationContext(), "selected date is " +stringOfDate, Toast.LENGTH_SHORT).show();
                            }
                        };

                DatePickerDialog datePicker =new DatePickerDialog(OrderInformation.this, android.R.style.Theme_Material_Light_Dialog, dateSetListener, year, month, day);
                // disable past dates
                datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                datePicker.show();
            }
        });
    }

    private void timePicker() {
        // Get open TimePickerDialog.
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        String format ="";

                        if (hour == 0) {
                            hour += 12;
                            format = "AM";
                        } else if (hour == 12) {
                            format = "PM";
                        } else if (hour > 12) {
                            hour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                      //  time.setText(hour+":"+minute+" "+format);
                        time.setText(new StringBuilder().append(hour).append(":").append(minute)
                                .append(" ").append(format));
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(OrderInformation.this, android.R.style.Theme_Material_Light_Dialog ,onTimeSetListener, hour, minute, is24Hour);

                // timePickerDialog.setIcon(R.drawable.if_snowman);
                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });
    }




}
