package com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
    }

    public void homebuttonclient(View v) {

       // MyApplication.isclientorfreelancer=1;
        Intent intent = new Intent(SignUp.this, SignUpClient.class);
        startActivity(intent);
        finish();
    }

    public void homebuttonfreelancer(View v) {
        //MyApplication.isclientorfreelancer=2;
        Intent intent = new Intent(SignUp.this, SignUpFreelancer.class);
        startActivity(intent);
        finish();
    }
}
