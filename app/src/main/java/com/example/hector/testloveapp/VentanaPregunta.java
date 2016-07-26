package com.example.hector.testloveapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import DTO.PreguntaDTO;

public class VentanaPregunta extends AppCompatActivity implements DialogoPregunta.ClickDialogoPreguntaListener {

    public static final String TAG = "GCMTestLoveApp";

    PreguntaDTO preguntaDTO = new PreguntaDTO();
    Context context;
    Util util=new Util();
    FragmentManager fragmentManager;
    DialogoPregunta dialogoPregunta;
    ProgressDialog dialogoProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"INI VentanaPregunta");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_pregunta);
        context = getApplicationContext();

        dialogoProgreso = new ProgressDialog(VentanaPregunta.this);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);

        preguntaDTO = util.getNumeroAndPreguntaRecibidaEnCache(getApplicationContext());
        fragmentManager = getFragmentManager();
        dialogoPregunta = dialogoPregunta.newInstance("Responder pregunta");
        dialogoPregunta.setPreguntaDTO(preguntaDTO);
        dialogoPregunta.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        dialogoPregunta.show(fragmentManager,"dialogo_pregunta");

    }

    @Override
    public void onClickResponder(String texto) {
        TareaResponderPregunta tarea = new TareaResponderPregunta();
        tarea.execute( util.getUserCache(context),preguntaDTO.getPregunta(),Long.toString(preguntaDTO.getNumero()),texto);
    }

    private class TareaResponderPregunta extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            publishProgress(10);
            String msg = "";


            resulTarea= servicio.enviarRespuestaPregunta(params[0],params[1],Long.parseLong(params[2]),params[3]);
            publishProgress(70);
            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");

            if (this.resulTarea.equals("1")) {
                Toast.makeText(VentanaPregunta.this, "Se ha enviado tu respuesta", Toast.LENGTH_SHORT).show();
                util.eliminarNumeroAndPreguntaRecibidaEnCache(context);
                dialogoProgreso.dismiss();
                finish();
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(VentanaPregunta.this, "No se ha enviado tu respuesta"+error, Toast.LENGTH_SHORT).show();
                dialogoProgreso.dismiss();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(VentanaPregunta.this, "Se ha cancelado el envio de la respuesta" , Toast.LENGTH_SHORT).show();
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
                    TareaResponderPregunta.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }
}
