package com.example.hector.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.exceptions.ConnectionException;
import com.example.hector.exceptions.HttpCallException;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.ServicioRest;
import com.example.hector.testloveapp.Util;

import DTO.SolicitudDTO;

/**
 * Created by hectorfrancisco on 24-07-2016.
 */
public class TareaEnviarSolicitudContacto extends AsyncTask<String,Integer,String> {

    private ServicioRest servicio;
    private String resulTarea="0";
    private Context context;
    private Util util;
    private ProgressDialog dialogoProgreso;
    private OnUpdateUI onUpdateUI;
    private SolicitudEnviadaDAO solicitudEnviadaDAO;
    private SolicitudDTO solicitudDTO;

    public TareaEnviarSolicitudContacto(Context context,SolicitudEnviadaDAO solicitudEnviadaDAO) {
        this.context = context;
        this.servicio = new ServicioRest(context);
        this.util = new Util();
        this.solicitudEnviadaDAO = solicitudEnviadaDAO;
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
        publishProgress(10);
        //Nos registramos en nuestro servidor
        try {
            solicitudDTO = servicio.enviarSolicitudContacto(params[0], params[1]);
        } catch (ConnectionException e) {
            resulTarea = e.getDescError();
        } catch (HttpCallException e) {
            resulTarea = e.getDescError();
        }

        if(resulTarea.equals("0")){
            solicitudEnviadaDAO.add(solicitudDTO);
            //util.registrarSolicitudContactoPendienteCache(context,params[1]);
        }

        publishProgress(70);
        return msg;
    }

    protected void onPostExecute(String result) {

       dialogoProgreso.dismiss();
        if (this.resulTarea.equals("0")) {
            Toast.makeText(context, "Se envio solicitud de contacto!", Toast.LENGTH_SHORT).show();
            //onUpdateUI.updateUI();

        } else {
           // onUpdateUI.updateUI()
            Toast.makeText(context, "No se ha logrado enviar solicitud a contacto"+this.resulTarea, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, "Solicitud de contacto cancelada" , Toast.LENGTH_SHORT).show();
       dialogoProgreso.dismiss();

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();

        dialogoProgreso.setProgress(progreso);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogoProgreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                TareaEnviarSolicitudContacto.this.cancel(true);
            }
        });

       dialogoProgreso.setProgress(0);
        dialogoProgreso.show();
    }

    public interface OnUpdateUI{
        void updateUI();
    }


}
