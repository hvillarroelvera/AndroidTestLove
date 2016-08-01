package com.example.hector.testloveapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.util.Log;

import com.example.hector.DAO.PreguntaContestadaDAO;
import com.example.hector.DAO.PreguntaEnviadaDAO;
import com.example.hector.DAO.PreguntaRecibidaDAO;
import com.example.hector.DAO.PuntuacionRecibidaDAO;
import com.example.hector.DAO.SolicitudContestadaDAO;
import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.DAO.SolicitudRecibidaDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import DTO.PreguntaDTO;
import DTO.SolicitudDTO;
import DTO.SolicitudEnviadaDTO;

/**
 * Created by hector on 20-06-2015.
 */
public class Util {

    public static final String TAG = "GCMTestLoveApp";

    public void removerCache(Context context,String nom_user){
        SolicitudEnviadaDAO solicitudEnviadaDAO = new SolicitudEnviadaDAO(context);
        SolicitudRecibidaDAO solicitudRecibidaDAO = new SolicitudRecibidaDAO(context);
        SolicitudContestadaDAO solicitudContestadaDAO = new SolicitudContestadaDAO(context);
        PreguntaEnviadaDAO preguntaEnviadaDAO =new PreguntaEnviadaDAO(context);
        PreguntaRecibidaDAO preguntaRecibidaDAO = new PreguntaRecibidaDAO(context);
        PreguntaContestadaDAO preguntaContestadaDAO = new PreguntaContestadaDAO(context);
        PuntuacionRecibidaDAO puntuacionRecibidaDAO = new PuntuacionRecibidaDAO(context);

        int cantidadPreguntas = 0;
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_REG_ID);
        editor.remove(Constantes.PROPERTY_APP_VERSION);
        editor.remove(Constantes.PROPERTY_EXPIRATION_TIME);
        editor.remove(Constantes.PROPERTY_CONTACTO);
        editor.remove(Constantes.PROPERTY_ID_CONTACTO);
        editor.remove(Constantes.PROPERTY_SOLICITANTE);
        editor.remove(Constantes.PROPERTY_USER);

        /*Se verifica que existan preguntas,luego se eliminan*/
        cantidadPreguntas = getCantidadPreguntaEnCache(context);
        for(int i=0;i<cantidadPreguntas;i++){
            int numeroPregunta = i;
            numeroPregunta++;
            editor.remove(Constantes.PROPERTY_PREGUNTA+numeroPregunta);
        }
        editor.remove(Constantes.PROPERTY_CANTIDAD_PREGUNTA);
        editor.remove(Constantes.PROPERTY_PREGUNTA_RECIBIDA);
        editor.remove(Constantes.PROPERTY_PREGUNTA_CONTESTADA);
        editor.remove(Constantes.PROPERTY_PUNTUACION_PREGUNTADAS);
        editor.remove(Constantes.PROPERTY_PUNTUACION_CONTESTADAS);
        editor.remove(Constantes.PROPERTY_RESULTADO);
        editor.remove(Constantes.PROPERTY_SOLICITUD_ENVIADA);

        solicitudEnviadaDAO.deleteAll();
        solicitudRecibidaDAO.deleteAll();
        solicitudContestadaDAO.deleteAll();
        preguntaEnviadaDAO.deleteAll();
        preguntaRecibidaDAO.deleteAll();
        preguntaContestadaDAO.deleteAll();
        puntuacionRecibidaDAO.deleteAll();

