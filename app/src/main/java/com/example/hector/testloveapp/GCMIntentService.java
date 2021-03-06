package com.example.hector.testloveapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.hector.DAO.SolicitudContestadaDAO;
import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.DAO.SolicitudRecibidaDAO;
import com.example.hector.exceptions.ConnectionException;
import com.example.hector.exceptions.HttpCallException;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import DTO.ContactoDTO;
import DTO.PreguntaDTO;
import DTO.SolicitudDTO;

/**
 * Created by hector on 21-06-2015.
 */
public class GCMIntentService extends IntentService {
    private static final int NOTIF_ALERTA_ID = 1;
    public static final String TAG = "GCMTestLoveApp";

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.i(TAG,"INI GCMIntentService");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {

                if(extras.containsKey(Constantes.TIPO_MENSAJE_GCM_SOLICITUD_CONTACTO)){

                   mostrarNotificacionSolicitudContacto(extras.getString(Constantes.TIPO_MENSAJE_GCM_SOLICITUD_CONTACTO));
                }else if(extras.containsKey(Constantes.TIPO_MENSAJE_GCM_RESULTADO_SOLICITUD_CONTACTO)){

                mostrarNotificacionRespSolicitudContacto(extras.getString(Constantes.TIPO_MENSAJE_GCM_RESULTADO_SOLICITUD_CONTACTO));
            }else if(extras.containsKey(Constantes.TIPO_MENSAJE_GCM_PREGUNTA)){

                    mostrarNotificacionPregunta(extras.getString(Constantes.TIPO_MENSAJE_GCM_PREGUNTA));
                }
                else if(extras.containsKey(Constantes.TIPO_MENSAJE_GCM_RESPUESTA_PREGUNTA)){

                    mostrarNotificacionPreguntaContestada(extras.getString(Constantes.TIPO_MENSAJE_GCM_RESPUESTA_PREGUNTA));
                }
                else if(extras.containsKey(Constantes.TIPO_MENSAJE_GCM_PUNTUACION)){

                    mostrarNotificacionPuntuacion(extras.getString(Constantes.TIPO_MENSAJE_GCM_PUNTUACION));
                }


            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }



    private void mostrarNotificacionSolicitudContacto(String msg){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Util util=new Util();
        SolicitudRecibidaDAO solicitudRecibidaDAO = new SolicitudRecibidaDAO(getApplicationContext());
        SolicitudDTO solicitudDTO = new SolicitudDTO();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //Vibration
                        .setVibrate(new long[]{0,500, 300,500})
                        .setSmallIcon(android.R.drawable.stat_notify_chat)
                        .setContentTitle("TestLove")
                        .setContentText("El usuario " + msg + " desea ser tu amigo!!")
                        .setAutoCancel(true);

        Intent notIntent =  new Intent(this, Pendiente.class);

        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);
        Log.i(TAG, getApplicationContext().getPackageResourcePath());
        //util.registrarUsuarioSolicitanteEnCache(getApplicationContext(), msg);

        solicitudDTO = util.getSolicitudDTOFromMsgGcm(msg);
        solicitudRecibidaDAO.add(solicitudDTO);
        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }

    private void mostrarNotificacionRespSolicitudContacto(String msg){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		SolicitudContestadaDAO solicitudContestadaDAO = new SolicitudContestadaDAO(getApplicationContext());
		SolicitudDTO solicitudDTO = new SolicitudDTO();
        Util util=new Util();
        ServicioRest servicioRest = new ServicioRest(getApplicationContext());
        ContactoDTO contactoDTO = new ContactoDTO();
        SolicitudEnviadaDAO solicitudEnviadaDAO = new SolicitudEnviadaDAO(getApplicationContext());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //Vibration
                        .setVibrate(new long[]{0,500, 300,500})
                        .setSmallIcon(android.R.drawable.star_big_on)
                        .setContentTitle("TestLove")
                        .setContentText("El usuario "+msg+" ha aceptado ser tu amigo!!")
                        .setAutoCancel(true);

        Intent notIntent =  new Intent(this, MenuPrincipal.class);

        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

				/*VALIDAR QUE HACER CUANDO ES ACEPTADA O RECHAZADA*/
		solicitudDTO = util.getSolicitudDTOReceptorFromMsgGcm(msg);
		solicitudContestadaDAO.add(solicitudDTO);
		/*VALIDAR QUE HACER CUANDO ES ACEPTADA O RECHAZADA*/
		
        /*try {
            contactoDTO = servicioRest.recuperarContactoXUsuario(util.getUserCache(getApplicationContext()));
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (HttpCallException e) {
            e.printStackTrace();
        }
        util.registrarContactoEnCache(getApplicationContext(), contactoDTO.getContacto(), Long.toString(contactoDTO.getId_contacto()));
        util.eliminarSolicitudContactoPendienteCache(getApplicationContext());
        solicitudEnviadaDAO.deleteByNombreContacto(contactoDTO.getContacto());*/
        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }

    private void mostrarNotificacionPregunta(String msg){
        Log.i(TAG,"INI mostrarNotificacionPregunta");
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Util util=new Util();
        PreguntaDTO preguntaDTO = new PreguntaDTO();
        /*Seteo de datos desde msg a objeto PreguntaDTO*/
        preguntaDTO = util.getPreguntaDTOFromMsgGcm(msg);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //Vibration
                        .setVibrate(new long[]{0,500, 300,500})
                        .setSmallIcon(android.R.drawable.star_off)
                        .setContentTitle("TestLove")
                        .setContentText("Tu contacto te ha preguntado:"+preguntaDTO.getPregunta())
                        .setAutoCancel(true);

        Intent notIntent =  new Intent(this, VentanaPregunta.class);

        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

        util.registrarNumeroAndPreguntaRecibidaEnCache(getApplicationContext(), preguntaDTO.getPregunta(), preguntaDTO.getNumero());
        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }

    private void mostrarNotificacionPreguntaContestada(String msg){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Util util=new Util();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //Vibration
                        .setVibrate(new long[]{0,500, 300,500})
                        .setSmallIcon(android.R.drawable.star_off)
                        .setContentTitle("TestLove")
                        .setContentText("Tu contacto te ha respondido")
                        .setAutoCancel(true);

        Intent notIntent =  new Intent(this, VentanaRespuesta.class);

        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

        util.registrarPreguntaContestadaEnCache(getApplicationContext(), msg);
        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }
    private void mostrarNotificacionPuntuacion(String msg){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Util util=new Util();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //Vibration
                        .setVibrate(new long[]{0,500, 300,500})
                        .setSmallIcon(android.R.drawable.star_off)
                        .setContentTitle("TestLove")
                        .setContentText("Ha llegado el resultado de tu respuesta!!!!!")
                        .setAutoCancel(true);

        Intent notIntent =  new Intent(this, VentanaPuntuacion.class);

        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

        util.registrarPuntuacionContestadasEnCache(getApplicationContext(), Long.parseLong(msg));
        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }
}
