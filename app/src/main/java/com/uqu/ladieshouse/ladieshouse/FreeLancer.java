package com.uqu.ladieshouse.ladieshouse;

/**
 * Created by dhuha on 09/04/18.
 */

class FreeLancer {

    String FreeLancerID,  FreeLancerName,  FreeLancerEmail,  FreeLancerPhone ;
    public FreeLancer(String ClientID, String clientName, String clientEmail, String clientPhone) {
        this.FreeLancerID=ClientID;
        this.FreeLancerName =clientName;
        this.FreeLancerEmail =clientEmail;
        this.FreeLancerPhone =clientPhone;
    }

    public String getFreeLancerName() {
        return FreeLancerName;
    }

    public String getFreeLancerEmail() {
        return FreeLancerEmail;
    }

    public String getFreeLancerPhone() {
        return FreeLancerPhone;
    }

    public String getFreeLancerID() { return FreeLancerID; }
}
