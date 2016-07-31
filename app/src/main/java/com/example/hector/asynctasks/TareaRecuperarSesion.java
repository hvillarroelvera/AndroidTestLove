package com.example.hector.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.hector.DAO.PreguntaContestadaDAO;
import com.example.hector.DAO.PreguntaEnviadaDAO;
import com.example.hector.DAO.PreguntaRecibidaDAO;
import com.example.hector.DAO.PuntuacionRecibidaDAO;
import com.example.hector.DAO.SolicitudContestadaDAO;
import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.DAO.SolicitudRecibidaDAO;
import com.example.hector.exceptions.ConnectionException;
import com.example.hector.exceptions.HttpCallException;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Inicio;
import com.example.hector.testloveapp.Pendiente;
import com.example.hector.testloveapp.ServicioRest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.example.hector.testloveapp.Util;
import com.example.hector.DAO.PreguntaContestadaDAO;
import com.example.hector.DAO.PreguntaEnviadaDAO;
import com.example.hector.DAO.PreguntaRecibidaDAO;
import com.example.hector.DAO.PuntuacionRecibidaDAO;
import com.example.hector.DAO.SolicitudContestadaDAO;
import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.DAO.SolicitudRecibidaDAO;

import DTO.ContactoDTO;
import DTO.SessionDTO;

public class TareaRecuperarSesion extends AsyncTask<String,Integer,String>{
	
	
	private ServicioRest servicio;
    private String resulTarea="0";
    private Context context;
    private Util util;
    private ProgressDialog dialogoProgreso;
    private SessionDTO sessionDTO;
	private ContactoDTO cDTO;
	private Intent intent;
    private SolicitudEnviadaDAO solicitudEnviadaDAO;
    private SolicitudRecibidaDAO solicitudRecibidaDAO;
    private SolicitudContestadaDAO solicitudContestadaDAO;
    private PreguntaEnviadaDAO preguntaEnviadaDAO;
    private PreguntaRecibidaDAO preguntaRecibidaDAO;
    private PreguntaContestadaDAO preguntaContestadaDAO;
    private PuntuacionRecibidaDAO puntuacionRecibidaDAO;

    public TareaRecuperarSesion(Context context,Intent intent) {
        this.context = context;
        this.servicio = new ServicioRest(context);
        this.util = new Util();
        this.sessionDTO = new SessionDTO();
		this.cDTO=new ContactoDTO();
		this.intent = intent;
        dialogoProgreso = new ProgressDialog(context);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);
    }
	
	
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
			try {
				
            sessionDTO= servicio.getSesionXUsuario(util.getUserCache(context));
            cDTO = servicio.recuperarContactoXUsuario(util.getUserCache(context));
			} catch (ConnectionException e) {
            resulTarea = e.getDescError();
			} catch (HttpCallException e) {
			resulTarea = e.getDescError();
			}
			
            return msg;
        }

        protected void onPostExecute(String result) {

            if (sessionDTO != null && sessionDTO.getEstado()==Constantes.SESSION_STARTED) {
                if(cDTO != null){
                    
                    init();
                    setToOtherActivity();
                    context.startActivity(intent);
                }else{
                    
                    init();
                    setToOtherActivity();
                    context.startActivity(intent);
                }

            } else {
                
                Toast.makeText(context, "No se ha recuperado sesion"+resulTarea, Toast.LENGTH_SHORT).show();
            }

        }


    /**
     * Inicializa DAOs recuperando solo valores desde SharedPreferences
     */
    private void init(){
        if(solicitudEnviadaDAO == null){
            solicitudEnviadaDAO = new SolicitudEnviadaDAO(context);
        }
        if(solicitudRecibidaDAO == null){
            solicitudRecibidaDAO = new SolicitudRecibidaDAO(context);
        }
        if(solicitudContestadaDAO == null){
            solicitudContestadaDAO = new SolicitudContestadaDAO(context);
        }
        if(preguntaEnviadaDAO == null){
            preguntaEnviadaDAO = new PreguntaEnviadaDAO(context);
        }
        if(preguntaRecibidaDAO == null){
            preguntaRecibidaDAO = new PreguntaRecibidaDAO(context);
        }
        if(preguntaContestadaDAO == null){
            preguntaContestadaDAO = new PreguntaContestadaDAO(context);
        }
        if(puntuacionRecibidaDAO == null){
            puntuacionRecibidaDAO = new PuntuacionRecibidaDAO(context);
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