        editor.commit();
    }

    public void removerCacheOnlyContacto(Context context){
        int cantidadPreguntas = 0;
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_CONTACTO);
        editor.remove(Constantes.PROPERTY_ID_CONTACTO);
        editor.remove(Constantes.PROPERTY_SOLICITANTE);

        /*Se verifica que existan preguntas,luego se eliminan*/
       /* cantidadPreguntas = getCantidadPreguntaEnCache(context);
        for(int i=0;i<cantidadPreguntas;i++){
            int numeroPregunta = i;
            numeroPregunta++;
            editor.remove(Constantes.PROPERTY_PREGUNTA+numeroPregunta);
        }
        editor.remove(Constantes.PROPERTY_CANTIDAD_PREGUNTA);*/
        editor.remove(Constantes.PROPERTY_PREGUNTA_RECIBIDA);
        editor.remove(Constantes.PROPERTY_PREGUNTA_CONTESTADA);
        editor.remove(Constantes.PROPERTY_PUNTUACION_PREGUNTADAS);
        editor.remove(Constantes.PROPERTY_PUNTUACION_CONTESTADAS);
        editor.remove(Constantes.PROPERTY_RESULTADO);
        editor.remove(Constantes.PROPERTY_SOLICITUD_ENVIADA);

        editor.commit();
    }

    public int compararGcmCache(Context context,Class clase,String nom_user)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);


        String registeredUser = prefs.getString(Constantes.PROPERTY_USER, "userDefaultTestLove");
        if (!nom_user.equals(registeredUser))
        {
            Log.d(TAG, "Distinto usuario.");
            //obtener datos de de servidor
            return Constantes.CACHE_NOT_FOUND_USER;
        }


        String registrationId = prefs.getString(Constantes.PROPERTY_REG_ID, "");

        if (registrationId.equals(""))
        {
            Log.d(TAG, "No tiene regId");
            //obtener datos de de servidor
            return Constantes.CACHE_NOT_FOUND_REG_ID;
        }

        long expirationTime = prefs.getLong(Constantes.PROPERTY_EXPIRATION_TIME, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));
        if (System.currentTimeMillis() > expirationTime)
        {
            Log.d(TAG, "Registro GCM expirado.");
            return Constantes.CACHE_NOT_EXPIRATION_TIME;
        }


        int registeredVersion = prefs.getInt(Constantes.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion)
        {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return Constantes.CACHE_NOT_APP_VERSION;
        }



        Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");


        return Constantes.SUCCESS;
    }

    public static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    public void registrarDatosCacheFromServidor(Context context,String user, String regId,int appVersion,long expirationTime)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        //int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.PROPERTY_USER, user);
        editor.putString(Constantes.PROPERTY_REG_ID, regId);
        editor.putInt(Constantes.PROPERTY_APP_VERSION, appVersion);
        editor.putLong(Constantes.PROPERTY_EXPIRATION_TIME,expirationTime);

        editor.commit();
    }

    public void actualizarDatosCacheFromServidor(Context context,String user, String regId,long expirationTime)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.PROPERTY_USER, user);
        editor.putString(Constantes.PROPERTY_REG_ID, regId);
        editor.putInt(Constantes.PROPERTY_APP_VERSION, appVersion);
        editor.putLong(Constantes.PROPERTY_EXPIRATION_TIME,
                expirationTime);

        editor.commit();
    }

    public String getUserCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);


        String registeredUser = prefs.getString(Constantes.PROPERTY_USER, "userDefaultTestLove");
       return registeredUser;
    }

    public String getContactoCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);


        String registeredContacto = prefs.getString(Constantes.PROPERTY_CONTACTO, "contactoDefaultTestLove");
        return registeredContacto;
    }

    public String getIdContactoCache(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);


        String idContacto = prefs.getString(Constantes.PROPERTY_ID_CONTACTO, "idContactoDefaultTestLove");
        return idContacto;
    }

    public void registrarContactoEnCache(Context context,String contacto,String idContacto)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.PROPERTY_CONTACTO, contacto);
        editor.putString(Constantes.PROPERTY_ID_CONTACTO, idContacto);


        editor.commit();
    }

    public void eliminarContactoEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_CONTACTO);
        editor.remove(Constantes.PROPERTY_ID_CONTACTO);

        editor.commit();
    }

    public String getSolicitudContactoPendienteCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);


        String registeredContactoPendiente = prefs.getString(Constantes.PROPERTY_SOLICITUD_ENVIADA, "contactoPendienteDefaultTestLove");
        return registeredContactoPendiente;
    }

    public void registrarSolicitudContactoPendienteCache(Context context,String contacto)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.PROPERTY_SOLICITUD_ENVIADA, contacto);

        editor.commit();
    }

    public void eliminarSolicitudContactoPendienteCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_SOLICITUD_ENVIADA);

        editor.commit();
    }

    public void registrarPreguntaEnCache(Context context,String pregunta,long numero)
    {
        Log.i(TAG, "INI registrarPreguntaEnCache");
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);
        int cantidadPreguntas=getCantidadPreguntaEnCache(context);
        Log.i(TAG, "Cantidad de preguntas " + cantidadPreguntas);

        if(cantidadPreguntas<0 || cantidadPreguntas==0){
            SharedPreferences.Editor editor = prefs.edit();

            /*Nueva implementacion*/
            /*Se agrega a la pregunta el numero seguido de un guion bajo y luego la pregunta*/
            editor.putString(Constantes.PROPERTY_PREGUNTA + "1",numero+"_"+pregunta );
            /*Se agrega a la pregunta el numero seguido de un guion bajo y luego la pregunta*/

            registrarCantidadPreguntaEnCache(context,1);

            Log.i(TAG, "Se ha guardado pregunta en" +Constantes.PROPERTY_PREGUNTA + "1");
            editor.commit();
        }else if(cantidadPreguntas>0){

            String numArray=Integer.toString(cantidadPreguntas+1);
                SharedPreferences.Editor editor = prefs.edit();
              /*Nueva implementacion*/
            /*Se agrega a la pregunta el numero seguido de un guion bajo y luego la pregunta*/
            editor.putString(Constantes.PROPERTY_PREGUNTA + numArray,numero+"_"+pregunta );
            /*Se agrega a la pregunta el numero seguido de un guion bajo y luego la pregunta*/


            actualizarCantidadPreguntaEnCache(context);
            Log.i(TAG, "Se ha guardado pregunta en "+Constantes.PROPERTY_PREGUNTA + numArray);
            editor.commit();

        }

    }

    public String getPreguntaCacheByNumArray(Context context,int numArray)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        String numArrayString=Integer.toString(numArray);
        String registeredPreguntaNumero = prefs.getString(Constantes.PROPERTY_PREGUNTA + numArrayString, "preguntaDefaultTestLove");
        int indexGuionBajo = 0;
        indexGuionBajo = registeredPreguntaNumero.indexOf("_");
        String registeredPregunta = registeredPreguntaNumero.substring(indexGuionBajo+1);
        return registeredPregunta;
    }

    public String getNumeroPreguntaCacheByNumArray(Context context,int numArray)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        String numArrayString=Integer.toString(numArray);
        String registeredPreguntaNumero = prefs.getString(Constantes.PROPERTY_PREGUNTA + numArrayString, "preguntaDefaultTestLove");
        int indexGuionBajo = 0;
        indexGuionBajo = registeredPreguntaNumero.indexOf("_");
        String registeredNumeroPregunta = registeredPreguntaNumero.substring(0,indexGuionBajo);
        return registeredNumeroPregunta;
    }

    public ArrayList<PreguntaDTO> eliminarPreguntaEnCache(Context context,ArrayList<PreguntaDTO>listaPreguntas,int index)
    {
        ArrayList<PreguntaDTO>nuevaListaPreguntas;
        PreguntaDTO preguntaDTO = null;
        int cantidadPreguntas = 0;

        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        /*INI Se eliminan todas las preguntas*/
        listaPreguntas.remove(index);
        nuevaListaPreguntas = listaPreguntas;
         /*Se verifica que existan preguntas,luego se eliminan*/
        cantidadPreguntas = getCantidadPreguntaEnCache(context);
        for(int i=0;i<cantidadPreguntas;i++){
            int numeroPregunta = i;
            numeroPregunta++;
            editor.remove(Constantes.PROPERTY_PREGUNTA+numeroPregunta);
            restaCantidadPreguntaEnCache(context);
        }

         /*FIN Se eliminan todas las preguntas*/

        /*INI Se recrean todas las preguntas con listado actualizado*/
        for(int j=0;j<nuevaListaPreguntas.size();j++){
            preguntaDTO = new PreguntaDTO();
            preguntaDTO = nuevaListaPreguntas.get(j);
            registrarPreguntaEnCache(context,preguntaDTO.getPregunta(),preguntaDTO.getNumero());
        }

        /*FIN Se recrean todas las preguntas con listado actualizado*/
        editor.commit();

        return listaPreguntas;
    }

    public void registrarCantidadPreguntaEnCache(Context context,int cantidadPreguntas)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constantes.PROPERTY_CANTIDAD_PREGUNTA, cantidadPreguntas);


        editor.commit();
    }

    public int getCantidadPreguntaEnCache(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        int cantidadPreguntas = prefs.getInt(Constantes.PROPERTY_CANTIDAD_PREGUNTA, -1);
        return cantidadPreguntas;
    }


    public void actualizarCantidadPreguntaEnCache(Context context)
    {
        Log.i(TAG,"INI actualizarCantidadPreguntaEnCache");
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        int cantidadPreguntas=getCantidadPreguntaEnCache(context);
        int nuevaCantidad=cantidadPreguntas+1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constantes.PROPERTY_CANTIDAD_PREGUNTA, nuevaCantidad);
        Log.i(TAG, "Cantidad nueva de preguntas "+nuevaCantidad);

        editor.commit();
    }

    public void restaCantidadPreguntaEnCache(Context context)
    {
        Log.i(TAG, "INI actualizarCantidadPreguntaEnCache");
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        int cantidadPreguntas=getCantidadPreguntaEnCache(context);
        int nuevaCantidad=cantidadPreguntas-1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constantes.PROPERTY_CANTIDAD_PREGUNTA, nuevaCantidad);
        Log.i(TAG, "Cantidad nueva de preguntas " + nuevaCantidad);

        editor.commit();
    }

    public void eliminarCantidadPreguntaEnCache(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_CANTIDAD_PREGUNTA);


        editor.commit();
    }

