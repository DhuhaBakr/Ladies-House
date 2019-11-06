package com.uqu.ladieshouse.ladieshouse;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by dhuha on 09/04/18.
 */

public  class FreeLancerTab4Profile extends Fragment{

    private static Context context;
    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference DB;
    private static FirebaseUser firebaseUser;

    Button changePass , SignOut ,changePhone;
    private EditText clientNameEditText , client_email_text, client_phone_text;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.freelancer_tab4_profile, container, false);

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        DB =  FirebaseDatabase.getInstance().getReference() ;
        firebaseUser = firebaseAuth.getCurrentUser();

        client_email_text = (EditText)  rootView.findViewById(R.id.client_email_text);
        clientNameEditText = (EditText)  rootView.findViewById(R.id.clientNameEditText);
        client_phone_text = (EditText)  rootView.findViewById(R.id.client_phone_text);
        changePass = (Button) rootView.findViewById(R.id.change_pass);
        changePhone = (Button) rootView.findViewById(R.id.change_phone);
        SignOut  = (Button) rootView.findViewById(R.id.sign_out);
        client_email_text.setText(firebaseUser.getEmail().toString());
        final DatabaseReference FreeLancerDB = DB.child("FreeLancer").child(firebaseUser.getUid());

        FreeLancerDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                try {
                    clientNameEditText.setText(map.get("name"));
                    client_phone_text.setText(map.get("phone"));
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });


        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Clicked on change pass Button", Toast.LENGTH_LONG).show();
                changePassPopup(v);

            }
        });

        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (changePhone.getText().equals("تغيير الرقم")) {
                 changePhone.setText(" حفظ التغيير ");
                 client_phone_text.setEnabled(true);
                }
                else {
                    if(client_phone_text.getText() !=null) {
                        try {
                            changePhone.setText("تغيير الرقم");
                            client_phone_text.setEnabled(false);
                            FreeLancerDB.child("phone").setValue(client_phone_text.getText().toString());
                            Toast.makeText(context, "Clicked on change phone Button", Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                        }
                    }
                }


            }
        });

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(context, "تم تسجيل الخروج", Toast.LENGTH_LONG).show();
                startActivity(new Intent(context, MainActivity.class));
                getActivity().finish();
            }
        });

        return rootView;
    }




    public static void changePassPopup(View view) {
        // https://stackoverflow.com/questions/23028480/android-how-to-create-popup-window

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.admin_tab3_change_pass_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        ( (Button) popupView.findViewById(R.id.change_pass_button)).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            public void onClick(View arg0) {

                String pass1 = ((EditText) popupView.findViewById(R.id.new_pass_1_EditText)).getText().toString();
                String pass2 = ((EditText) popupView.findViewById(R.id.new_pass_2_EditText)).getText().toString();


                if (pass1.equals(pass2) && !pass1.isEmpty() && !pass1.equals("")){

                    firebaseUser.updatePassword(pass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("admin password change :", "User password updated.");
                                popupWindow.dismiss();
                                Toast.makeText(context, " تم تغيير كلمة المرور " , Toast.LENGTH_LONG).show();

                            }
                            if (!task.isSuccessful()) {
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.d("admin password change :", e.getMessage());
                                    }
                                });
                            }
                        }
                    });

                }
                if ( !pass1.equals("")){
                    Toast.makeText(context, " !!! null value !!! " , Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, " الكلمتين غير متطابقتين " , Toast.LENGTH_LONG).show();
                }
            }
        });



    }



}
