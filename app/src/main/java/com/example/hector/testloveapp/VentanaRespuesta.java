package com.example.hector.testloveapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VentanaRespuesta extends AppCompatActivity implements DialogoRespuesta.ClickDialogoRespuestaListener {

    public static final String TAG = "GCMTestLoveApp";


    private String preguntaContestada;
    FragmentManager fragmentManager;
    Context context;
    Util util=new Util();
    DialogoRespuesta dialogoRespuesta;
    ProgressDialog dialogoProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_respuesta);
        context = getApplicationContext();

        dialogoProgreso = new ProgressDialog(VentanaRespuesta.this);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);

        preguntaContestada=util.getPreguntaContestadaEnCache(context);
        fragmentManager = getFragmentManager();
        dialogoRespuesta = dialogoRespuesta.newInstance("Respuesta pregunta");
        dialogoRespuesta.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        dialogoRespuesta.setPreguntaContestada(preguntaContestada);
        dialogoRespuesta.show(fragmentManager, "dialogo_respuesta");


    }

    @Override
    public void onClickCorrecto() {
        TareaPuntuacion tarea = new TareaPuntuacion();
        tarea.execute(util.getUserCache(context), Constantes.PUNTUACION_RESPUESTA_CORRECTA);
    }

    @Override
    public void onClickIncorrecto() {
        TareaPuntuacion tarea = new TareaPuntuacion();
        tarea.execute(util.getUserCache(context), Constantes.PUNTUACION_RESPUESTA_INCORRECTA);
    }

    private class TareaPuntuacion extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            publishProgress(10);
           String msg = "";

           resulTarea= servicio.enviarPuntuacion(context,params[0], params[1]);
            publishProgress(60);
            util.registrarResultadoEnCache(context, params[1]);
            publishProgress(80);
            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");

            if (this.resulTarea.equals("1")) {
                Toast.makeText(VentanaRespuesta.this, "Se ha enviado el resultado", Toast.LENGTH_SHORT).show();
                dialogoProgreso.dismiss();
                //util.eliminarPuntuacionContestadasEnCache(context);
                Intent intent;
                intent = new Intent(VentanaRespuesta.this, MenuPrincipal.class);
                startActivity(intent);

            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(VentanaRespuesta.this, "No se ha enviado el resultado"+error, Toast.LENGTH_SHORT).show();
                dialogoProgreso.dismiss();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(VentanaRespuesta.this, "Se ha cancelado el envio del resultado" , Toast.LENGTH_SHORT).show();
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
                    TareaPuntuacion.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }
}
