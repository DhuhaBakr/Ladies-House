package com.uqu.ladieshouse.ladieshouse;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ClientOrderPage extends AppCompatActivity {


    private String orderID;
    private DatabaseReference mDatabase;
    private TextView serviceName,servicePrice;
    private Order order ;
    private TextView freelncerName;
    private EditText date,time;
    private EditText notesTextView , location;
    private Button editReservationButton , deleteReservationButton;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_order_page);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("orderID");
            //The key argument here must match that used in the other activity
            orderID = value;

            editReservationButton = (Button) findViewById(R.id.editReservationButton);
            deleteReservationButton = (Button) findViewById(R.id.deleteReservationButton);
            serviceName = (TextView) findViewById(R.id.serviceName);
            servicePrice = (TextView) findViewById(R.id.servicePrice);
            freelncerName = (TextView) findViewById(R.id.freelancerName);
            location = (EditText) findViewById(R.id.locationEditText);
            date = (EditText) findViewById(R.id.dateText);
            time = (EditText) findViewById(R.id.timeText);
            notesTextView = (EditText) findViewById(R.id.notesEditText);

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

                                mDatabase.child("FreeLancer").child(order.getFreelancerID()).child("name").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String freelancername = dataSnapshot.getValue().toString();
                                        freelncerName.setText(freelancername);
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

    public void deleteReservationButton(View v) {
        Intent startIntent = new Intent(ClientOrderPage.this, ClientConfirmOrderDeletion.class).putExtra("orderID",orderID);
        startActivity(startIntent);
        finish();
    }

    public void editReservationButton(View v){
        if (editReservationButton.getText().equals("تعديل الحجز")) {
            editReservationButton.setText(" حفظ التعديل ");
            date.setEnabled(true);
            date.setClickable(true);
            datePicker();
            time.setEnabled(true);
            time.setClickable(true);
            timePicker();
            notesTextView.setEnabled(true);
            notesTextView.setFocusable(true);
        }
      else {
            try {
                editReservationButton.setText(" تعديل الحجز ");
                date.setEnabled(false);
                date.setClickable(false);
                time.setEnabled(false);
                time.setClickable(false);
                notesTextView.setEnabled(false);
                notesTextView.setFocusable(false);
                mDatabase.child("Order").child(orderID).child("date").setValue(date.getText().toString());
                mDatabase.child("Order").child(orderID).child("time").setValue(time.getText().toString());
                if (notesTextView.getText().toString() != null) {
                    mDatabase.child("Order").child(orderID).child("notes").setValue(notesTextView.getText().toString());
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "  خطأ عند تعديل البيانات  " , Toast.LENGTH_SHORT).show();
            }
        }
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

                DatePickerDialog datePicker =new DatePickerDialog(ClientOrderPage.this, android.R.style.Theme_Material_Light_Dialog, dateSetListener, year, month, day);
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
                        time.setText(new StringBuilder().append(hour).append(":").append(minute)
                                .append(" ").append(format));
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(ClientOrderPage.this, android.R.style.Theme_Material_Light_Dialog ,onTimeSetListener, hour, minute, is24Hour);

                // timePickerDialog.setIcon(R.drawable.if_snowman);
                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });
    }



}
