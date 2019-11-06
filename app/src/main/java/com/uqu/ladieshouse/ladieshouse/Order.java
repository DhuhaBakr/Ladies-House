package com.uqu.ladieshouse.ladieshouse;

/**
 * Created by noufa on 12/04/2018 AD.
 */

public class Order {
    String orderID, serviceID , freelancerID,date,time,notes,location,clientID,confirmation, startService,EndService,startServiceConfirm,endServiceConfirm,state;

    public Order() {
    }

    public Order(String orderID, String serviceID, String freelancerID, String date, String time, String notes, String location, String clientID, String confirmation, String startService, String endService, String startServiceConfirm, String endServiceConfirm, String state) {
        this.orderID =orderID;
        this.serviceID = serviceID;
        this.freelancerID = freelancerID;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.location = location;
        this.clientID = clientID;
        this.confirmation = confirmation;
        this.startService = startService;
        EndService = endService;
        this.startServiceConfirm = startServiceConfirm;
        this.endServiceConfirm = endServiceConfirm;
        this.state = state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public void setStartService(String startService) {
        this.startService = startService;
    }
    public void setEndService(String endService) {
        EndService = endService;
    }
    public void setStartServiceConfirm(String startServiceConfirm) {this.startServiceConfirm = startServiceConfirm;}
    public void setEndServiceConfirm(String endServiceConfirm) {this.endServiceConfirm = endServiceConfirm;}
    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
    public void setFreelancerID(String freelancerID) {
        this.freelancerID = freelancerID;
    }
    public void setTime(String time) {this.time = time; }
    public void setNotes(String notes) {this.notes = notes;}

    public  String  getOrderID(){return orderID;}
    public String getState() {
        return state;
    }
    public String getStartService() {
        return startService;
    }
    public String getEndService() {
        return EndService;
    }
    public String getStartServiceConfirm() {
        return startServiceConfirm;
    }
    public String getEndServiceConfirm() {
        return endServiceConfirm;
    }
    public String getClientID() {
        return clientID;
    }
    public String getConfirmation() { return confirmation; }
    public String getFreelancerID() {
        return freelancerID;
    }
    public String getLocation() {return location;}
    public String getServiceID() {return serviceID;}
    public String getNotes() {return notes;}
    public String getTime() {return time;}
    public String getDate() { return date;}






}
