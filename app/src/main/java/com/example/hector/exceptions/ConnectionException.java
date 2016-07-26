package com.example.hector.exceptions;

/**
 * Created by hectorfrancisco on 25-07-2016.
 */
public class ConnectionException extends Exception {

    private String descError;

    public ConnectionException(){
    }

    public ConnectionException(String descError){
        this.descError = descError;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }
}
