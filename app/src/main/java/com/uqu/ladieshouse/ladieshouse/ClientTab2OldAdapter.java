package com.uqu.ladieshouse.ladieshouse;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

/**
 * Created by dhuha on 28/03/18.
 */

class ClientTab2OldAdapter extends RecyclerView.Adapter<ClientTab2OldAdapter.MyViewHolder>  {


    private final List<Order> ordersList;
    private final Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView serviceID , FLname , service_expier_date;

        public MyViewHolder(View view) {
            super(view);
            serviceID = (TextView) view.findViewById(R.id.srfi_name);
            FLname =(TextView) view.findViewById(R.id.FLname);
            service_expier_date =(TextView) view.findViewById(R.id.service_date);

        }
    }

    public ClientTab2OldAdapter(List<Order> ordersList, Context context) {

        this.ordersList = ordersList;
        this.context=context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_tab1_reservation_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Order order = ordersList.get(position);
        DatabaseReference DB =  FirebaseDatabase.getInstance().getReference() ;
        try {
            DatabaseReference ServicesDB = DB.child("FreelancerServices").child(order.getServiceID());
            ServicesDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    String serviceName = map.get("serviceName");
                    holder.serviceID.setText(" الخدمة:  " + serviceName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){

        }
        try {
            DatabaseReference FreeLancerDB = DB.child("FreeLancer").child(order.getFreelancerID());
            FreeLancerDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    String name = map.get("name");
                    holder.FLname.setText("مقدم الخدمة:  " + name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
        holder.service_expier_date.setText("تاريخ الخدمة :  "+ order.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value=order.getOrderID();
                Intent i = new Intent(context, BillActivity.class);
                i.putExtra("key",value);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }
}
