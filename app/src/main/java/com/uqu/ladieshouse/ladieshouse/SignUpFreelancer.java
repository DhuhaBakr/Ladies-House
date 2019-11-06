package com.uqu.ladieshouse.ladieshouse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class SignUpFreelancer extends AppCompatActivity {

    EditText pass;
    EditText Email;
    EditText phone;
    EditText description;
    CheckBox City;
    FirebaseDatabase database;
    DatabaseReference ref;

    FirebaseAuth firebaseAuth;
    private Button buttonSignUpForFreeLancer;
    private DatabaseReference databaseReference;
    private EditText SignUpFreelancerUserText;
    private EditText SignUpFreelancerPhoneText;
    private EditText SignUpFreelancerdecText;
    private CheckBox ch1 , ch2 ,ch3, ch4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_freelancer);

        // getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();


        pass = (EditText) findViewById(R.id.SignPassText);
        Email = (EditText) findViewById(R.id.SignEmailText);
        SignUpFreelancerUserText = (EditText) findViewById(R.id.SignUpFreelancerUserText);
        SignUpFreelancerPhoneText = (EditText) findViewById(R.id.SignUpFreelancerPhoneText);
        SignUpFreelancerdecText = (EditText) findViewById(R.id.SignUpFreelancerdecText);
        ch1=(CheckBox)findViewById(R.id.MakkahcheckBox);
        ch2=(CheckBox)findViewById(R.id.JeddahcheckBox);
        ch3=(CheckBox)findViewById(R.id.RiydhcheckBox);
        ch4=(CheckBox)findViewById(R.id.MadinahcheckBox);

        buttonSignUpForFreeLancer = (Button) findViewById(R.id.buttonSignUpForFreeLancer);



    }

    private void getValues() {

    }


    public void SignupFreeLancer(View v) {
        String email = Email.getText().toString().trim();
        String password = pass.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if (task.isSuccessful()) {
                    continueSignup();
                    finish();
                   // startActivity(new Intent(getApplicationContext(), FreeLancerActivity.class));
                    Toast.makeText(SignUpFreelancer.this, " !!! DONE !!! ", Toast.LENGTH_LONG).show();
                } else {
                    //display some message here
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpFreelancer.this, "Registration Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    private void continueSignup(){
        // getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // getting data from the user
        String name = SignUpFreelancerUserText.getText().toString().trim();
        String phone = SignUpFreelancerPhoneText.getText().toString().trim();
        String description = SignUpFreelancerdecText.getText().toString().trim();
        String result = "";
        if(ch1.isChecked()){
            result+="مكة المكرمة";
        }
         if(ch2.isChecked()){
            result+=", جدة";
        }
         if(ch3.isChecked()){
            result+=", الرياض";
        }
         if(ch4.isChecked()){
            result+=", المدينة";
        }

        //saving data to firebase database
        databaseReference.child("Users").child(user.getUid()).setValue("FreeLancer");
        databaseReference.child("FreeLancer").child(user.getUid()).child("name").setValue(name);
        databaseReference.child("FreeLancer").child(user.getUid()).child("phone").setValue(phone);
        databaseReference.child("FreeLancer").child(user.getUid()).child("description").setValue(description);
        databaseReference.child("FreeLancer").child(user.getUid()).child("location").setValue(result);

    }


}
