package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by TOSHIBA on 28/04/18.
 */

public class ClientRejectedPage  extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_rejected_page);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("orderID");
            //The key argument here must match that used in the other activity
            orderID = value;
        }
    }

    public void deleteOrder(View v){
        try {
            databaseReference.child("Order").child(orderID).setValue(null);
            startActivity(new Intent(ClientRejectedPage.this, ClientActivity.class));
            finish();
            Toast.makeText(getApplicationContext(), "تم حذف طلب الخدمة", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "خطأ لم يتم حذف طلب الخدمة", Toast.LENGTH_LONG).show();
        }
    }
}