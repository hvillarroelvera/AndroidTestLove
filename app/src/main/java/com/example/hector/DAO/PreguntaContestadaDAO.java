package com.example.hector.DAO;

import android.content.Context;

import com.example.hector.sharedpreferences.CrudSharedPreferences;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Util;

import java.util.ArrayList;

import DTO.PreguntaDTO;
import DTO.SolicitudEnviadaDTO;

/**
 * Created by hectorfrancisco on 16-07-2016.
 */
public class PreguntaContestadaDAO {


    private ArrayList<PreguntaDTO> listaPreguntasContestadasDTO = new ArrayList<PreguntaDTO>();
    private Context context;
    private Util util;

    public PreguntaContestadaDAO(Context context) {
        this.util = new Util();
        this.context = context;
        /*INI Se inicializa lista de solicitudes*/
        this.listaPreguntasContestadasDTO = getListaPreguntasContestadasDTOFromCache(context);
         /*FIN Se inicializa lista de solicitudes*/
    }

    public PreguntaContestadaDAO(ArrayList<PreguntaDTO> listaPreguntasContestadasDTO,Context context) {
        this.util = new Util();
        this.listaPreguntasContestadasDTO = listaPreguntasContestadasDTO;
        this.context = context;
        /*Se agrega a cache lista recuperada de servidor*/
        addToCacheFromLista(listaPreguntasContestadasDTO);
         /*Se agrega a cache lista recuperada de servidor*/
    }

    public ArrayList<PreguntaDTO> getListaPreguntasContestadasDTO() {
        return listaPreguntasContestadasDTO;
    }

    public void setListaPreguntasContestadasDTO(ArrayList<PreguntaDTO> listaPreguntasContestadasDTO) {
        this.listaPreguntasContestadasDTO = listaPreguntasContestadasDTO;
    }



    public void add(PreguntaDTO preguntaDTO){
        int indiceUltimoAdd = 0;
        indiceUltimoAdd = addToSolEnviadaDTO(preguntaDTO);
        addToUserPreferences(indiceUltimoAdd, preguntaDTO.getPregunta());
    }

    private int addToSolEnviadaDTO(PreguntaDTO preguntaDTO){
        listaPreguntasContestadasDTO.add(preguntaDTO);
        return listaPreguntasContestadasDTO.indexOf(preguntaDTO);
    }

    private void addToUserPreferences(int indice,String pregunta){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.registrarEnListaCacheByTipoProperty(context, Constantes.PROPERTY_PREGUNTA_CONTESTADA, indice, pregunta);
    }

    public void delete(int index){
        deleteAllFromUserPreferences(context);
        deleteFromPreguntaContestadaDTOByObject(index);
        /*Se recrean con lista actualizada*/
        addToCacheFromLista(listaPreguntasContestadasDTO);
         /*Se recrean con lista actualizada*/
    }

    private void deleteFromPreguntaContestadaDTOByObject(int index){
        listaPreguntasContestadasDTO.remove(index);
    }

    private void deleteAllFromUserPreferences(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.eliminaAllByPropertyEnCache(context, listaPreguntasContestadasDTO, Constantes.PROPERTY_PREGUNTA_CONTESTADA);
    }

    public ArrayList<PreguntaDTO> getListaPreguntasContestadasDTOFromCache(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        ArrayList<PreguntaDTO>listafinalPreguntas = new ArrayList<PreguntaDTO>();
        PreguntaDTO preguntaDTO = null;
        listaGetsCache = crudSharedPreferences.getListFromCacheByProperty(context,Constantes.PROPERTY_PREGUNTA_CONTESTADA);
        for(int i = 0 ;i < listaGetsCache.size() ; i++){
            String cadenaObtenida = listaGetsCache.get(i);
            preguntaDTO = new PreguntaDTO();
            preguntaDTO.setPregunta(cadenaObtenida);
            listafinalPreguntas.add(preguntaDTO);
        }
        return listafinalPreguntas;
    }



    public void addToCacheFromLista(ArrayList<PreguntaDTO> listaPreguntasContestadasDTO){
        PreguntaDTO preguntaDTO = null;
        for(int i = 0;i<listaPreguntasContestadasDTO.size();i++){
            preguntaDTO = new PreguntaDTO();
            preguntaDTO = listaPreguntasContestadasDTO.get(i);
            addToUserPreferences(i, preguntaDTO.getPregunta());
        }
    }

    public int deleteByPregunta(String pregunta){

        ArrayList<PreguntaDTO> listaPreguntaContestadaDTOs = new ArrayList<PreguntaDTO>();
        PreguntaDTO preguntaDTO = null;
        listaPreguntaContestadaDTOs = getListaPreguntasContestadasDTOFromCache(context);
        for (int i = 0;i<listaPreguntaContestadaDTOs.size();i++){
            preguntaDTO = listaPreguntaContestadaDTOs.get(i);
            if(preguntaDTO.getPregunta().equals(pregunta)) {
                delete(i);
                return Constantes.SUCCESS;
            }
        }
        return Constantes.NOT_SUCCESS;
    }

    public void deleteAll(){
        deleteAllFromUserPreferences(context);
        listaPreguntasContestadasDTO.clear();
    }
}
