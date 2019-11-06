package  com.uqu.ladieshouse.ladieshouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {


    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        passwordEmail = (EditText) findViewById(R.id.etPasswordEmail);
        resetPassword = (Button) findViewById(R.id.btnPasswordReset);


        firebaseAuth = FirebaseAuth.getInstance();


        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = passwordEmail.getText().toString().trim();

                if (useremail.equals("")) {
                    // Toast.makeText(R, "please enter your registered Email :,)", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ResetPassword.this, "من فضلك قم بإدخال البريد الإلكتروني المسجل:,)", Toast.LENGTH_SHORT).show();

                } else {

                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //  Log.e("Erro F" , task.toString());

                            if (task.isComplete()) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(ResetPassword.this, "تم إرسال كلمة المرور الى البريد الإلكتروني", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(ResetPassword.this, MainActivity.class));

                                }
                            } else {
                                Toast.makeText(ResetPassword.this, " لم يتم إرسال كلمة المرور", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ResetPassword.this, MainActivity.class));

                            }
                            //task.isComplete()

                        }
                    });

                }
            }
        });

    }
}
