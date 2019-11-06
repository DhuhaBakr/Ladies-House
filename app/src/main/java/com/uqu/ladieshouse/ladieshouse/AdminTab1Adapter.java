package com.uqu.ladieshouse.ladieshouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dhuha on 09/04/18.
 */

class AdminTab1Adapter extends RecyclerView.Adapter<AdminTab1Adapter.MyViewHolder>  {


    private final List<Client> clientsList;
    private final Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView client_name , client_email , client_phone;

        public MyViewHolder(View view) {
            super(view);
            client_name = (TextView) view.findViewById(R.id.client_name);
            client_email =(TextView) view.findViewById(R.id.client_email);
            client_phone =(TextView) view.findViewById(R.id.client_phone);

        }
    }

    public AdminTab1Adapter(List<Client> clientsList, Context context) {

        this.clientsList = clientsList;
        this.context=context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_tab1_clients_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Client client = clientsList.get(position);
        String name =client.getClientName();
        String email = client.getClientEmail();
        String phone =  client.getClientPhone();
        holder.client_name.setText(" الاسم:  "+ name);
        holder.client_email.setText("البريد الالكتروني:  "+ email );
        holder.client_phone.setText("الرقم :  "+phone);

    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }
}
