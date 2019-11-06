package com.uqu.ladieshouse.ladieshouse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by noufa on 11/04/2018 AD.
 */

public class ClientTab1CustomAdapter extends BaseAdapter{
private Context mContext;
private List<FreelancerServices> freelancerServicesList;

    public ClientTab1CustomAdapter(Context mContext, List<FreelancerServices> freelancerServicesList) {
        this.mContext = mContext;
        this.freelancerServicesList = freelancerServicesList;
    }

    @Override
    public int getCount() {
        return freelancerServicesList.size();
    }

    @Override
    public Object getItem(int position) {
        return freelancerServicesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    View v = View.inflate(mContext,R.layout.customlayout,null);
    TextView serviceNameTextView = (TextView)v.findViewById(R.id.serviceNameTextView);
        TextView servicePriceTextView = (TextView)v.findViewById(R.id.servicePriceTextView);
        TextView serviceDescriptionTextView = (TextView)v.findViewById(R.id.serviceDescriptionTextView);
        TextView hiddenFreelancerID = (TextView)v.findViewById(R.id.hiddenFreelancerID);
        TextView hiddenServiceID = (TextView)v.findViewById(R.id.hiddenServiceID);
        serviceNameTextView.setText(freelancerServicesList.get(position).getServiceName());
        servicePriceTextView.setText(String.valueOf(freelancerServicesList.get(position).getServicePrice())+" SR");
        serviceDescriptionTextView.setText(freelancerServicesList.get(position).getServiceDescription());
        hiddenFreelancerID.setText(freelancerServicesList.get(position).getFreelancerID());
        hiddenServiceID.setText(freelancerServicesList.get(position).getServiceID());
       // hiddenServiceID.setText(freelancerServicesList.get(position).getServiceDescription());
v.setTag(freelancerServicesList.get(position).getServiceID());
        return v;
    }
}
