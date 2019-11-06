package com.uqu.ladieshouse.ladieshouse;

/**
 * Created by dhuha on 09/04/18.
 */

class Client {

    String ClientID,  clientName,  clientEmail,  clientPhone ;
    public Client(String ClientID, String clientName, String clientEmail, String clientPhone) {
        this.ClientID=ClientID;
        this.clientName =clientName;
        this.clientEmail =clientEmail;
        this.clientPhone =clientPhone;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }
}
