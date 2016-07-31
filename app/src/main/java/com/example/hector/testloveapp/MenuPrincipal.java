package com.example.hector.testloveapp;

import android.animation.Animator;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.hector.DAO.PreguntaContestadaDAO;
import com.example.hector.DAO.PreguntaEnviadaDAO;
import com.example.hector.DAO.PreguntaRecibidaDAO;
import com.example.hector.DAO.PuntuacionRecibidaDAO;
import com.example.hector.DAO.SolicitudContestadaDAO;
import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.DAO.SolicitudRecibidaDAO;

import java.util.ArrayList;

import DTO.PreguntaDTO;
import DTO.PreguntaPendienteDTO;
import DTO.PuntuacionDTO;
import DTO.PuntuacionPendienteDTO;
import DTO.RespuestaPendienteDTO;
import DTO.SolicitudDTO;
import DTO.SolicitudEnviadaDTO;


public class MenuPrincipal extends AppCompatActivity implements DialogoPreguntas.ClickDialogoPreguntasListener,
        DialogoAgregar.ClickDialogoAgregarListener {


    private Button buttonEliminarContacto;
    private Button buttonGuardarPregunta,buttonModificarPregunta,buttonEliminarPregunta;
    private FloatingActionButton fabPreguntar;
    private Toolbar toolbar;
    private TextView nombreContacto;
    private LinearLayout contenedor1Puntuacion;
    private TextView textViewPuntuacionPreguntadas;
    private String contacto="";
    int numeroPreguntas=0;

    ArrayList<PreguntaDTO>listaPreguntas=new ArrayList<PreguntaDTO>();
    public static final String TAG = "GCMTestLoveApp";
    Util util=new Util();
    private Context context;
    ProgressDialog dialogoProgreso;
    FragmentManager fragmentManager;
    DialogoPreguntas dialogoPreguntas;
    DialogoAgregar dialogoAgregar;
    private long puntuacionContestadas;
    private long puntuacionPreguntadas;
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
        Log.i(TAG, "INI MenuPrincipal");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        /*INI Inicializacion de variables*/
        context = getApplicationContext();
        fabPreguntar = (FloatingActionButton) findViewById(R.id.fabPreguntar);
        nombreContacto=(TextView) findViewById(R.id.textViewnombreContacto);
        textViewPuntuacionPreguntadas=(TextView) findViewById(R.id.puntuacion);
        contenedor1Puntuacion = (LinearLayout) findViewById(R.id.Contenedor1Puntuacion);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        contacto=util.getContactoCache(context);
        numeroPreguntas=util.getCantidadPreguntaEnCache(context);
        puntuacionPreguntadas=util.getPuntuacionPreguntadasEnCache(context);
        puntuacionContestadas = util.getPuntuacionContestadasEnCache(context);

        getDataFromOtherActivity();
        /*FIN Inicializacion de variables*/

        /*INI Se carga nombre contacto*/
        nombreContacto.append(contacto);
        /*FIN Se carga nombre contacto*/
        textViewPuntuacionPreguntadas.append(""+puntuacionPreguntadas);

        dialogoProgreso = new ProgressDialog(MenuPrincipal.this);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);

        cargraInterfaz();

        /*INI Se obtiene puntaje para mostrase y cargar animacion*/
        //animacion(puntuacionPreguntadas);
        /*FIN Se obtiene puntaje para mostrase y cargar animacion*/

        solicitudEnviadaDAO.setSolEnviadaDTO((ArrayList<SolicitudDTO>) getIntent().getParcelableExtra("SOL_ENVIADA_LISTA"));




        fabPreguntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationClickOnFloatingActionbutton(fabPreguntar);

                fragmentManager = getFragmentManager();
                dialogoPreguntas = new DialogoPreguntas();
                dialogoPreguntas.setTipo(Constantes.TIPO_DIALOGO_ENVIAR_PREGUNTA);
                dialogoPreguntas.setListaPreguntas(listaPreguntas);
                dialogoPreguntas.show(fragmentManager, TAG);
            }
        });


    }

    @Override
    public void onClickPregunta(String pregunta,long numeroPregunta,short tipo,int index) {
        if(tipo == Constantes.TIPO_DIALOGO_ENVIAR_PREGUNTA){
            TareaPregunta tareaP=new TareaPregunta();
            tareaP.execute(util.getUserCache(context), pregunta,Long.toString(numeroPregunta));
        }else if(tipo == Constantes.TIPO_DIALOGO_ELIMINAR_PREGUNTA){
            TareaEliminarPregunta tarea=new TareaEliminarPregunta();
            tarea.execute(Long.toString(numeroPregunta),Integer.toString(index));
        }
    }

    @Override
    public void onClickAgregar(String texto,short tipoDialogo) {
        if(tipoDialogo == Constantes.TIPO_DIALOGO_AGREGAR_CONTACTO){

        }else if(tipoDialogo == Constantes.TIPO_DIALOGO_AGREGAR_PREGUNTA){
            TareaGuardarPregunta tarea=new TareaGuardarPregunta();
            tarea.execute(util.getUserCache(context), texto);
        }

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
                clickAgregarPreguntaOption();
                return true;
            case R.id.eliminarPregunta:
                clickEliminarPregunta();
                return true;
            case R.id.eliminarContacto:
                clickEliminarContacto();
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

    public void clickAgregarPreguntaOption(){
        fragmentManager = getFragmentManager();
        dialogoAgregar = DialogoAgregar.newInstance("Agregar pregunta");
        dialogoAgregar.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
        dialogoAgregar.setTipoDialogo(Constantes.TIPO_DIALOGO_AGREGAR_PREGUNTA);
        dialogoAgregar.show(fragmentManager,"dialogo_agregar");
    }

    public void clickCerrarSesion(){
        TareaCerrarSesion tarea=new TareaCerrarSesion();
        tarea.execute(util.getUserCache(context));
    }

    public void clickEliminarPregunta(){
        fragmentManager = getFragmentManager();
        dialogoPreguntas = new DialogoPreguntas();
        dialogoPreguntas.setTipo(Constantes.TIPO_DIALOGO_ELIMINAR_PREGUNTA);
        dialogoPreguntas.setListaPreguntas(listaPreguntas);
        dialogoPreguntas.show(fragmentManager, TAG);
    }

    public void clickPendiente(){
        Intent intent;
        intent = new Intent(MenuPrincipal.this, Pendiente.class);
        startActivity(intent);
    }

    public void clickEliminarContacto(){
        TareaEliminarContacto tarea = new TareaEliminarContacto();
        tarea.execute(util.getIdContactoCache(context),util.getContactoCache(context));
    }


    private class TareaGuardarPregunta extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            Log.i(TAG,"Usuario= "+params[0]);
            Log.i(TAG, "Pregunta= " +params[1]);
            publishProgress(10);
           //resulTarea= servicio.registrarPregunta(context,params[0],params[1]);
            publishProgress(70);

            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(MenuPrincipal.this, "Registro de pregunta exitoso!", Toast.LENGTH_SHORT).show();
                cargraInterfaz();
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(MenuPrincipal.this, "No se ha registrado la pregunta"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuPrincipal.this, "Solicitud de contacto cancelada" , Toast.LENGTH_SHORT).show();
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
                    TareaGuardarPregunta.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }


    private class TareaPregunta extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";

            publishProgress(10);
            //resulTarea= servicio.preguntar(params[0], params[1],Long.parseLong(params[2]));

            publishProgress(70);
            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(MenuPrincipal.this, "Ya se envio tu pregunta", Toast.LENGTH_SHORT).show();

            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(MenuPrincipal.this, "No se ha enviado tu pregunta"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuPrincipal.this, "Solicitud de contacto cancelada" , Toast.LENGTH_SHORT).show();
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
                    TareaPregunta.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
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
           // resulTarea= servicio.cerrarSesion(params[0]);
            publishProgress(70);

            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(MenuPrincipal.this, "Se ha cerrado la sesion!", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MenuPrincipal.this, Inicio.class);
                startActivity(intent);
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(MenuPrincipal.this, "No se ha cerrado sesion"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuPrincipal.this, "Se ha cancelado cierre de sesion" , Toast.LENGTH_SHORT).show();
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

    private class TareaEliminarPregunta extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            publishProgress(10);
           // resulTarea= servicio.eliminarPregunta(Long.parseLong(params[0]));
            listaPreguntas = util.eliminarPreguntaEnCache(context,listaPreguntas,Integer.parseInt(params[1]));

            publishProgress(70);

            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                cargraInterfaz();
                Toast.makeText(MenuPrincipal.this, "Se ha eliminado la pregunta!", Toast.LENGTH_SHORT).show();
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(MenuPrincipal.this, "No se ha eliminado la pregunta"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuPrincipal.this, "Se ha cancelado eliminacion de pregunta" , Toast.LENGTH_SHORT).show();
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
                    TareaEliminarPregunta.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }


    private class TareaEliminarContacto extends AsyncTask<String,Integer,String>
    {
        ServicioRest servicio=new ServicioRest(context);
        String resulTarea="0";
        PuntuacionDTO puntuacionDTO = new PuntuacionDTO();
        PuntuacionPendienteDTO puntuacionPendienteDTO = new PuntuacionPendienteDTO();
        PreguntaPendienteDTO preguntaPendienteDTO = new PreguntaPendienteDTO();
        RespuestaPendienteDTO respuestaPendienteDTO = new RespuestaPendienteDTO();

        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
            publishProgress(10);

            puntuacionDTO.setIdContacto(Long.parseLong(params[0]));
           // resulTarea =servicio.eliminarPuntuacionByIdContacto(puntuacionDTO);
            publishProgress(20);

            if(resulTarea.equals("1")){
                puntuacionPendienteDTO.setContacto(params[1]);
            //    resulTarea =servicio.eliminarAllPuntuacionPendienteByContacto(puntuacionPendienteDTO);
                publishProgress(30);
            }
            if(resulTarea.equals("1")){
                respuestaPendienteDTO.setContacto(params[1]);
               // resulTarea =servicio.eliminarAllRespuestaPendienteByContacto(respuestaPendienteDTO);
                publishProgress(40);
            }
            if(resulTarea.equals("1")){
                preguntaPendienteDTO.setContacto(params[1]);
               // resulTarea =servicio.eliminarAllPreguntaPendienteByContacto(preguntaPendienteDTO);
                publishProgress(50);
            }
            if(resulTarea.equals("1")) {
               // resulTarea = servicio.eliminarContacto(Long.parseLong(params[0]));
                publishProgress(60);
            }
            if(resulTarea.equals("1")) {
                util.removerCacheOnlyContacto(context);
                publishProgress(70);
            }

            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            dialogoProgreso.dismiss();
            if (this.resulTarea.equals("1")) {
                Toast.makeText(MenuPrincipal.this, "Se ha eliminado el contacto!", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MenuPrincipal.this, Contacto.class);
                startActivity(intent);
            } else {
                String error = "";
                if( !servicio.getErrorDescripcion().equals("") &&  servicio.getErrorDescripcion() != null){
                    error=" ,"+servicio.getErrorDescripcion();
                }
                Toast.makeText(MenuPrincipal.this, "No se ha eliminado el contacto"+error, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MenuPrincipal.this, "Se ha cancelado eliminacion de contacto" , Toast.LENGTH_SHORT).show();
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
                    TareaEliminarContacto.this.cancel(true);
                }
            });

            dialogoProgreso.setProgress(0);
            dialogoProgreso.show();
        }
    }

    public void actualizarCantPreguntas(){
        Log.i(TAG,"INI actualizarCantPreguntas");
        numeroPreguntas=util.getCantidadPreguntaEnCache(context);
       /* if(numeroPreguntas == 1){
            preguntas.add("Seleccione...");

        }else{
            preguntas.clear();
        }*/

        for(int i=0;i<numeroPreguntas;i++){

            PreguntaDTO pDTO=new PreguntaDTO();

            pDTO.setPregunta(util.getPreguntaCacheByNumArray(context,i+1));
            pDTO.setNumero(Long.parseLong(util.getNumeroPreguntaCacheByNumArray(context,i+1)));
            if(!pDTO.getPregunta().equals("preguntaDefaultTestLove")){
                listaPreguntas.add(pDTO);
            }

        }
        Log.i(TAG, "FIN actualizarCantPreguntas");

    }


    public void cargraInterfaz(){
        Log.i(TAG, "INI cargraInterfazInicial");
        actualizarCantPreguntas();
        if((!contacto.equals("contactoDefaultTestLove")&& !contacto.equals(""))
                && numeroPreguntas > 0 ) {
            Log.i(TAG, "Tiene preguntas guardadas,tiene contacto,cantidad=" + numeroPreguntas + " ,contacto=" + contacto);

            fabPreguntar.setVisibility(FloatingActionButton.VISIBLE);
            contenedor1Puntuacion.setVisibility(LinearLayout.VISIBLE);

        }else if((!contacto.equals("contactoDefaultTestLove")&& !contacto.equals(""))
                && numeroPreguntas == 0){
            Log.i(TAG, "No tiene preguntas,tiene contacto,cantidad=" + numeroPreguntas + " ,contacto=" + contacto);

            fabPreguntar.setVisibility(FloatingActionButton.GONE);
            contenedor1Puntuacion.setVisibility(LinearLayout.VISIBLE);


        } else if(contacto.equals("contactoDefaultTestLove")|| contacto.equals("")){
            Log.i(TAG, "No tiene contacto=" + numeroPreguntas);

            fabPreguntar.setVisibility(FloatingActionButton.GONE);
            contenedor1Puntuacion.setVisibility(LinearLayout.GONE);

        }

        Log.i(TAG, "FIN cargraInterfazInicial");
    }


    public void animacion(long puntaje){

            ImageView imgView = (ImageView)findViewById(R.id.emoticonPuntuacion);
            AnimationDrawable frame = (AnimationDrawable) setAnimationResourceByPuntaje(puntaje,imgView);
            frame.start();

    }

    public Drawable setAnimationResourceByPuntaje(long puntaje,ImageView imgView){

        imgView.setVisibility(ImageView.VISIBLE);

        /*Agregar recursos dependiendo del puntaje*/
        if(puntaje >= Constantes.ANIMACION_MENU_PRINCIPAL_PUNTUACION_MUYFELIZ){
            /*Test*/
            imgView.setBackgroundResource(R.drawable.frame_animation_test);
        }

        return imgView.getBackground();
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
