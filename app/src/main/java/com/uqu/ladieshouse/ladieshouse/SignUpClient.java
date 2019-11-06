package com.uqu.ladieshouse.ladieshouse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpClient extends AppCompatActivity {


    EditText clientName, pass, Email, phoneNo ;
    Button SignUpClientButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_client);

        // getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        Email = (EditText) findViewById(R.id.SignEmailText);
        pass = (EditText) findViewById(R.id.SignPassText);
        clientName = (EditText) findViewById(R.id.signUpUsernameClientText);
        phoneNo = (EditText) findViewById(R.id.signUpphneClientText);

        SignUpClientButton = (Button) findViewById(R.id.SignUpClientButton);

    }

    private void getValues() {

    }

    public void SignUpClientButton(View v) {
        String email = Email.getText().toString().trim();
        String password = pass.getText().toString().trim();


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if (task.isSuccessful()) {
                    continueSignup();
                    //startActivity(new Intent(getApplicationContext(), ClientActivity.class));
                    finish();
                    Toast.makeText(SignUpClient.this, " !!! DONE !!! ", Toast.LENGTH_LONG).show();
                } else {
                    //display some message here
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpClient.this, "Registration Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

public void backSignUpClient(View v){
    //Intent intent = new Intent(SignUpClient.this,SignUpEmailAndPassword.class);
    //startActivity(intent);
}

    private void continueSignup(){

        // getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String name = clientName.getText().toString().trim();
        String phone = phoneNo.getText().toString().trim();

        //saving data to firebase database
        databaseReference.child("Users").child(user.getUid()).setValue("Client");
        databaseReference.child("Client").child(user.getUid()).child("name").setValue(name);
        databaseReference.child("Client").child(user.getUid()).child("phone").setValue(phone);

    }


}