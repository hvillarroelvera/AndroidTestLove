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

import java.io.IOException;
import java.util.ArrayList;

import DTO.ContactoDTO;
import DTO.GcmDTO;
import DTO.PreguntaDTO;
import DTO.PreguntasDTO;
import DTO.SolicitudDTO;
import com.example.hector.testloveapp.Util;

public class TareaLogin extends AsyncTask<String,Integer,String>
    {
        private ServicioRest servicio;
        private String resultadoLogin="0";
        private String resulActualizarGcm="0";
		private String resulTarea="0";
        private GcmDTO gcmdto;
        private ContactoDTO cDTO;
        private PreguntasDTO pDTO;
        private ArrayList<SolicitudDTO> listaSolicitudes;
        private SolicitudDTO solicitudDTO;
        private int resultCompCache=0;
        private long newExpirationTime = 0;
		private Intent intent;
		private ProgressDialog dialogoProgreso;
		public static GoogleCloudMessaging gcm;
        private String regid="";
        private Context context;
        private Util util;
        private SolicitudEnviadaDAO solicitudEnviadaDAO;
        private SolicitudRecibidaDAO solicitudRecibidaDAO;
        private SolicitudContestadaDAO solicitudContestadaDAO;
        private PreguntaEnviadaDAO preguntaEnviadaDAO;
        private PreguntaRecibidaDAO preguntaRecibidaDAO;
        private PreguntaContestadaDAO preguntaContestadaDAO;
        private PuntuacionRecibidaDAO puntuacionRecibidaDAO;
        private AsyncDelegate asyncDelegate;
		
		public TareaLogin(Context context,Intent intent,AsyncDelegate asyncDelegate) {
        this.context = context;
        this.servicio = new ServicioRest(context);
        this.util = new Util();
        this.gcmdto=new GcmDTO();
		this.cDTO=new ContactoDTO();
		this.pDTO=new PreguntasDTO();
		this.listaSolicitudes = new ArrayList<SolicitudDTO>();
		this.solicitudDTO = new SolicitudDTO();
            this.asyncDelegate = asyncDelegate;
		this.intent = intent;
        dialogoProgreso = new ProgressDialog(context);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);
    }

        @Override
        protected String doInBackground(String... params) {
            String msg = "";


            try {
                publishProgress(10);
                
                resultCompCache = util.compararGcmCache(context, Inicio.class, params[0]);
                


                if(resultCompCache == Constantes.CACHE_NOT_FOUND_USER || resultCompCache == Constantes.CACHE_NOT_FOUND_REG_ID){
                    //Si no es el mismo usuario se recuperan datos desde el servidor y se validan
                    //Si no es el mismo regId se recuperan datos desde el servidor y se validan
                    publishProgress(60);
					try {
                    servicio.loguearUsuario(params[0], params[1]);
					} catch (ConnectionException e) {
					resultadoLogin = e.getDescError();
					} catch (HttpCallException e) {
					resultadoLogin = e.getDescError();
					}
                    if(resultadoLogin.equals("0")) {
                        publishProgress(70);
						try {
                        gcmdto = servicio.recuperarGcmXUsuario(params[0]);
                        cDTO = servicio.recuperarContactoXUsuario(params[0]);
                        pDTO = servicio.getPreguntasXUsuario(params[0]);
                        listaSolicitudes = servicio.getSolicitudContacto(params[0]);
						} catch (ConnectionException e) {
						resulTarea = e.getDescError();
						} catch (HttpCallException e) {
						resulTarea = e.getDescError();
						}
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
                            try {
                                regid = gcm.register(Constantes.SENDER_ID);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            /*actualizar informacion en servidor*/
                            newExpirationTime = System.currentTimeMillis() + Constantes.EXPIRATION_TIME_MS;
							try {
                            servicio.actualizarGcm(params[0],regid,util.getAppVersion(context),newExpirationTime);
							} catch (ConnectionException e) {
							resulActualizarGcm = e.getDescError();
							} catch (HttpCallException e) {
							resulActualizarGcm = e.getDescError();
							}
                                if(resulActualizarGcm.equals("0")){
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
					try {
                    servicio.actualizarGcm(params[0],regid,util.getAppVersion(context),newExpirationTime);
                   } catch (ConnectionException e) {
					resulActualizarGcm = e.getDescError();
					} catch (HttpCallException e) {
					resulActualizarGcm = e.getDescError();
					}
				   if(resulActualizarGcm.equals("0")){
                        util.actualizarDatosCacheFromServidor(context, params[0], regid,newExpirationTime);
                    }



                }else{
                    publishProgress(70);
					try {
                    servicio.loguearUsuario(params[0], params[1]);
					} catch (ConnectionException e) {
					resultadoLogin = e.getDescError();
					} catch (HttpCallException e) {
					resultadoLogin = e.getDescError();
					}
                    if(resultadoLogin.equals("0")) {

                        //init();

                    }

                }

             
                return msg;
            } catch (IOException e) {
                e.printStackTrace();
                dialogoProgreso.dismiss();
            }
            return msg;
        }
            @Override
        protected void onCancelled() {
            Toast.makeText(context, "Login cancelado", Toast.LENGTH_SHORT).show();
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


                if(this.resultadoLogin.equals("0")){
                    Toast.makeText(context, "Login Exitoso!",Toast.LENGTH_SHORT).show();

                    /*Se registra inicio de session*/
                    util.registrarSessionEnCache(context,Constantes.SESSION_OPEN);
                    /*Se registra inicio de session*/

                    dialogoProgreso.dismiss();


                    String contacto = util.getContactoCache(context);
                        if(!contacto.equals("contactoDefaultTestLove")){
                        /*intent = new Intent(context, MenuPrincipal.class);
                            init();
                            setToOtherActivity();
                        startActivity(intent);*/
						/*Por el momento siempre se redireccionara a Pendiente*/
						 intent = new Intent(context, Pendiente.class);
                            /*init();
                            setToOtherActivity();
                            startActivity(intent);*/
                        }else {
                            intent = new Intent(context, Pendiente.class);
                            /*init();
                            setToOtherActivity();
                            startActivity(intent);*/
                        }


                }else{
                   
                    Toast.makeText(context, "Intentelo nuevamente!"+resultadoLogin,Toast.LENGTH_SHORT).show();
                    dialogoProgreso.dismiss();
                };


        }

    }