package com.example.hector.DAO;

import android.content.Context;

import com.example.hector.sharedpreferences.CrudSharedPreferences;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Util;

import java.util.ArrayList;

import DTO.PreguntaDTO;

/**
 * Created by hectorfrancisco on 17-07-2016.
 */
public class PreguntaEnviadaDAO {

    private ArrayList<PreguntaDTO> listaPreguntaEnviadaDTO = new ArrayList<PreguntaDTO>();
    private Context context;
    private Util util;

    public PreguntaEnviadaDAO(Context context) {
        this.util = new Util();
        this.context = context;
        /*INI Se inicializa lista de solicitudes*/
        this.listaPreguntaEnviadaDTO = getlistaPreguntaEnviadaDTOFromCache(context);
         /*FIN Se inicializa lista de solicitudes*/
    }

    public PreguntaEnviadaDAO(ArrayList<PreguntaDTO> listaPreguntaEnviadaDTO,Context context) {
        this.util = new Util();
        this.listaPreguntaEnviadaDTO = listaPreguntaEnviadaDTO;
        this.context = context;
    }

    public ArrayList<PreguntaDTO> getListaPreguntaEnviadaDTO() {
        return listaPreguntaEnviadaDTO;
    }

    public void setListaPreguntaEnviadaDTO(ArrayList<PreguntaDTO> listaPreguntaEnviadaDTO) {
        this.listaPreguntaEnviadaDTO = listaPreguntaEnviadaDTO;
    }


    public void deleteAll(){
        deleteAllFromUserPreferences(context);
        listaPreguntaEnviadaDTO.clear();
    }

    public void add(PreguntaDTO preguntaDTO){
        int indiceUltimoAdd = 0;
        indiceUltimoAdd = addTolistaPreguntaEnviadaDTO(preguntaDTO);
        addToUserPreferences(indiceUltimoAdd,preguntaDTO.getNumero(),preguntaDTO.getPregunta());
    }

    private int addTolistaPreguntaEnviadaDTO(PreguntaDTO preguntaDTO){
        listaPreguntaEnviadaDTO.add(preguntaDTO);
        return listaPreguntaEnviadaDTO.indexOf(preguntaDTO);
    }

    private void addToUserPreferences(int indice,long numero, String pregunta){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.registrarEnListaCacheByTipoProperty(context, Constantes.PROPERTY_PREGUNTA_ENVIADA, indice, Long.toString(numero), pregunta);
    }

    public void delete(int index){
        deleteAllFromUserPreferences(context);
        deleteFromlistaPreguntaEnviadaDTOByObject(index);
        /*Se recrean con lista actualizada*/
        addToCacheFromLista(listaPreguntaEnviadaDTO);
         /*Se recrean con lista actualizada*/
    }

    private void deleteFromlistaPreguntaEnviadaDTOByObject(int index){
        listaPreguntaEnviadaDTO.remove(index);
    }

    private void deleteAllFromUserPreferences(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.eliminaAllByPropertyEnCache(context, listaPreguntaEnviadaDTO, Constantes.PROPERTY_PREGUNTA_ENVIADA);
    }

    public ArrayList<PreguntaDTO> getlistaPreguntaEnviadaDTOFromCache(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        ArrayList<PreguntaDTO>listafinalPreguntas = new ArrayList<PreguntaDTO>();
        PreguntaDTO preguntaDTO = null;
        listaGetsCache = crudSharedPreferences.getListFromCacheByProperty(context,Constantes.PROPERTY_PREGUNTA_ENVIADA);
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

    public void addToCacheFromLista(ArrayList<PreguntaDTO> listaPreguntaEnviadaDTO){
        PreguntaDTO preguntaDTO = null;
        for(int i = 0;i<listaPreguntaEnviadaDTO.size();i++){
            preguntaDTO = new PreguntaDTO();
            preguntaDTO = listaPreguntaEnviadaDTO.get(i);
            addToUserPreferences(i,preguntaDTO.getNumero(),preguntaDTO.getPregunta());
        }
    }
}