/*
    private static boolean checkPlayServices(Class clase) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(clase);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, clase,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i(TAG, "Dispositivo no soportado.");
                finish();
            }
            return false;
        }
        return true;
    }

*/

    public void registrarUsuarioSolicitanteEnCache(Context context,String solicitante)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.PROPERTY_SOLICITANTE, solicitante);


        editor.commit();
    }

    public String getSolicitanteCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        Log.i(TAG, context.getPackageResourcePath());
        String registeredContacto = prefs.getString(Constantes.PROPERTY_SOLICITANTE, "contactoSolicitanteDefaultTestLove");
        return registeredContacto;
    }

    public void eliminarUsuarioSolicitanteEnCache(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_SOLICITANTE);


        editor.commit();
    }

    public void registrarNumeroAndPreguntaRecibidaEnCache(Context context, String pregunta, long numeroPregunta)
    {
        Log.i(TAG,"INI registrarNumeroAndPreguntaRecibidaEnCache");
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        /*Se agrega a la pregunta el numeroPregunta seguido de un guion bajo y luego la pregunta*/
        editor.putString(Constantes.PROPERTY_PREGUNTA_RECIBIDA, numeroPregunta + "_" + pregunta);
        /*Se agrega a la pregunta el numeroPregunta seguido de un guion bajo y luego la pregunta*/

        editor.commit();
    }

    public PreguntaDTO getNumeroAndPreguntaRecibidaEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        PreguntaDTO preguntaDTO = new PreguntaDTO();
        String temp = "";
        Log.i(TAG, context.getPackageResourcePath());
        temp = prefs.getString(Constantes.PROPERTY_PREGUNTA_RECIBIDA, "preguntaRecibidaDefaultTestLove");
        preguntaDTO = getPreguntaDTOFromCache(temp);
        return preguntaDTO;
    }

    public void eliminarNumeroAndPreguntaRecibidaEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_PREGUNTA_RECIBIDA);


        editor.commit();
    }


    public void registrarPreguntaContestadaEnCache(Context context,String pregunta)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.PROPERTY_PREGUNTA_RECIBIDA, pregunta);


        editor.commit();
    }

    public String getPreguntaContestadaEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        Log.i(TAG, context.getPackageResourcePath());
        String preguntaRecibida = prefs.getString(Constantes.PROPERTY_PREGUNTA_RECIBIDA, "preguntaRecibidaDefaultTestLove");
        return preguntaRecibida;
    }

    public void eliminarPreguntaContestadaEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_PREGUNTA_RECIBIDA);


        editor.commit();
    }

    public void registrarPuntuacionPreguntadasEnCache(Context context,long puntuacion)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Constantes.PROPERTY_PUNTUACION_PREGUNTADAS, puntuacion);


        editor.commit();
    }

    public long getPuntuacionPreguntadasEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        Log.i(TAG, context.getPackageResourcePath());
        long puntuacion = prefs.getLong(Constantes.PROPERTY_PUNTUACION_PREGUNTADAS, 0);
        return puntuacion;
    }

    public void eliminarPuntuacionPreguntadasEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_PUNTUACION_PREGUNTADAS);


        editor.commit();
    }

    public void registrarPuntuacionContestadasEnCache(Context context,long puntuacion)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Constantes.PROPERTY_PUNTUACION_CONTESTADAS, puntuacion);


        editor.commit();
    }

    public long getPuntuacionContestadasEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        Log.i(TAG, context.getPackageResourcePath());
        long puntuacion = prefs.getLong(Constantes.PROPERTY_PUNTUACION_CONTESTADAS, 0);
        return puntuacion;
    }

    public void eliminarPuntuacionContestadasEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_PUNTUACION_CONTESTADAS);


        editor.commit();
    }

    public void registrarResultadoEnCache(Context context,String resultado)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constantes.PROPERTY_RESULTADO, resultado);


        editor.commit();
    }

    public String getResultadoEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        Log.i(TAG, context.getPackageResourcePath());
        String resultado = prefs.getString(Constantes.PROPERTY_RESULTADO, "puntuacionDefaultTestLove");
        return resultado;
    }

    public void eliminarResultadoEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_RESULTADO);


        editor.commit();
    }

    public void registrarSessionEnCache(Context context,boolean session)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constantes.PROPERTY_SESSION, session);


        editor.commit();
    }

    public boolean getSessionEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        Log.i(TAG, context.getPackageResourcePath());
        boolean session = prefs.getBoolean(Constantes.PROPERTY_SESSION, false);
        return session;
    }

    public void eliminarSessionEnCache(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(
                Constantes.APP_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constantes.PROPERTY_SESSION);


        editor.commit();
    }

    public PreguntaDTO getPreguntaDTOFromMsgGcm(String msgGcm){
        PreguntaDTO preguntaDTO = new PreguntaDTO();
        String numeroPregunta = "";
        String pregunta = "";
        int index = 0;
        index = msgGcm.indexOf(":");
        numeroPregunta = msgGcm.substring(0, index);
        pregunta = msgGcm.substring(index+1,msgGcm.length());
        preguntaDTO.setNumero(Long.parseLong(numeroPregunta));
        preguntaDTO.setPregunta(pregunta);
        return preguntaDTO;
    }

    public SolicitudDTO getSolicitudDTOFromMsgGcm(String msgGcm){
        SolicitudDTO solicitudDTO = new SolicitudDTO();
        String numeroSolicitud = "";
        String emisor = "";
        int index = 0;
        index = msgGcm.indexOf(":");
        numeroSolicitud = msgGcm.substring(0, index);
        emisor = msgGcm.substring(index+1,msgGcm.length());
        solicitudDTO.setNumero(Long.parseLong(numeroSolicitud));
        solicitudDTO.setUserEmisor(emisor);
        return solicitudDTO;
    }
	
	public SolicitudDTO getSolicitudDTOReceptorFromMsgGcm(String msgGcm){
        SolicitudDTO solicitudDTO = new SolicitudDTO();
        String numeroSolicitud = "";
        String emisor = "";
        int index = 0;
        index = msgGcm.indexOf(":");
        numeroSolicitud = msgGcm.substring(0, index);
        receptor = msgGcm.substring(index+1,msgGcm.length());
        solicitudDTO.setNumero(Long.parseLong(numeroSolicitud));
        solicitudDTO.setUserReceptor(receptor);
        return solicitudDTO;
    }

    public PreguntaDTO getPreguntaDTOFromCache(String temp){
        PreguntaDTO preguntaDTO = new PreguntaDTO();
        String numeroPregunta = "";
        String pregunta = "";
        int index = 0;
        index = temp.indexOf("_");
        numeroPregunta = temp.substring(0, index);
        pregunta = temp.substring(index+1,temp.length());
        preguntaDTO.setNumero(Long.parseLong(numeroPregunta));
        preguntaDTO.setPregunta(pregunta);
        return preguntaDTO;
    }

    public static ArrayList<SolicitudEnviadaDTO> transformToListSolEnviada(ArrayList<SolicitudDTO>listaSolDTO){
        ArrayList<SolicitudEnviadaDTO> solicitudEnviadaDTOs = new ArrayList<SolicitudEnviadaDTO>();
        SolicitudEnviadaDTO solicitudEnviadaDTO = null;
        SolicitudDTO solicitudDTO = null;
        for (int i = 0;i<listaSolDTO.size();i++){
            solicitudDTO = new SolicitudDTO();
            solicitudEnviadaDTO = new SolicitudEnviadaDTO();
            solicitudDTO = listaSolDTO.get(i);
            solicitudEnviadaDTO.setNombreContacto(solicitudDTO.getUserReceptor());
            solicitudEnviadaDTO.setNumero((int) solicitudDTO.getNumero());
            solicitudEnviadaDTOs.add(solicitudEnviadaDTO);
        }
        return solicitudEnviadaDTOs;
    }

    public  void setArrayListToOtherActivity(Intent intent,ArrayList<? extends Parcelable>lista,String propertyLista){

            intent.putParcelableArrayListExtra(propertyLista,lista);


    }

    public  ArrayList<? extends Parcelable> getArrayListFromOtherActivity(Intent intent,String propertyLista){
        ArrayList<? extends Parcelable> lista =intent.getParcelableArrayListExtra(propertyLista);
        return lista;
    }
}
