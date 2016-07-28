package com.example.hector.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.exceptions.ConnectionException;
import com.example.hector.exceptions.HttpCallException;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Inicio;
import com.example.hector.testloveapp.ServicioRest;
import com.example.hector.testloveapp.Util;

/**
 * Created by hectorfrancisco on 25-07-2016.
 */
public class TareaCerrarSesion extends AsyncTask<String,Integer,String> {

    private ServicioRest servicio;
    private String resulTarea="0";
    private Context context;
    private Util util;
    private ProgressDialog dialogoProgreso;
    private Intent intent;

    public TareaCerrarSesion(Context context,Intent intent) {
        this.context = context;
        this.servicio = new ServicioRest(context);
        this.util = new Util();
        dialogoProgreso = new ProgressDialog(context);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);
        this.intent = intent;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String msg = "";
        publishProgress(10);
        try {
            servicio.cerrarSesion(params[0]);
        } catch (ConnectionException e) {
            resulTarea = e.getDescError();
        } catch (HttpCallException e) {
            resulTarea = e.getDescError();
        }
        publishProgress(70);

        return msg;
    }

    protected void onPostExecute(String result) {

        dialogoProgreso.dismiss();
        if (this.resulTarea.equals("0")) {
            Toast.makeText(context, "Se ha cerrado la sesion!", Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        } else {

            Toast.makeText(context, "No se ha cerrado sesion"+resulTarea, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, "Se ha cancelado cierre de sesion" , Toast.LENGTH_SHORT).show();
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
                TareaCerrarSesion.this.cancel(true);
            }
        });

        dialogoProgreso.setProgress(0);
        dialogoProgreso.show();
    }
}
