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
import android.widget.TextView;
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

public  class AdminTab3Profile extends Fragment{


    private static Context context;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser cuttentuser;
    private static DatabaseReference DB;
    private static FirebaseUser firebaseUser;
    TextView AdminText ;
    Button changePass , SignOut;

    private TextView AdminNameText;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_tab3, container, false);

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        DB =  FirebaseDatabase.getInstance().getReference() ;
        firebaseUser = firebaseAuth.getCurrentUser();

        AdminText = (TextView)  rootView.findViewById(R.id.admin_email);
        AdminNameText = (TextView)  rootView.findViewById(R.id.admin_name);
        changePass = (Button) rootView.findViewById(R.id.change_pass);
        SignOut  = (Button) rootView.findViewById(R.id.sign_out);
        AdminText.setText(firebaseUser.getEmail().toString());
        DatabaseReference ClientDB = DB.child("Admin").child(firebaseUser.getUid());
        // OrderNoTake.setText(orderDB.getKey() );
        ClientDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                 AdminNameText.setText( "Hi "+map.get("name")+"  !");
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


                if (pass1.equals(pass2) && !pass1.equals(null)){

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
                else {
                    Toast.makeText(context, " الكلمتين غير متطابقتين " , Toast.LENGTH_LONG).show();
                }
            }
        });



    }



}
