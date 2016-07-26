package com.example.hector.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hector.testloveapp.Constantes;

import java.util.ArrayList;

/**
 * Created by hectorfrancisco on 10-07-2016.
 */
public class CrudSharedPreferences {


    public void registrarEnListaCacheByTipoProperty(Context context,String property,int indice,String... params)
    {

        SharedPreferences prefs = context.getSharedPreferences(Constantes.APP_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();

        if(property.equals(Constantes.PROPERTY_SOLICITUD_ENVIADA)) {
            editor.putString(property + indice,params[0]+"_"+params[1]);

        }else if(property.equals(Constantes.PROPERTY_PREGUNTA_CONTESTADA)) {
            editor.putString(property + indice,params[0]);

        }else if(property.equals(Constantes.PROPERTY_PUNTUACION_CONTESTADAS)) {
            editor.putString(property + indice,params[0]);

        }
            editor.commit();

    }

    public void eliminaAllByPropertyEnCache(Context context,ArrayList<?>lista,String property)
    {
        ArrayList<?>nuevaLista;
        ArrayList<?>listaEntrante;

        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for(int i=0;i<lista.size();i++){
            editor.remove(property+i);
        }

        editor.commit();

    }

    public ArrayList<String> getListFromCacheByProperty(Context context,String property)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        String finalWhile = "";
        int i = 0;

        do{
            finalWhile = prefs.getString(property + i, "");
            if(finalWhile != ""){
                listaGetsCache.add(finalWhile);
                i++;
            }

        }while(finalWhile != "");

        return listaGetsCache;
    }
}
