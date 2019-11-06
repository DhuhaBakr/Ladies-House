package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientConfirmOrderDeletion extends AppCompatActivity {
private Button delete;
    private String orderID;
private FirebaseDatabase firebaseDatabase;
private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_confirm_order_deletion);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("orderID");

            orderID = value;

            delete = (Button) findViewById(R.id.deleteOrderButton);

            reference = FirebaseDatabase.getInstance().getReference();
        }
    }


   public void deleteOrder(View view) {
    try {


        reference.child("Order").child(orderID).removeValue();
        Toast.makeText(ClientConfirmOrderDeletion.this, "   تم حذف الطلب   ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ClientConfirmOrderDeletion.this, ClientActivity.class);
        startActivity(intent);
        finish();
    }
    catch (Exception e){
        Toast.makeText(ClientConfirmOrderDeletion.this, "   خطأ عند حذف الطلب   ", Toast.LENGTH_LONG).show();
    }
   }
}
