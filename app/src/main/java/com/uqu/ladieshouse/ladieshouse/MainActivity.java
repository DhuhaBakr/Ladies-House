package com.uqu.ladieshouse.ladieshouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Context context;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    EditText Email, password;
    FirebaseAuth firebaseAuth ;
    FirebaseUser firebaseUser;

    private TextView forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        context = getApplicationContext();
        firebaseAuth = FirebaseAuth.getInstance();
        // getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();


            Email = (EditText) findViewById(R.id.signInUsernameText);
            password = (EditText) findViewById(R.id.signInPassText);


        forgotpassword= (TextView)findViewById(R.id.forgetpasswordbutton);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this,ResetPassword.class);
                startActivity(i);

            }
        });

    }

    private void client() {

        Toast.makeText(MainActivity.this, " !! signed in as client !!! ", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), ClientActivity.class));
        finish();
    }

    private void freeLancer() {

        Toast.makeText(MainActivity.this, " !! signed in as freelancer !!! ", Toast.LENGTH_LONG).show();
        FirebaseUser cuttentuser = firebaseAuth.getCurrentUser();

        DatabaseReference ClientDB = databaseReference.child("FreeLancer").child(cuttentuser.getUid());
        ClientDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                try {
                    String state = String.valueOf(map.get("state"));
                    if (state.equals("blocked")){
                        Intent intent1 = new Intent(context, FreelancerBlockPage.class);
                        startActivity(intent1);
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), FreeLancerActivity.class));
                    }
                }catch (Exception e){
                    startActivity(new Intent(getApplicationContext(), FreeLancerActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

        //finish();

    }

    public void admin(){
       Toast.makeText(MainActivity.this, " !! signed in as admin !!! ", Toast.LENGTH_LONG).show();
       startActivity(new Intent(getApplicationContext(), AdminActivity.class));
       finish();
   }
    public void signinButton(View v) {

        String email = Email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (email!=null && pass !=null && !email.equals("")&& ! pass.equals("")) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //if the task is successfull
                                if (task.isSuccessful()) {

                                    //start the profile activity
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    databaseReference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            try {
                                                String type = dataSnapshot.getValue().toString();
                                                if (type.equals("Admin")) {
                                                    admin();
                                                }
                                                if (type.equals("FreeLancer")) {
                                                    freeLancer();
                                                }
                                                if (type.equals("Client")) {
                                                    client();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(MainActivity.this, " error in sign in", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity.this, "البريد أو كلمة المرور غير صحيحة", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }catch (Exception e){
                Toast.makeText(MainActivity.this, " error in sign in", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void newuserregisterbutton(View v) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }


}
