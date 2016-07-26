package com.example.hector.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hectorfrancisco on 07-07-2016.
 */
public class VerifyConnection {



    public static Boolean isConnected(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            if(activeNetwork != null){
                boolean isConnected = activeNetwork.isConnected();
                if(isConnected){
                    return true;
                }else{
                    return false;
                }
            }

            return false;
        }else{
            return false;
        }

    }



}
