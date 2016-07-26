package com.example.hector.testloveapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hector.DAO.PreguntaContestadaDAO;
import com.example.hector.DAO.PreguntaEnviadaDAO;
import com.example.hector.DAO.PreguntaRecibidaDAO;
import com.example.hector.DAO.PuntuacionRecibidaDAO;
import com.example.hector.DAO.SolicitudContestadaDAO;
import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.DAO.SolicitudRecibidaDAO;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;

import DTO.ContactoDTO;
import DTO.GcmDTO;
import DTO.PreguntaDTO;
import DTO.PreguntasDTO;
import DTO.SessionDTO;
import DTO.SolicitudDTO;
import DTO.SolicitudEnviadaDTO;


public class Inicio extends AppCompatActivity {

    private EditText nombreUsuario;
    private EditText claveUsuario;
    private Button botonLoguear;
    private Button botonRegistrar;
    private ProgressDialog pdialog;
    public static final String TAG = "GCMTestLoveApp";
    private Context context;
    Util util=new Util();
    public static GoogleCloudMessaging gcm;
    private String regid="";
    ProgressDialog dialogoProgreso;
    private Intent intent;
    private SolicitudEnviadaDAO solicitudEnviadaDAO;
    private SolicitudRecibidaDAO solicitudRecibidaDAO;
    private SolicitudContestadaDAO solicitudContestadaDAO;
    private PreguntaEnviadaDAO preguntaEnviadaDAO;
    private PreguntaRecibidaDAO preguntaRecibidaDAO;
    private PreguntaContestadaDAO preguntaContestadaDAO;
    private PuntuacionRecibidaDAO puntuacionRecibidaDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existOpenSesion();
        setContentView(R.layout.activity_inicio);
        nombreUsuario=(EditText) findViewById(R.id.nombreUsuario);
        claveUsuario=(EditText) findViewById(R.id.claveUsuario);
        botonLoguear=(Button) findViewById(R.id.botonLoguear);
        botonRegistrar=(Button) findViewById(R.id.botonRegistrar);

        botonLoguear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context = getApplicationContext();

