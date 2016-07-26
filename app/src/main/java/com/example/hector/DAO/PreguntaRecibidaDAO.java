package com.example.hector.DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import DTO.PreguntaDTO;

import com.example.hector.sharedpreferences.CrudSharedPreferences;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Util;

/**
 * Created by hectorfrancisco on 09-07-2016.
 */
public class PreguntaRecibidaDAO {

    private ArrayList<PreguntaDTO> listaPreguntaRecibidaDTO = new ArrayList<PreguntaDTO>();
    private Context context;
    private Util util;

    public PreguntaRecibidaDAO(Context context) {
        this.util = new Util();
        this.context = context;
        /*INI Se inicializa lista de solicitudes*/
        this.listaPreguntaRecibidaDTO = getlistaPreguntaRecibidaDTOFromCache(context);
         /*FIN Se inicializa lista de solicitudes*/
    }

    public PreguntaRecibidaDAO(ArrayList<PreguntaDTO> listaPreguntaRecibidaDTO,Context context) {
        this.util = new Util();
        this.listaPreguntaRecibidaDTO = listaPreguntaRecibidaDTO;
        this.context = context;
    }

    public ArrayList<PreguntaDTO> getListaPreguntaRecibidaDTO() {
        return listaPreguntaRecibidaDTO;
    }

    public void setListaPreguntaRecibidaDTO(ArrayList<PreguntaDTO> listaPreguntaRecibidaDTO) {
        this.listaPreguntaRecibidaDTO = listaPreguntaRecibidaDTO;
    }

    public void add(PreguntaDTO preguntaDTO){
        int indiceUltimoAdd = 0;
        indiceUltimoAdd = addTolistaPreguntaRecibidaDTO(preguntaDTO);
        addToUserPreferences(indiceUltimoAdd,preguntaDTO.getNumero(),preguntaDTO.getPregunta());
    }

    public void deleteAll(){
        deleteAllFromUserPreferences(context);
        listaPreguntaRecibidaDTO.clear();
    }

    private int addTolistaPreguntaRecibidaDTO(PreguntaDTO preguntaDTO){
        listaPreguntaRecibidaDTO.add(preguntaDTO);
        return listaPreguntaRecibidaDTO.indexOf(preguntaDTO);
    }

    private void addToUserPreferences(int indice,long numero, String pregunta){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.registrarEnListaCacheByTipoProperty(context, Constantes.PROPERTY_PREGUNTA_RECIBIDA, indice, Long.toString(numero), pregunta);
    }

    public void delete(int index){
        deleteAllFromUserPreferences(context);
        deleteFromlistaPreguntaRecibidaDTOByObject(index);
        /*Se recrean con lista actualizada*/
        addToCacheFromLista(listaPreguntaRecibidaDTO);
         /*Se recrean con lista actualizada*/
    }

    private void deleteFromlistaPreguntaRecibidaDTOByObject(int index){
        listaPreguntaRecibidaDTO.remove(index);
    }

    private void deleteAllFromUserPreferences(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.eliminaAllByPropertyEnCache(context, listaPreguntaRecibidaDTO, Constantes.PROPERTY_PREGUNTA_RECIBIDA);
    }

    public ArrayList<PreguntaDTO> getlistaPreguntaRecibidaDTOFromCache(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        ArrayList<PreguntaDTO>listafinalPreguntas = new ArrayList<PreguntaDTO>();
        PreguntaDTO preguntaDTO = null;
        listaGetsCache = crudSharedPreferences.getListFromCacheByProperty(context,Constantes.PROPERTY_PREGUNTA_RECIBIDA);
        for(int i = 0 ;i < listaGetsCache.size() ; i++){
            String cadenaObtenida = listaGetsCache.get(i);
            preguntaDTO = new PreguntaDTO();
            preguntaDTO = getPreguntaDTOFromCache(cadenaObtenida);
            listafinalPreguntas.add(preguntaDTO);
        }
        return listafinalPreguntas;
    }

    private PreguntaDTO getPreguntaDTOFromCache(String temp){
        PreguntaDTO preguntaDTO = new PreguntaDTO();
        String numeroPregunta = "";
        String pregunta = "";
        int index = 0;
        index = temp.indexOf("_");
        numeroPregunta = temp.substring(0, index);
        pregunta = temp.substring(index+1,temp.length());
        preguntaDTO.setNumero(Long.parseLong(numeroPregunta));
        preguntaDTO.setPregunta(pregunta);
        return preguntaDTO;
    }

    public void addToCacheFromLista(ArrayList<PreguntaDTO> listaPreguntaRecibidaDTO){
        PreguntaDTO preguntaDTO = null;
        for(int i = 0;i<listaPreguntaRecibidaDTO.size();i++){
            preguntaDTO = new PreguntaDTO();
            preguntaDTO = listaPreguntaRecibidaDTO.get(i);
            addToUserPreferences(i,preguntaDTO.getNumero(),preguntaDTO.getPregunta());
        }
    }
}