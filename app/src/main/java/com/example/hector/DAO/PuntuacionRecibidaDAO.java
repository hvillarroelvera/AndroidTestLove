package com.example.hector.DAO;

import android.content.Context;

import com.example.hector.sharedpreferences.CrudSharedPreferences;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Util;

import java.util.ArrayList;

import DTO.PreguntaDTO;
import DTO.PuntuacionDTO;

/**
 * Created by hectorfrancisco on 16-07-2016.
 */
public class PuntuacionRecibidaDAO {

    private ArrayList<PuntuacionDTO> listaPuntuacionRecibidasDTO = new ArrayList<PuntuacionDTO>();
    private Context context;
    private Util util;

    public PuntuacionRecibidaDAO(Context context) {
        this.util = new Util();
        this.context = context;
        /*INI Se inicializa lista de solicitudes*/
        this.listaPuntuacionRecibidasDTO = getListaPuntuacionRecibidaDTOFromCache(context);
         /*FIN Se inicializa lista de solicitudes*/
    }

    public PuntuacionRecibidaDAO(ArrayList<PuntuacionDTO> listaPuntuacionRecibidasDTO, Context context) {
        this.util = new Util();
        this.listaPuntuacionRecibidasDTO = listaPuntuacionRecibidasDTO;
        this.context = context;
        /*Se agrega a cache lista recuperada de servidor*/
        addToCacheFromLista(listaPuntuacionRecibidasDTO);
         /*Se agrega a cache lista recuperada de servidor*/
    }

    public ArrayList<PuntuacionDTO> getListaPuntuacionRecibidasDTO() {
        return listaPuntuacionRecibidasDTO;
    }

    public void setListaPuntuacionRecibidasDTO(ArrayList<PuntuacionDTO> listaPuntuacionRecibidasDTO) {
        this.listaPuntuacionRecibidasDTO = listaPuntuacionRecibidasDTO;
    }

    public void add(PuntuacionDTO puntuacionDTO){
        int indiceUltimoAdd = 0;
        indiceUltimoAdd = addToSolEnviadaDTO(puntuacionDTO);
        addToUserPreferences(indiceUltimoAdd, puntuacionDTO.getPuntos());
    }

    public void deleteAll(){
        deleteAllFromUserPreferences(context);
        listaPuntuacionRecibidasDTO.clear();
    }

    private int addToSolEnviadaDTO(PuntuacionDTO puntuacionDTO){
        listaPuntuacionRecibidasDTO.add(puntuacionDTO);
        return listaPuntuacionRecibidasDTO.indexOf(puntuacionDTO);
    }

    private void addToUserPreferences(int indice,int puntuacion){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.registrarEnListaCacheByTipoProperty(context, Constantes.PROPERTY_PUNTUACION_RECIBIDA, indice, Integer.toString(puntuacion));
    }

    public void delete(int index){
        deleteAllFromUserPreferences(context);
        deleteFromListaPuntuacionRecibidasDTOByObject(index);
        /*Se recrean con lista actualizada*/
        addToCacheFromLista(listaPuntuacionRecibidasDTO);
         /*Se recrean con lista actualizada*/
    }

    private void deleteFromListaPuntuacionRecibidasDTOByObject(int index){
        listaPuntuacionRecibidasDTO.remove(index);
    }

    private void deleteAllFromUserPreferences(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.eliminaAllByPropertyEnCache(context, listaPuntuacionRecibidasDTO, Constantes.PROPERTY_PUNTUACION_RECIBIDA);
    }

    public ArrayList<PuntuacionDTO> getListaPuntuacionRecibidaDTOFromCache(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        ArrayList<PuntuacionDTO>listafinalPuntuacion = new ArrayList<PuntuacionDTO>();
        PuntuacionDTO puntuacionDTO = null;
        listaGetsCache = crudSharedPreferences.getListFromCacheByProperty(context,Constantes.PROPERTY_PUNTUACION_RECIBIDA);
        for(int i = 0 ;i < listaGetsCache.size() ; i++){
            String cadenaObtenida = listaGetsCache.get(i);
            puntuacionDTO = new PuntuacionDTO();
            puntuacionDTO.setPuntos(Integer.parseInt(cadenaObtenida));
            listafinalPuntuacion.add(puntuacionDTO);
        }
        return listafinalPuntuacion;
    }



    public void addToCacheFromLista(ArrayList<PuntuacionDTO> listaPuntuacionRecibidasDTO){
        PuntuacionDTO puntuacionDTO = null;
        for(int i = 0;i<listaPuntuacionRecibidasDTO.size();i++){
            puntuacionDTO = new PuntuacionDTO();
            puntuacionDTO = listaPuntuacionRecibidasDTO.get(i);
            addToUserPreferences(i,puntuacionDTO.getPuntos());
        }
    }


}
