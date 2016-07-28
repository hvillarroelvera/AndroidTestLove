package com.example.hector.testloveapp;

import android.animation.Animator;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.ArrayList;

import DTO.PreguntaDTO;
import DTO.PuntuacionDTO;
import DTO.SolicitudDTO;
import DTO.SolicitudEnviadaDTO;

public class Contacto extends AppCompatActivity implements DialogoAgregar.ClickDialogoAgregarListener {

    private Button buttonAgregarContacto,buttonEliminarSolicitud,buttonReenviarSolicitud;
    FloatingActionButton fabAgregarContacto,fabEliminarSolicitud,fabReenviarSolicitud;
    private TextView txtNinguno;
    Util util = new Util();
    private String SolicitudContactoPendiente;
    private Context context;
    ProgressDialog dialogoProgreso;
    FragmentManager fragmentManager;
    DialogoAgregar dialogoAgregar;
    public static final String TAG = "GCMTestLoveApp";
    private Toolbar toolbar;
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
        setContentView(R.layout.activity_contacto);
        txtNinguno=(TextView) findViewById(R.id.txtSolicitudesPendientesNinguna);
        fabAgregarContacto=(FloatingActionButton) findViewById(R.id.fabAgregarContacto);
        fabEliminarSolicitud=(FloatingActionButton) findViewById(R.id.fabEliminarContacto);
        fabReenviarSolicitud=(FloatingActionButton) findViewById(R.id.fabReenviarSolContacto);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        /*Objetos para animacion via xml*/
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        /*Objetos para animacion via xml*/

        intent = getIntent();
        getDataFromOtherActivity();



        fabAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Investigar alternativas de animacion
                este metodo solo se ejecuta en Lollypop*/
                animationClickOnFloatingActionbutton(fabAgregarContacto);

