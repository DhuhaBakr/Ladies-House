package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FreeLancerAddNewService extends AppCompatActivity {
    private EditText serviceName;
    private  EditText servicePrice;
    private EditText serviceDescription;
    private Spinner serviceCatagory;
    private Button enterService;
    private DatabaseReference database;
    private String selectedCatagory;
    private FreelancerServices serviceObj;
    private String freelancerID;
    private FirebaseUser user;
    private ArrayAdapter<CharSequence> catagoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.freelancer_add_new_service);


//to get uthe current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        // get from the interface
        serviceName = (EditText)findViewById(R.id.serviceName);
        servicePrice = (EditText)findViewById(R.id.servicePrice);
        serviceDescription = (EditText)findViewById(R.id.serviceDescription);
        serviceCatagory = (Spinner) findViewById(R.id.serviceCatagory);
        enterService =(Button)findViewById(R.id.enterService);



        database=FirebaseDatabase.getInstance().getReference();
        serviceObj = new FreelancerServices();
        try {
//passing the string from string file to the catagoryAdapter :
            catagoryAdapter=ArrayAdapter.createFromResource(this,R.array.catagory,android.R.layout.simple_spinner_item);
            catagoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            serviceCatagory.setAdapter(catagoryAdapter);
            serviceCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCatagory = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), selectedCatagory, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception e){}
    }


    public void enterService(View view){

        try {
            String servicekey =  database.child("FreelancerServices").push().getKey();
            getValues();
            database.child("FreelancerServices").child(servicekey).setValue(serviceObj);
            Toast.makeText(FreeLancerAddNewService.this, "تمت اضافة الخدمة", Toast.LENGTH_LONG).show();
            startActivity(new Intent(FreeLancerAddNewService.this, FreeLancerActivity.class));
            finish();
        }catch (Exception e){
            Toast.makeText(FreeLancerAddNewService.this,  "خطأ لم يتم اضافة الخدمة"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }


    }


    private void getValues(){
        serviceObj.setServiceName(serviceName.getText().toString());
        serviceObj.setServiceDescription(serviceDescription.getText().toString());
        serviceObj.setServicePrice(Integer.parseInt((servicePrice.getText().toString())));
        serviceObj.setServiceCatagory(selectedCatagory);
        serviceObj.setFreelancerID(user.getUid());

    }
}