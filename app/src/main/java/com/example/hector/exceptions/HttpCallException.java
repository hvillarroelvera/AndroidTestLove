package com.example.hector.exceptions;

/**
 * Created by hectorfrancisco on 25-07-2016.
 */
public class HttpCallException extends Exception {
    private String descError;

    public HttpCallException(){
    }

    public HttpCallException(String descError){
        this.descError = descError;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }
}
