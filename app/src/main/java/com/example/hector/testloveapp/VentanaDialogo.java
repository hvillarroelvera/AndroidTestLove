package com.example.hector.testloveapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hector.exceptions.ConnectionException;
import com.example.hector.exceptions.HttpCallException;

public class VentanaDialogo extends AppCompatActivity implements DialogoSolicitudContacto.ClickDialogoSolicitudContactoListener {

    public static final String TAG = "GCMTestLoveApp";
    private String nombreUserSolicitante;
    Context context;
    Util util=new Util();
    FragmentManager fragmentManager;
    DialogoSolicitudContacto dialogoSolicitudContacto;
    ProgressDialog dialogoProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_dialogo);
        context = getApplicationContext();

        dialogoProgreso = new ProgressDialog(VentanaDialogo.this);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);

        nombreUserSolicitante=util.getSolicitanteCache(getApplicationContext());

        fragmentManager = getFragmentManager();
        dialogoSolicitudContacto = dialogoSolicitudContacto.newInstance("Solicitud contacto");
        dialogoSolicitudContacto.setNombreUserSolicitante(nombreUserSolicitante);
        dialogoSolicitudContacto.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        dialogoSolicitudContacto.show(fragmentManager,"Solicitud contacto");
    }

    @Override
    public void onClickAceptar() {
        TareaRegistrarContacto tarea=new TareaRegistrarContacto();
        tarea.execute(nombreUserSolicitante, util.getUserCache(context),Integer.toString(Constantes.SUCCESS));
    }

    @Override
    public void onClickRechazar() {
        TareaRegistrarContacto tarea=new TareaRegistrarContacto();
        tarea.execute(nombreUserSolicitante, util.getUserCache(context),Integer.toString(Constantes.NOT_SUCCESS));
    }


    private class TareaRegistrarContacto extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            publishProgress(10);
            String msg = "";


            try {
                servicio.registrarContacto(params[0],params[1],Short.parseShort(params[2]));
            } catch (ConnectionException e) {
                resulTarea = e.getDescError();
            } catch (HttpCallException e) {
                resulTarea = e.getDescError();
            }
            publishProgress(70);

            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");

            if (this.resulTarea.equals("0")) {
                Toast.makeText(VentanaDialogo.this, "Ya te has registrado como contacto", Toast.LENGTH_SHORT).show();
                util.eliminarUsuarioSolicitanteEnCache(context);
                dialogoProgreso.dismiss();
                finish();
            } else {

                Toast.makeText(VentanaDialogo.this, "No te has registrado como contacto"+resulTarea, Toast.LENGTH_SHORT).show();
                dialogoProgreso.dismiss();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(VentanaDialogo.this, "Se ha cancelado tu registro como contacto" , Toast.LENGTH_SHORT).show();
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
                    TareaRegistrarContacto.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }
}
