package com.uqu.ladieshouse.ladieshouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by dhuha on 09/04/18.
 */

class AdminTab2Adapter extends RecyclerView.Adapter<AdminTab2Adapter.MyViewHolder>  {


    private  List<FreeLancer> FreeLancersList;
    private  Context context;
    private  DatabaseReference DB;

    public Button Services, clients;
    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView client_name , client_email , client_phone;


        public MyViewHolder(View view) {
            super(view);
            client_name = (TextView) view.findViewById(R.id.client_name);
            client_email =(TextView) view.findViewById(R.id.client_email);
            client_phone =(TextView) view.findViewById(R.id.client_phone);

            clients =(Button) view.findViewById(R.id.clients);
            Services =(Button) view.findViewById(R.id.Services);

        }

    }

    public AdminTab2Adapter(List<FreeLancer> FreeLancersList, Context context) {

        this.FreeLancersList = FreeLancersList;
        this.context=context;
        DB =  FirebaseDatabase.getInstance().getReference() ;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_tab2_freelancer_list_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final FreeLancer freeLancer = FreeLancersList.get(position);

        String name =freeLancer.getFreeLancerName();
        String email = freeLancer.getFreeLancerEmail();
        String phone =  freeLancer.getFreeLancerPhone();
        holder.client_name.setText(" الاسم:  "+ name);
        holder.client_email.setText("البريد الالكتروني:  "+ email );
        holder.client_phone.setText("الرقم :  "+phone);

        Services.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (AdminTab2.freeLancersServicespopup(v ,freeLancer.getFreeLancerID())) {
                    new AdminTab2().update(v);
                }

            }
        });
        clients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AdminTab2.freeLancersClientspopup(v ,freeLancer.getFreeLancerID());
            }
        });

    }



    @Override
    public int getItemCount() {
        return FreeLancersList.size();
    }


}
