package com.example.hector.testloveapp;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.example.hector.connection.VerifyConnection;
import com.example.hector.exceptions.ConnectionException;
import com.example.hector.exceptions.HttpCallException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;

import DTO.ContactoDTO;
import DTO.GcmDTO;
import DTO.PreguntaDTO;
import DTO.PreguntaPendienteDTO;
import DTO.PreguntasDTO;
import DTO.PuntuacionDTO;
import DTO.PuntuacionPendienteDTO;
import DTO.RespuestaPendienteDTO;
import DTO.SessionDTO;
import DTO.SolicitudDTO;
import DTO.SolicitudEnviadaDTO;
import Util.JsonParseador;

/**
 * Created by hector on 20-06-2015.
 */
public class ServicioRest {

    boolean resul = true;
    public static final String TAG = "GCMTestLoveApp";
    private String ErrorDescripcion;
    private short codError;
    private Context context;

    public ServicioRest(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public short getCodError() {
        return codError;
    }

    public void setCodError(short codError) {
        this.codError = codError;
    }

    public String getErrorDescripcion() {
        return ErrorDescripcion;
    }

    public void setErrorDescripcion(String errorDescripcion) {
        ErrorDescripcion = errorDescripcion;
    }

    public String registrarUsuario(String nom_user,String password,String cod_gcm,int appVersion,long expirationTime)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);
            dato.put("password", password);
            dato.put("codGcm", cod_gcm);
            dato.put("expirationTime", expirationTime);
            dato.put("appVersion",appVersion );

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_USUARIO_REGISTRAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
        /*INI Se setea errorDescripcion*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }

    public String registrarContacto(String nom_user,String contacto,short respuesta)throws ConnectionException, HttpCallException {
        String resulStatus="";
        HttpResponse resp=null;

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);
            dato.put("contacto", contacto);
            dato.put("respuesta", respuesta);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_PUT, Constantes.HTTPCALL_RUTA_CONTACTO_REGISTRAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
           /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
        /*INI Se setea errorDescripcion*/

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }

    public String loguearUsuario(String nom_user,String password)throws ConnectionException, HttpCallException {
        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);
            dato.put("password", password);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_USUARIO_LOGUEAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
                /*INI Se setea errorDescripcion*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }

    public GcmDTO recuperarGcmXUsuario(String nom_user)throws ConnectionException, HttpCallException {
        HttpResponse resp=null;
        String resulStatus="";
        String respStr="";
        GcmDTO gcmdto=new GcmDTO();

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_GCM_OBTENER, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){
                respStr = EntityUtils.toString(resp.getEntity());
                JsonParseador jParser=new JsonParseador();
                Log.i(TAG, "Resp recuperarGcmXUsuario ="+respStr);
                gcmdto=jParser.getGcmFromJson(respStr);
            }else if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }




        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gcmdto;
    }


    public ContactoDTO recuperarContactoXUsuario(String nom_user)throws ConnectionException, HttpCallException {
        String respStr="";
        HttpResponse resp=null;
        String resulStatus="";
        ContactoDTO contactoDTO=new ContactoDTO();

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();



        try {

            dato.put("nom_user", nom_user);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_CONTACTO_OBTENER, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }


            if(resulStatus=="1"){

                respStr = EntityUtils.toString(resp.getEntity());
                /*Recupera datos del contacto desde Headers*/
                Header headerContacto = resp.getFirstHeader("Contacto");
                contactoDTO.setContacto(headerContacto.getValue());
                Header headerIdContacto = resp.getFirstHeader("IdContacto");
                contactoDTO.setId_contacto(Long.parseLong(headerIdContacto.getValue()));

            }else{
                getErrorMessage(resp);
                contactoDTO=null;
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contactoDTO;
    }


    public PreguntasDTO getCountPreguntasXUsuario(String nom_user)throws ConnectionException, HttpCallException {
        String respStr="";
        HttpResponse resp=null;
        String resulStatus="";
        PreguntasDTO pDTO=new PreguntasDTO();


        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PREGUNTA_OBTENER_CANTIDAD, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus=="1"){

                respStr = EntityUtils.toString(resp.getEntity());

                JsonParseador jParser=new JsonParseador();
                Log.i(TAG, "Resp getCountPreguntasXUsuario =" + respStr);


                pDTO.setCantidadPreguntas(Integer.parseInt(jParser.getUnDatoSimpleString(respStr, "cantidadPreguntas")));

            }else if(resulStatus=="0"){
                getErrorMessage(resp);
                pDTO.setCantidadPreguntas(0);

            }







        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pDTO;
    }

    public ArrayList<SolicitudDTO> getSolicitudContacto(String nom_user)throws ConnectionException, HttpCallException {
        String respStr="";
        HttpResponse resp=null;
        String resulStatus="";
        SolicitudDTO solicitudDTO = new SolicitudDTO();
        ArrayList<SolicitudDTO>solicitudDTOs = new ArrayList<SolicitudDTO>();
        String error = "";

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);
            dato.put("password", "");
            dato.put("codGcm", "");

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_CONTACTO_SOLICITUD_RECUPERAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus=="1"){

                respStr = EntityUtils.toString(resp.getEntity());

                JsonParseador jParser=new JsonParseador();
                Log.i(TAG, "Resp getCountPreguntasXUsuario =" + respStr);
                solicitudDTO.setUserReceptor(jParser.getUnDatoSimpleString(respStr, "userReceptor"));
                solicitudDTOs.add(solicitudDTO);

            }else if(resulStatus=="0"){
               /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
                /*INI Se setea errorDescripcion*/

                solicitudDTO = null;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





        return solicitudDTOs;
    }

    public PreguntasDTO getPreguntasXUsuario(String nom_user)throws ConnectionException, HttpCallException {
        String respStr="";
        HttpResponse resp=null;
        String resulStatus="";
        PreguntasDTO preguntasDTO=new PreguntasDTO();
        JSONArray arrayJson = null;


        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);
            dato.put("password", "");
            dato.put("codGcm", "");

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PREGUNTA_OBTENER_PREGUNTAS, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus=="1"){

                respStr = EntityUtils.toString(resp.getEntity());

                JsonParseador jParser=new JsonParseador();
                Log.i(TAG, "Resp getPreguntasXUsuario = " + respStr);


                preguntasDTO.setCantidadPreguntas(Integer.parseInt(jParser.getUnDatoSimpleString(respStr, "cantidadPreguntas")));
                arrayJson = jParser.getArrayFromString(respStr,"preguntas");
                preguntasDTO.setPreguntaDTOs(jParser.getPreguntasDTOFromArrayJson(arrayJson, preguntasDTO.getCantidadPreguntas()));

            }else if(resulStatus=="0"){
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
                preguntasDTO.setCantidadPreguntas(0);
                preguntasDTO.setPreguntaDTOs(null);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return preguntasDTO;
    }


    public String preguntar(String nom_user,String pregunta,long numeroPregunta)throws ConnectionException, HttpCallException {
        Log.d(TAG, "********preguntar********");
        HttpResponse resp=null;
        String resulStatus="";
        PreguntaDTO pDTO=new PreguntaDTO();
        PreguntasDTO psDTO=new PreguntasDTO();

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();
        JSONArray arrayPregunta1=new JSONArray();

        JSONObject pregunta1 = new JSONObject();
        try {
            pregunta1.put("pregunta",pregunta);
            pregunta1.put("numero",numeroPregunta);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayPregunta1.put(pregunta1);


        try {

            dato.put("nom_user", nom_user);
            dato.put("cantidadPreguntas", "0");
            dato.put("preguntas",arrayPregunta1);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PREGUNTA_PREGUNTAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")||
                    resulStatus.equals(Constantes.RESULTADO_STATUS_409)){
                getErrorMessage(resp);
            }
        /*INI Se setea errorDescripcion*/

        }  catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }

    public String registrarPregunta(Context context,String nom_user,String pregunta)throws ConnectionException, HttpCallException {
        Log.i(TAG, "INI registrarPregunta");
        HttpResponse resp=null;
        String resulStatus="";
        long numeroPregunta = 0;
        Util util = new Util();


        //Construimos el JSON
        JSONObject dato = new JSONObject();
        JSONArray arrayPregunta1=new JSONArray();



        JSONObject pregunta1 = new JSONObject();
        try {
            pregunta1.put("pregunta",pregunta);
            pregunta1.put("numero",0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayPregunta1.put(pregunta1);

        try {

            dato.put("nom_user", nom_user);

            dato.put("preguntas",arrayPregunta1);
            dato.put("cantidadPreguntas", 0);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_PUT, Constantes.HTTPCALL_RUTA_PREGUNTA_REGISTRAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }


             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("1")){
                 /*Almacenar pregunta en cache*/
                Header headers = resp.getFirstHeader("NumeroPregunta");
                numeroPregunta = Long.parseLong(headers.getValue());
                util.registrarPreguntaEnCache(context, pregunta, numeroPregunta);
            }else if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
            /*INI Se setea errorDescripcion*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }


    public String enviarSolicitudContacto(String nom_user,String contacto)throws ConnectionException, HttpCallException {

        HttpResponse resp=null;
        String resulStatus="";

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();



        StringEntity entity;
        try {

            dato.put("nom_user", nom_user);
            dato.put("contacto", contacto);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_CONTACTO_ENVIAR_SOLICITUD, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")||
            resulStatus.equals(Constantes.RESULTADO_STATUS_409)){
                getErrorMessage(resp);
            }
            /*INI Se setea errorDescripcion*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }


    public String enviarRespuestaPregunta(String nom_user,String pregunta,long numeroPregunta,String respuesta)throws ConnectionException, HttpCallException {
        Log.i(TAG, "INI enviarRespuestaPregunta");
        HttpResponse resp=null;
        String resulStatus="";

        //Construimos el JSON
        JSONObject dato = new JSONObject();
        JSONArray arrayPregunta1=new JSONArray();


        JSONObject pregunta1 = new JSONObject();
        try {
            pregunta1.put("pregunta",pregunta);
            pregunta1.put("numero",numeroPregunta);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayPregunta1.put(pregunta1);




        StringEntity entity;
        try {

            dato.put("nom_user", nom_user);
            dato.put("preguntas",arrayPregunta1);
            dato.put("cantidadPreguntas", 1);
            dato.put("respuesta", respuesta);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PREGUNTA_RESPONDER, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
            /*INI Se setea errorDescripcion*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }

    public String enviarPuntuacion(Context context,String nom_user,String resultado)throws ConnectionException, HttpCallException {
        Log.i(TAG, "INI enviarPuntuacion");
        HttpResponse resp=null;
        String resulStatus="";
        long puntuacion = 0;
        Util util = new Util();

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();
        try {

            dato.put("nom_user", nom_user);
            dato.put("resultado", resultado);
            dato.put("puntos", 0);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_GCM_PUNTUACION, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")||
            resulStatus.equals(Constantes.RESULTADO_STATUS_409)){
                 /*Recupera puntuacion de preguntasEnviadas*/
                puntuacion = util.getPuntuacionPreguntadasEnCache(context);
                Log.i(TAG, "Puntuacion recuperada de cache ["+puntuacion+"]");
            /*Almacenar puntuacion de preguntasEnviadas*/
                Header headers = resp.getFirstHeader("Puntos");
                puntuacion = Long.parseLong(headers.getValue());
                Log.i(TAG, "Puntuacion recuperada de cache + punt<je de ultima pregunta ["+puntuacion+"]");
                util.registrarPuntuacionPreguntadasEnCache(context,puntuacion);
            }else{
                /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
            /*INI Se setea errorDescripcion*/
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }

    public String actualizarGcm(String nom_user,String cod_gcm,int appVersion,long expirationTime)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {
            dato.put("nom_user", nom_user);
            dato.put("password", "");
            dato.put("codGcm", cod_gcm);
            dato.put("expirationTime", expirationTime);
            dato.put("appVersion",appVersion );


            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_GCM_ACTUALIZAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
        /*INI Se setea errorDescripcion*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }

    public String eliminarContacto(long idContacto)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {
            dato.put("id_contacto", idContacto);
            dato.put("contacto", "");
            dato.put("nomUser", "");



            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_CONTACTO_ELIMINAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
        /*INI Se setea errorDescripcion*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }

    public String actualizarContacto(ContactoDTO contactoDTO)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {
            dato.put("id_contacto", contactoDTO.getId_contacto());
            dato.put("contacto", contactoDTO.getContacto());
            dato.put("nomUser", contactoDTO.getNomUser());

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_CONTACTO_MODIFICAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }

    public String eliminarSolicitudContacto(String emisor,String receptor)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {
            dato.put("numero", 0);
            dato.put("userEmisor", emisor);
            dato.put("userReceptor", receptor);
            dato.put("estado", 0);
            dato.put("tipo", 0);



            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_CONTACTO_SOLICITUD_ELIMINAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }

    public String reenviarSolicitudContacto(String nom_user,String contacto)throws ConnectionException, HttpCallException {

        HttpResponse resp=null;
        String resulStatus="";

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();

        StringEntity entity;
        try {

            dato.put("nom_user", nom_user);
            dato.put("contacto", contacto);

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_CONTACTO_REENVIAR_SOLICITUD, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
                /*INI Se setea errorDescripcion*/
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resulStatus;
    }

    public String cerrarSesion(String nom_user)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);
            dato.put("password", "");
            dato.put("codGcm", "");

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_USUARIO_CERRAR_SESION, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
             /*INI Se setea errorDescripcion*/
            if(resulStatus.equals("0")){
                getErrorMessage(resp);
            }
        /*INI Se setea errorDescripcion*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }

    public SessionDTO getSesionXUsuario(String nom_user)throws ConnectionException, HttpCallException {
        String respStr="";
        HttpResponse resp=null;
        String resulStatus="";
        SessionDTO sessionDTO = new SessionDTO();

        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {

            dato.put("nom_user", nom_user);
            dato.put("password", "");
            dato.put("codGcm", "");

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_SESION_OBTENER_SESION_BY_USUARIO, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus=="1"){

                respStr = EntityUtils.toString(resp.getEntity());

                JsonParseador jParser=new JsonParseador();
                Log.i(TAG, "Resp getPreguntasXUsuario = " + respStr);

                sessionDTO.setId(Long.parseLong(jParser.getUnDatoSimpleString(respStr, "id")));
                sessionDTO.setEstado(Short.parseShort(jParser.getUnDatoSimpleString(respStr, "estado")));
                sessionDTO.setUsuario(jParser.getUnDatoSimpleString(respStr, "usuario"));
                sessionDTO.setUltimaFechaLogin(jParser.getUnDatoSimpleString(respStr, "ultimaFechaLogin"));

            }else if(resulStatus=="0"){
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
                sessionDTO = null;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionDTO;
    }


    public String eliminarPregunta(long numeroPregunta)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {
            dato.put("pregunta", "");
            dato.put("numero", numeroPregunta);



            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PREGUNTA_ELIMINAR, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }


    public String eliminarAllPreguntaPendienteByContacto(PreguntaPendienteDTO preguntaPendienteDTO)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();


        try {
            dato.put("id", 0);
            dato.put("numero", 0);
            dato.put("contacto", preguntaPendienteDTO.getContacto());
            dato.put("estado", 0);



            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PREGUNTAPENDIENTE_ELIMINAR_ALL_BY_CONTACTO, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }


    public String eliminarAllRespuestaPendienteByContacto(RespuestaPendienteDTO respuestaPendienteDTO)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();

        try {
            dato.put("id", 0);
            dato.put("estado", 0);
            dato.put("respuesta", "");
            dato.put("numero", 0);
            dato.put("contacto", respuestaPendienteDTO.getContacto());

            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_RESPUESTAPENDIENTE_ELIMINAR_ALL_BY_CONTACTO, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }


    public String eliminarAllPuntuacionPendienteByContacto(PuntuacionPendienteDTO puntuacionPendienteDTO)throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();

        try {
            dato.put("id", 0);
            dato.put("contacto", puntuacionPendienteDTO.getContacto());
            dato.put("puntos", "");
            dato.put("estado", 0);


            if(VerifyConnection.isConnected(getContext())){
            resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PUNTUACIONPENDIENTE_ELIMINAR_ALL_BY_CONTACTO, dato);
            resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }
            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){
                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }


    public String eliminarPuntuacionByIdContacto(PuntuacionDTO puntuacionDTO) throws ConnectionException, HttpCallException {

        String resulStatus="";
        HttpResponse resp=null;
        //Construimos el objeto cliente en formato JSON
        JSONObject dato = new JSONObject();

        try {
            dato.put("idContacto", puntuacionDTO.getIdContacto());
            dato.put("puntos", 0);

            if(VerifyConnection.isConnected(getContext())){
                resp = httpCall(Constantes.HTTPCALL_TIPO_POST, Constantes.HTTPCALL_RUTA_PUNTUACION_ELIMINAR_BY_IDCONTACTO, dato);
                resulStatus= validarHttpStatus(resp.getStatusLine().getStatusCode());
            }else{
                resulStatus="0";
                setCodError(Constantes.ERROR_GENERAL_CONEXION);
            }


            if(resulStatus.equals("1")){

            }else{
                 /*INI Se setea errorDescripcion*/
                if(resulStatus.equals("0")){

                    getErrorMessage(resp);
                }
        /*INI Se setea errorDescripcion*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resulStatus;
    }


    private HttpResponse httpCall(short tipo,String ruta,JSONObject jsonObject) {

        HttpClient httpClient = new DefaultHttpClient();
        StringEntity entity=null;
        HttpResponse resp =null;
        int httpStatus=0;
        String ruta_principal=Constantes.HTTPCALL_RUTA_PRINCIPAL;

        try {
            entity = new StringEntity(jsonObject.toString(),"utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if (tipo == Constantes.HTTPCALL_TIPO_PUT) {

            HttpPut put =
                    new HttpPut(ruta_principal+ruta);

            put.setHeader("content-type", "application/json; charset=utf-8");
            put.setEntity(entity);
            try {
                resp = httpClient.execute(put);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if(tipo == Constantes.HTTPCALL_TIPO_POST){

            HttpPost post =
                    new HttpPost(ruta_principal+ruta);

            post.setHeader("content-type", "application/json; charset=utf-8");
            post.setEntity(entity);
            try {
                resp = httpClient.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }


    private String validarHttpStatus(int httpStatus){

        String resulStatus="";
        Log.i(TAG, "HTTP STATUS " + httpStatus);

        /*Se resetea erreoDescripcion*/
        setErrorDescripcion("");
        if(httpStatus==200){

            resulStatus =  Constantes.RESULTADO_STATUS_200;

        }else if(httpStatus==204){

            resulStatus="0";
            setCodError(Constantes.ERROR_GENERAL_REST_SERVICE);
        }else if(httpStatus==304){

                resulStatus="0";
            setCodError(Constantes.ERROR_GENERAL_REST_SERVICE);
        }
        else if(httpStatus==412){
            resulStatus="0";
            setCodError(Constantes.ERROR_GENERAL_REST_SERVICE);
        }else if(httpStatus==500){
            resulStatus="0";
            setCodError(Constantes.ERROR_GENERAL_REST_SERVICE);
            Log.i(TAG,"Error en el servidor");
        }else if(httpStatus==409){
            resulStatus=Constantes.RESULTADO_STATUS_409;
            setCodError(Constantes.ERROR_GENERAL_REST_SERVICE);
            Log.i(TAG,"El dispositivo emisor es el mismo del receptor,se enviara" +
                    "solicitud cuando el contacto inicie sesion");
        } else{
            resulStatus="0";
            setCodError(Constantes.ERROR_GENERAL_REST_SERVICE);
        }

        return resulStatus;
    }


    public void getErrorMessage(HttpResponse resp)throws HttpCallException,ConnectionException{

        if(getCodError()==Constantes.ERROR_GENERAL_REST_SERVICE){
            Header header = resp.getFirstHeader("Error");
            if(header != null){
                //setErrorDescripcion(header.getValue());
                throw new HttpCallException(header.getValue());
            }
        }else if(getCodError()==Constantes.ERROR_GENERAL_CONEXION){
            //setErrorDescripcion(Constantes.ERROR_NO_EXISTE_CONEXION);
            throw new ConnectionException(Constantes.ERROR_NO_EXISTE_CONEXION);
        }


    }

}