                fragmentManager = getFragmentManager();
                dialogoAgregar = DialogoAgregar.newInstance("Agregar contacto");
                dialogoAgregar.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
                dialogoAgregar.setTipoDialogo(Constantes.TIPO_DIALOGO_AGREGAR_CONTACTO);
                dialogoAgregar.show(fragmentManager, "dialogo_agregar");
            }
        });

        fabEliminarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Investigar alternativas de animacion
                este metodo solo se ejecuta en Lollypop*/
                animationClickOnFloatingActionbutton(fabEliminarSolicitud);

                TareaEliminarSolicitudContacto tarea = new TareaEliminarSolicitudContacto();
                tarea.execute(util.getUserCache(context), util.getSolicitudContactoPendienteCache(context));

            }
        });

        fabReenviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Investigar alternativas de animacion
                este metodo solo se ejecuta en Lollypop*/
                animationClickOnFloatingActionbutton(fabReenviarSolicitud);

                TareaReenviarSolicitudContacto tarea = new TareaReenviarSolicitudContacto();
                tarea.execute(util.getUserCache(context),util.getSolicitudContactoPendienteCache(context) );

            }
        });
        SolicitudContactoPendiente = util.getSolicitudContactoPendienteCache(context);
        cargarInterfaz();

        dialogoProgreso = new ProgressDialog(Contacto.this);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.pendientes:
                clickPendiente();
                return true;
            case R.id.agregarPregunta:
                Toast.makeText(Contacto.this, "Debes enviar una solicitud de contacto antes de Agregar Pregunta", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.eliminarPregunta:
                Toast.makeText(Contacto.this, "Debes enviar una solicitud de contacto antes de Eliminar Pregunta", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.eliminarContacto:
                Toast.makeText(Contacto.this, "Debes enviar una solicitud de contacto antes de Eliminar Pregunta", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opciones:
                return true;
            case R.id.salir:
                clickCerrarSesion();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void clickCerrarSesion(){
        TareaCerrarSesion tarea=new TareaCerrarSesion();
        tarea.execute(util.getUserCache(context));
    }

    public void clickPendiente(){
        //Intent intent;
        intent = new Intent(Contacto.this, Pendiente.class);
        setToOtherActivity();
        startActivity(intent);
    }

    @Override
    public void onClickAgregar(String texto, short tipoDialogo) {
        if(tipoDialogo == Constantes.TIPO_DIALOGO_AGREGAR_CONTACTO) {
            TareaEnviarSolicitudContacto tarea = new TareaEnviarSolicitudContacto();
            tarea.execute(util.getUserCache(context), texto);
        }
    }


    private class TareaCerrarSesion extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            publishProgress(10);
            resulTarea= servicio.cerrarSesion(params[0]);
            publishProgress(70);

            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(Contacto.this, "Se ha cerrado la sesion!", Toast.LENGTH_SHORT).show();
                intent = new Intent(Contacto.this, Inicio.class);
                startActivity(intent);
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(Contacto.this, "No se ha cerrado sesion"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Contacto.this, "Se ha cancelado cierre de sesion" , Toast.LENGTH_SHORT).show();
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


    private class TareaEnviarSolicitudContacto extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            publishProgress(10);
            //Nos registramos en nuestro servidor
            try {
                servicio.enviarSolicitudContacto(params[0], params[1]);
            } catch (ConnectionException e) {
                e.printStackTrace();
            } catch (HttpCallException e) {
                e.printStackTrace();
            }

            if(resulTarea.equals("1")||
                    resulTarea.equals(Constantes.RESULTADO_STATUS_409)){

                util.registrarSolicitudContactoPendienteCache(context,params[1]);

            }

            publishProgress(70);
            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(Contacto.this, "Se envio solicitud de contacto!", Toast.LENGTH_SHORT).show();
                cargarInterfaz();
            } else {
                cargarInterfaz();
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(Contacto.this, "No se ha logrado enviar solicitud a contacto"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Contacto.this, "Solicitud de contacto cancelada" , Toast.LENGTH_SHORT).show();
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
                    TareaEnviarSolicitudContacto.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }

    private class TareaEliminarSolicitudContacto extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            publishProgress(10);
            //Nos registramos en nuestro servidor
            resulTarea= servicio.eliminarSolicitudContacto(params[0], params[1]);

            if(resulTarea.equals("1")){
                util.eliminarSolicitudContactoPendienteCache(context);
            }

            publishProgress(70);
            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(Contacto.this, "Se elimino solicitud de contacto!", Toast.LENGTH_SHORT).show();
                cargarInterfaz();
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(Contacto.this, "No se ha logrado elimino solicitud de contacto"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Contacto.this, "Eliminacion de solicitud de contacto cancelada" , Toast.LENGTH_SHORT).show();
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
                    TareaEliminarSolicitudContacto.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }

    private class TareaReenviarSolicitudContacto extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            publishProgress(10);
            //Nos registramos en nuestro servidor
            resulTarea= servicio.reenviarSolicitudContacto(params[0], params[1]);

            publishProgress(70);
            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(Contacto.this, "Se reenvio solicitud de contacto!", Toast.LENGTH_SHORT).show();
                cargarInterfaz();
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(Contacto.this, "No se ha logrado reenviar solicitud de contacto"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Contacto.this, "Reenvio de solicitud de contacto cancelada" , Toast.LENGTH_SHORT).show();
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
                    TareaReenviarSolicitudContacto.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }

    public void cargarInterfaz(){

        SolicitudContactoPendiente = util.getSolicitudContactoPendienteCache(context);
        if(!SolicitudContactoPendiente.equals("contactoPendienteDefaultTestLove")){
            txtNinguno.setText(SolicitudContactoPendiente);
            txtNinguno.setWidth(90);
            txtNinguno.setHeight(90);

            /*Fabs*/
            fabAgregarContacto.setVisibility(FloatingActionButton.GONE);
            fabEliminarSolicitud.setVisibility(FloatingActionButton.VISIBLE);
            animationAppearFloatingActionbutton(fabEliminarSolicitud);
            fabReenviarSolicitud.setVisibility(FloatingActionButton.VISIBLE);
            animationAppearFloatingActionbutton(fabReenviarSolicitud);
            /*Fabs*/
        }else{
            txtNinguno.setText("Ninguna");
            /*Fabs*/
            fabAgregarContacto.setVisibility(FloatingActionButton.VISIBLE);
            animationAppearFloatingActionbutton(fabAgregarContacto);
            fabEliminarSolicitud.setVisibility(FloatingActionButton.GONE);
            fabReenviarSolicitud.setVisibility(FloatingActionButton.GONE);
            /*Fabs*/
        }
    }

    public void animationClickOnFloatingActionbutton(final FloatingActionButton buttonClicked){


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.fast_out_slow_in);

            buttonClicked.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(1000)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            buttonClicked.animate()
                                    .scaleY(0)
                                    .scaleX(0)
                                    .setInterpolator(interpolador)
                                    .setDuration(600)
                                    .start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }

    }

    public void animationAppearFloatingActionbutton(final FloatingActionButton buttonClicked){
         /*INI Animaciones fab*/

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.fast_out_slow_in);

            buttonClicked.animate()
                    .scaleX(0)
                    .scaleY(0)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(1000)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            buttonClicked.animate()
                                    .scaleY(1)
                                    .scaleX(1)
                                    .setInterpolator(interpolador)
                                    .setDuration(600)
                                    .start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
        /*FIN Animaciones fab*/


    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Obtiene datos desde intent
     */
    public void getDataFromOtherActivity(){
        /*get sol enviada*/
        ArrayList<SolicitudDTO> solEnviadaDTO = (ArrayList<SolicitudDTO>)util.getArrayListFromOtherActivity(intent, Constantes.PARCEL_LISTA_SOL_ENVIADA);
        solicitudEnviadaDAO = new SolicitudEnviadaDAO(solEnviadaDTO,context);
         /*get sol enviada*/
        /*get sol recibida*/
        ArrayList<SolicitudDTO> solRecibidaDTO = (ArrayList<SolicitudDTO>)util.getArrayListFromOtherActivity(intent, Constantes.PARCEL_LISTA_SOL_RECIBIDA);
        solicitudRecibidaDAO = new SolicitudRecibidaDAO(solRecibidaDTO,context);
        /*get sol recibida*/
        /*get sol contestada*/
        ArrayList<SolicitudDTO> solContestadaDTO = (ArrayList<SolicitudDTO>)util.getArrayListFromOtherActivity(intent, Constantes.PARCEL_LISTA_SOL_CONTESTADA);
        solicitudContestadaDAO = new SolicitudContestadaDAO(solContestadaDTO,context);
        /*get sol contestada*/
        /*get sol contestada*/
        ArrayList<PreguntaDTO> preguntaEnviadaDTO = (ArrayList<PreguntaDTO>)util.getArrayListFromOtherActivity(intent, Constantes.PARCEL_LISTA_PREG_ENVIADA);
        preguntaEnviadaDAO = new PreguntaEnviadaDAO(preguntaEnviadaDTO,context);
        /*get sol contestada*/
        /*get sol contestada*/
        ArrayList<PreguntaDTO> preguntaRecibidaDTO = (ArrayList<PreguntaDTO>)util.getArrayListFromOtherActivity(intent, Constantes.PARCEL_LISTA_PREG_RECIBIDA);
        preguntaRecibidaDAO = new PreguntaRecibidaDAO(preguntaRecibidaDTO,context);
        /*get sol contestada*/
        /*get sol contestada*/
        ArrayList<PreguntaDTO> preguntaContestadaDTO = (ArrayList<PreguntaDTO>)util.getArrayListFromOtherActivity(intent, Constantes.PARCEL_LISTA_PREG_CONTESTADA);
        preguntaContestadaDAO = new PreguntaContestadaDAO(preguntaContestadaDTO,context);
        /*get sol contestada*/
        /*get sol contestada*/
        ArrayList<PuntuacionDTO> puntuacionRecibidaDTO = (ArrayList<PuntuacionDTO>)util.getArrayListFromOtherActivity(intent, Constantes.PARCEL_LISTA_PUNT_RECIBIDA);
        puntuacionRecibidaDAO = new PuntuacionRecibidaDAO(puntuacionRecibidaDTO,context);
        /*get sol contestada*/


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
