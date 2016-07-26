package com.example.hector.testloveapp;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.hector.DAO.PreguntaContestadaDAO;
import com.example.hector.DAO.PreguntaEnviadaDAO;
import com.example.hector.DAO.PreguntaRecibidaDAO;
import com.example.hector.DAO.PuntuacionRecibidaDAO;
import com.example.hector.DAO.SolicitudContestadaDAO;
import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.DAO.SolicitudRecibidaDAO;
import com.example.hector.adaptadores.AdaptadorSolicitudesPendientes;
import com.example.hector.asynctasks.TareaCerrarSesion;
import com.example.hector.asynctasks.TareaEnviarSolicitudContacto;
import com.example.hector.frangments.PreguntaContestadaFragment;
import com.example.hector.frangments.PreguntaEnviadaFragment;
import com.example.hector.frangments.PreguntaRecibidaFragment;
import com.example.hector.frangments.PuntuacionRecibidaFragment;
import com.example.hector.frangments.SolicitudContestadaFragment;
import com.example.hector.frangments.SolicitudEnviadaFragment;
import com.example.hector.frangments.SolicitudRecibidaFragment;

import java.util.ArrayList;

import DTO.PreguntaDTO;
import DTO.PuntuacionDTO;
import DTO.SolicitudDTO;

public class Pendiente extends AppCompatActivity implements SolicitudEnviadaFragment.OnSolicitudEnviadaListener,DialogoAgregar.ClickDialogoAgregarListener,
        TareaEnviarSolicitudContacto.OnUpdateUI{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Context context;
    ProgressDialog dialogoProgreso;
    Util util = new Util();
    private SolicitudEnviadaDAO solicitudEnviadaDAO;
    private SolicitudRecibidaDAO solicitudRecibidaDAO;
    private SolicitudContestadaDAO solicitudContestadaDAO;
    private PreguntaEnviadaDAO preguntaEnviadaDAO;
    private PreguntaRecibidaDAO preguntaRecibidaDAO;
    private PreguntaContestadaDAO preguntaContestadaDAO;
    private PuntuacionRecibidaDAO puntuacionRecibidaDAO;
    private Intent intent;
    private Bundle bundle;
    FragmentManager fragmentManager;
    DialogoAgregar dialogoAgregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pendiente_drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layoutPendiente);
        navigationView = (NavigationView)findViewById(R.id.navview);

        context = getApplicationContext();
        intent = getIntent();
        getDataFromOtherActivity();

        /*INI primer fragment*/
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        fragment = new SolicitudEnviadaFragment();
        bundle = new Bundle();
        bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_SOL_ENVIADA,solicitudEnviadaDAO.getSolEnviadaDTO());
        fragment.setArguments(bundle);
        fragmentTransaction = true;
        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.SolicitudesEnviadasMenuNavView:
                                fragment = new SolicitudEnviadaFragment();
                                bundle = new Bundle();
                                bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_SOL_ENVIADA,solicitudEnviadaDAO.getSolEnviadaDTO());
                                fragment.setArguments(bundle);
                                        fragmentTransaction = true;
                                break;
                            case R.id.SolicitudesRecibidasMenuNavView:
                                fragment = new SolicitudRecibidaFragment();
                                bundle = new Bundle();
                                bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_SOL_RECIBIDA,solicitudRecibidaDAO.getSolRecibidaDTO());
                                fragment.setArguments(bundle);
                                fragmentTransaction = true;
                                break;
                            case R.id.SolicitudesContestadasMenuNavView:
                                fragment = new SolicitudContestadaFragment();
                                bundle = new Bundle();
                                bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_SOL_CONTESTADA,solicitudContestadaDAO.getSolContestadaDTO());
                                fragment.setArguments(bundle);
                                fragmentTransaction = true;
                                break;
                            case R.id.PreguntasEnviadasMenuNavView:
                                fragment = new PreguntaEnviadaFragment();
                                bundle = new Bundle();
                                bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_PREG_ENVIADA,preguntaEnviadaDAO.getListaPreguntaEnviadaDTO());
                                fragment.setArguments(bundle);
                                fragmentTransaction = true;
                                break;
                            case R.id.PreguntasRecibidasMenuNavView:
                                fragment = new PreguntaRecibidaFragment();
                                bundle = new Bundle();
                                bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_PREG_RECIBIDA,preguntaRecibidaDAO.getListaPreguntaRecibidaDTO());
                                fragment.setArguments(bundle);
                                fragmentTransaction = true;
                                break;
                            case R.id.PreguntasContestadasMenuNavView:
                                fragment = new PreguntaContestadaFragment();
                                bundle = new Bundle();
                                bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_PREG_CONTESTADA,preguntaContestadaDAO.getListaPreguntasContestadasDTO());
                                fragment.setArguments(bundle);
                                fragmentTransaction = true;
                                break;
                            case R.id.PuntuacionRecibidaMenuNavView:
                                fragment = new PuntuacionRecibidaFragment();
                                bundle = new Bundle();
                                bundle.putParcelableArrayList(Constantes.PARCEL_LISTA_PUNT_RECIBIDA,puntuacionRecibidaDAO.getListaPuntuacionRecibidasDTO());
                                fragment.setArguments(bundle);
                                fragmentTransaction = true;
                                break;
                        }

                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
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
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.pendientes:
                clickPendiente();
                return true;
            case R.id.agregarPregunta:
                Toast.makeText(Pendiente.this, "Debes enviar una solicitud de contacto antes de Agregar Pregunta", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.eliminarPregunta:
                Toast.makeText(Pendiente.this, "Debes enviar una solicitud de contacto antes de Eliminar Pregunta", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.eliminarContacto:
                Toast.makeText(Pendiente.this, "Debes enviar una solicitud de contacto antes de Eliminar Pregunta", Toast.LENGTH_SHORT).show();
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
        asyncTaskTareaCerrarSesion();
    }

    public void clickPendiente(){
        Intent intent;
        intent = new Intent(Pendiente.this, Pendiente.class);
        startActivity(intent);
    }

    @Override
    public void onClickAgregarContacto() {


    }

    @Override
    public void onClickAgregar(String texto, short tipoDialogo) {
        if(tipoDialogo == Constantes.TIPO_DIALOGO_AGREGAR_CONTACTO) {
            asyncTaskEnviarSolicitudContacto(texto);
        }
    }

    @Override
    public void updateUI() {
        Toast.makeText(Pendiente.this, "updateUITest", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
       /* intent = new Intent(Pendiente.this, Contacto.class);
        setToOtherActivity();
        startActivity(intent);*/
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

    public void asyncTaskEnviarSolicitudContacto(String texto){
        new TareaEnviarSolicitudContacto(this,solicitudEnviadaDAO).execute(util.getUserCache(context), texto);
    }

    public void asyncTaskTareaCerrarSesion(){
        new TareaCerrarSesion(this).execute(util.getUserCache(context));
    }
}