                TareaLogin tarea=new TareaLogin();
                dialogoProgreso = new ProgressDialog(Inicio.this);
                dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_LOGIN);
                dialogoProgreso.setCancelable(true);
                dialogoProgreso.setMax(100);
                tarea.execute(nombreUsuario.getText().toString(),claveUsuario.getText().toString());

            }
        });


        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(Inicio.this, Registrar.class);
                startActivity(intent);
            }
        });
    }


    private class TareaLogin extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resultadoLogin="";
        String resulActualizarGcm="";
        GcmDTO gcmdto=new GcmDTO();
        ContactoDTO cDTO=new ContactoDTO();
        PreguntasDTO pDTO=new PreguntasDTO();
        ArrayList<SolicitudDTO>listaSolicitudes = new ArrayList<SolicitudDTO>();
        SolicitudDTO solicitudDTO = new SolicitudDTO();
        int resultCompCache=0;
        long newExpirationTime = 0;

        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            Log.i(TAG, "INI doInBackground");

            try {
                publishProgress(10);
                Log.i(TAG, "PASO 1");
                resultCompCache = util.compararGcmCache(context, Inicio.class, params[0]);
                Log.i(TAG, "PASO 2");


                if(resultCompCache == Constantes.CACHE_NOT_FOUND_USER || resultCompCache == Constantes.CACHE_NOT_FOUND_REG_ID){
                    //Si no es el mismo usuario se recuperan datos desde el servidor y se validan
                    //Si no es el mismo regId se recuperan datos desde el servidor y se validan
                    publishProgress(60);
                    resultadoLogin = servicio.loguearUsuario(params[0], params[1]);
                    if(resultadoLogin.equals("1")) {
                        publishProgress(70);
                        gcmdto = servicio.recuperarGcmXUsuario(params[0]);
                        cDTO = servicio.recuperarContactoXUsuario(params[0]);
                        pDTO = servicio.getPreguntasXUsuario(params[0]);
                        listaSolicitudes = servicio.getSolicitudContacto(params[0]);
                         /*Falta implementar servicio para obetener puntuacion*/

                        /*Se limpia cache*/
                        util.removerCache(context, params[0]);

                        util.registrarDatosCacheFromServidor(context, params[0], gcmdto.getGcm_codGcm(), gcmdto.getAppVersion(), gcmdto.getExpirationTime());


                        if(cDTO != null){
                            util.registrarContactoEnCache(context, cDTO.getContacto(), Long.toString(cDTO.getId_contacto()));

                            /*registro en cache de preguntas si es que usuario tiene preguntas*/
                            if(pDTO.getCantidadPreguntas()>0){
                                for(int i = 0;i<pDTO.getCantidadPreguntas();i++){
                                    PreguntaDTO preguntaDTO = new PreguntaDTO();
                                    preguntaDTO = pDTO.getPreguntaDTOs().get(i);
                                    util.registrarPreguntaEnCache(context,preguntaDTO.getPregunta(),preguntaDTO.getNumero());
                                }
                            }
                        }else if(listaSolicitudes.size()>0){
                            solicitudEnviadaDAO = new SolicitudEnviadaDAO(listaSolicitudes,context);
                            /*util.registrarSolicitudContactoPendienteCache(context,solicitudDTO.getUserReceptor());*/
                        }

                        resultCompCache = util.compararGcmCache(context, Inicio.class, params[0]);

                        if (resultCompCache == Constantes.CACHE_NOT_EXPIRATION_TIME || resultCompCache == Constantes.CACHE_NOT_APP_VERSION) {
                            //Si expiro tiempo se registra nuevamente en google
                            //si version es distinta se registra en google
                            if (gcm == null) {
                                gcm = GoogleCloudMessaging.getInstance(context);
                            }

                            //Nos registramos en los servidores de GCM
                            regid = gcm.register(Constantes.SENDER_ID);


                            /*actualizar informacion en servidor*/
                            newExpirationTime = System.currentTimeMillis() + Constantes.EXPIRATION_TIME_MS;
                            resulActualizarGcm = servicio.actualizarGcm(params[0],regid,util.getAppVersion(context),newExpirationTime);
                                if(resulActualizarGcm.equals("1")){
                                    util.actualizarDatosCacheFromServidor(context, params[0], regid,newExpirationTime);
                                }
                        }
                    }
                }else if(resultCompCache == Constantes.CACHE_NOT_EXPIRATION_TIME || resultCompCache == Constantes.CACHE_NOT_APP_VERSION){
                    //Si expiro tiempo se registra nuevamente en google
                    //si version es distinta se registra en google
                    publishProgress(70);
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    //Nos registramos en los servidores de GCM
                    regid = gcm.register(Constantes.SENDER_ID);
                    /*actualizar informacion en servidor*/
                    newExpirationTime = System.currentTimeMillis() + Constantes.EXPIRATION_TIME_MS;
                    resulActualizarGcm = servicio.actualizarGcm(params[0],regid,util.getAppVersion(context),newExpirationTime);
                    if(resulActualizarGcm.equals("1")){
                        util.actualizarDatosCacheFromServidor(context, params[0], regid,newExpirationTime);
                    }



                }else{
                    publishProgress(70);
                    resultadoLogin = servicio.loguearUsuario(params[0], params[1]);
                    if(resultadoLogin.equals("1")) {

                        init();

                    }

                }

                Log.i(TAG, "Termino bien,RESULTADO=" + resultadoLogin);
                return msg;
            } catch (IOException e) {
                e.printStackTrace();
                dialogoProgreso.dismiss();
            }
            return msg;
        }
            @Override
        protected void onCancelled() {
            Toast.makeText(Inicio.this, "Login cancelado" , Toast.LENGTH_SHORT).show();
                dialogoProgreso.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            dialogoProgreso.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {

            dialogoProgreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    TareaLogin.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }


        protected void onPostExecute(String result) {
            Log.d(TAG,"Entro a OnPostExecute");

                if(this.resultadoLogin.equals("1")){
                    Toast.makeText(Inicio.this, "Login Exitoso!",Toast.LENGTH_SHORT).show();

                    /*Se registra inicio de session*/
                    util.registrarSessionEnCache(context,Constantes.SESSION_OPEN);
                    /*Se registra inicio de session*/

                    dialogoProgreso.dismiss();


                    String contacto = util.getContactoCache(context);
                        if(!contacto.equals("contactoDefaultTestLove")){
                        intent = new Intent(Inicio.this, MenuPrincipal.class);
                            init();
                            setToOtherActivity();
                        startActivity(intent);
                        }else {
                            intent = new Intent(Inicio.this, Pendiente.class);
                            init();
                            setToOtherActivity();
                            startActivity(intent);
                        }


                }else{
                    String error = "";
                    if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                        error=" ,"+servicio.getErrorDescripcion();
                    }
                    Toast.makeText(Inicio.this, "Intentelo nuevamente!"+error,Toast.LENGTH_SHORT).show();
                    dialogoProgreso.dismiss();
                };


        }

    }

    public void existOpenSesion(){
        TareaRecuperarSesion tarea = new TareaRecuperarSesion();
        tarea.execute();
    }

    private class TareaRecuperarSesion extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(getApplicationContext());
        SessionDTO sessionDTO = new SessionDTO();
        ContactoDTO cDTO=new ContactoDTO();
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            sessionDTO= servicio.getSesionXUsuario(util.getUserCache(getApplicationContext()));
            cDTO = servicio.recuperarContactoXUsuario(util.getUserCache(getApplicationContext()));

            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            if (sessionDTO != null && sessionDTO.getEstado()==Constantes.SESSION_STARTED) {
                if(cDTO != null){
                    intent = new Intent(Inicio.this, MenuPrincipal.class);
                    init();
                    setToOtherActivity();
                    startActivity(intent);
                }else{
                    intent = new Intent(Inicio.this, Pendiente.class);
                    init();
                    setToOtherActivity();
                    startActivity(intent);
                }

            } else {
                /*String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(Inicio.this, "No se ha cerrado sesion"+error, Toast.LENGTH_SHORT).show();*/
            }

        }
    }

    /**
     * Inicializa DAOs recuperando solo valores desde SharedPreferences
     */
    private void init(){
        if(solicitudEnviadaDAO == null){
            solicitudEnviadaDAO = new SolicitudEnviadaDAO(getApplicationContext());
        }
        if(solicitudRecibidaDAO == null){
            solicitudRecibidaDAO = new SolicitudRecibidaDAO(getApplicationContext());
        }
        if(solicitudContestadaDAO == null){
            solicitudContestadaDAO = new SolicitudContestadaDAO(getApplicationContext());
        }
        if(preguntaEnviadaDAO == null){
            preguntaEnviadaDAO = new PreguntaEnviadaDAO(getApplicationContext());
        }
        if(preguntaRecibidaDAO == null){
            preguntaRecibidaDAO = new PreguntaRecibidaDAO(getApplicationContext());
        }
        if(preguntaContestadaDAO == null){
            preguntaContestadaDAO = new PreguntaContestadaDAO(getApplicationContext());
        }
        if(puntuacionRecibidaDAO == null){
            puntuacionRecibidaDAO = new PuntuacionRecibidaDAO(getApplicationContext());
        }
    }

    /**
     * Agrega datos a intent
     */
    private void setToOtherActivity(){

        util.setArrayListToOtherActivity(intent, solicitudEnviadaDAO.getSolEnviadaDTO(), Constantes.PARCEL_LISTA_SOL_ENVIADA);
        util.setArrayListToOtherActivity(intent, solicitudRecibidaDAO.getSolRecibidaDTO(), Constantes.PARCEL_LISTA_SOL_RECIBIDA);
        util.setArrayListToOtherActivity(intent, solicitudContestadaDAO.getSolContestadaDTO(), Constantes.PARCEL_LISTA_SOL_CONTESTADA);
        util.setArrayListToOtherActivity(intent, preguntaEnviadaDAO.getListaPreguntaEnviadaDTO(), Constantes.PARCEL_LISTA_PREG_ENVIADA);
        util.setArrayListToOtherActivity(intent, preguntaRecibidaDAO.getListaPreguntaRecibidaDTO(), Constantes.PARCEL_LISTA_PREG_RECIBIDA);
        util.setArrayListToOtherActivity(intent, preguntaContestadaDAO.getListaPreguntasContestadasDTO(), Constantes.PARCEL_LISTA_PREG_CONTESTADA);
        util.setArrayListToOtherActivity(intent, puntuacionRecibidaDAO.getListaPuntuacionRecibidasDTO(), Constantes.PARCEL_LISTA_PUNT_RECIBIDA);

    }

}
