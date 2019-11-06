package com.uqu.ladieshouse.ladieshouse;



public class FreelancerServices {
    private String serviceName;
    private String serviceDescription;
    private int ServicePrice;
    private String serviceCatagory;
    private String freelancerID;
    private String serviceID;



    public FreelancerServices() {
    }

    public FreelancerServices(String serviceName, String serviceDescription, int ServicePrice, String serviceCatagory, String freelancerID, String serviceID) {
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.ServicePrice = ServicePrice;
        this.serviceCatagory = serviceCatagory;
        this.freelancerID = freelancerID;
        this.serviceID = serviceID;
    }
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public void setServiceDescription(String serviceDescription) {this.serviceDescription = serviceDescription;}
    public void setServicePrice(int servicePrice) {
        ServicePrice = servicePrice;
    }
    public void setServiceCatagory(String serviceCatagory) {this.serviceCatagory = serviceCatagory;}
    public void setFreelancerID(String freelancerID) {
        this.freelancerID = freelancerID;
    }

    public String getServiceName() {
        return serviceName;
    }
    public String getServiceID() {
        return serviceID;
    }
    public String getServiceDescription() {
        return serviceDescription;
    }
    public int getServicePrice() {
        return ServicePrice;
    }
    public String getServiceCatagory() {
        return serviceCatagory;
    }
    public String getFreelancerID() {
        return freelancerID;
    }


}