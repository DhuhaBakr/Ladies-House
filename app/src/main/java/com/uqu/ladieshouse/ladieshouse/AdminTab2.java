package com.uqu.ladieshouse.ladieshouse;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by dhuha on 09/04/18.
 */

public  class AdminTab2 extends Fragment{


    private RecyclerView admin_recycler_view_1;
    private static AdminTab2Adapter  tab2adapter;


    private static Context context;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser cuttentuser;
    private static DatabaseReference DB;


    private static List<FreeLancer> FreeLancersList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_tab2, container, false);

        context = getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        DB =  FirebaseDatabase.getInstance().getReference() ;


        admin_recycler_view_1 = (RecyclerView) rootView.findViewById(R.id.admin_recycler_view_2);
        tab2adapter = new AdminTab2Adapter(FreeLancersList , context);
        admin_recycler_view_1.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        admin_recycler_view_1.setLayoutManager(mLayoutManager);
        admin_recycler_view_1.setItemAnimator(new DefaultItemAnimator());
        admin_recycler_view_1.setAdapter(tab2adapter);


        if (FreeLancersList != null || !FreeLancersList.isEmpty()) {
            FreeLancersList.clear();
            tab2adapter.notifyDataSetChanged();
        }

        DatabaseReference ClientDB = DB.child("FreeLancer");
        // OrderNoTake.setText(orderDB.getKey() );
        ClientDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String FreeLancerID,  FreeLancerName,  FreeLancerEmail,  FreeLancerPhone ;
                    Map<String, String> map = (Map<String, String>) postSnapshot.getValue();
                    FreeLancerID = postSnapshot.getKey();
                    FreeLancerName = map.get("name");
                    FreeLancerEmail = map.get("email");
                    FreeLancerPhone = map.get("phone");
                    FreeLancer freeLancer = new FreeLancer(FreeLancerID, FreeLancerName, FreeLancerEmail, FreeLancerPhone);
                    FreeLancersList.add(freeLancer);
                    tab2adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    public static void freeLancersClientspopup(View view, String freeLancerID) {
        // https://stackoverflow.com/questions/23028480/android-how-to-create-popup-window

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.admin_tab2_clients_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        final DatabaseReference Client =DB.child("Client");
        DatabaseReference FreelancerClients = DB.child("FreeLancerOrder").child(freeLancerID);
        ((TextView) popupView.findViewById(R.id.client_TextView)).setText(((TextView) popupView.findViewById(R.id.client_TextView)).getText()+"\n");
        ((TextView) popupView.findViewById(R.id.service_TextView)).setText(((TextView) popupView.findViewById(R.id.service_TextView)).getText()+"\n");
        FreelancerClients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int OrdersNum = (int) dataSnapshot.getChildrenCount();
                if (OrdersNum ==0){
                    popupWindow.dismiss();
                }
                else {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (final DataSnapshot clientSnapshot : postSnapshot.getChildren()) {
                            String clientID = clientSnapshot.getKey();
                            Client.child(clientID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                        String name = map.get("name");
                                        String serviceName = clientSnapshot.getValue().toString();
                                        String text = (String) ((TextView) popupView.findViewById(R.id.client_TextView)).getText();
                                        ((TextView) popupView.findViewById(R.id.client_TextView)).setText(text +"\n"+ name+ "\n" );
                                        String servicetext = (String) ((TextView) popupView.findViewById(R.id.service_TextView)).getText();
                                        ((TextView) popupView.findViewById(R.id.service_TextView)).setText(servicetext +"\n"+ serviceName + "\n");

                                    } catch (Exception e) {
                                        popupWindow.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    popupWindow.dismiss();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                popupWindow.dismiss();
                Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static boolean freeLancersServicespopup( View view, final String freeLancerID) {

        final boolean update = true;
        // https://stackoverflow.com/questions/23028480/android-how-to-create-popup-window

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.admin_tab2_services_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        DatabaseReference FreelancerServices = DB.child("FreelancerServices");
        FreelancerServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        for (DataSnapshot serviceSnapshot: dataSnapshot.getChildren()) {
                            Map<String, String> map = (Map<String, String>) serviceSnapshot.getValue();
                            String freelancerId = map.get("freelancerID");
                            String serviceCatageory = map.get("serviceCatagory");
                            if (freelancerId.equals(freeLancerID)) {
                                ((TextView) popupView.findViewById(R.id.Service_text)).setText("serviceCatageory : " + serviceCatageory);
                            }
                        }
                    }
                } catch (Exception e) {
                    // popupWindow.dismiss();
                    ((TextView) popupView.findViewById(R.id.Service_text)).setText(" no service " );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference Freelancer = DB.child("FreeLancer").child(freeLancerID);
        Freelancer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                try {
                    String state = map.get("state");
                    if (!state.equals(null)) {
                        ((Button) popupView.findViewById(R.id.delete_free_lancer)).setText("unblock" );

                    }
                }catch (Exception e) { }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show();
            }
        });

        ( (Button) popupView.findViewById(R.id.delete_free_lancer)).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            public void onClick(View arg0) {
                DatabaseReference Freelancer = DB.child("FreeLancer").child(freeLancerID);
                Freelancer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        try {
                            String state = map.get("state");
                            if (!state.equals(null)) {
                                DB.child("FreeLancer").child(freeLancerID).child("state").setValue(null);
                                popupWindow.dismiss();
                            }
                        }catch (Exception e) {
                            DB.child("FreeLancer").child(freeLancerID).child("state").setValue("blocked");
                            popupWindow.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Failed to load.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return update;
    }

    public void update(View view){
        // update the page after button clicked

        FragmentActivity activity = (FragmentActivity) view.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.detach( AdminTab2.this).attach(AdminTab2.this).commit();

    }

}
