package com.example.hector.DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.ArrayList;
import DTO.SolicitudDTO;

import com.example.hector.sharedpreferences.CrudSharedPreferences;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Util;

/**
 * Created by hectorfrancisco on 09-07-2016.
 */
public class SolicitudRecibidaDAO {

    private ArrayList<SolicitudDTO> solRecibidaDTO = new ArrayList<SolicitudDTO>();
    private Context context;
    private Util util;

    public SolicitudRecibidaDAO(Context context) {
        this.util = new Util();
        this.context = context;
        /*INI Se inicializa lista de solicitudes*/
        this.solRecibidaDTO = getsolRecibidaDTOFromCache(context);
         /*FIN Se inicializa lista de solicitudes*/
    }

    public SolicitudRecibidaDAO(ArrayList<SolicitudDTO> solRecibidaDTO,Context context) {
        this.util = new Util();
        this.solRecibidaDTO = solRecibidaDTO;
        this.context = context;
    }

    public ArrayList<SolicitudDTO> getSolRecibidaDTO() {
        return solRecibidaDTO;
    }

    public void setSolRecibidaDTO(ArrayList<SolicitudDTO> solRecibidaDTO) {
        this.solRecibidaDTO = solRecibidaDTO;
    }

    public void add(SolicitudDTO solicitudRecibidaDTO){
        int indiceUltimoAdd = 0;
        indiceUltimoAdd = addTosolRecibidaDTO(solicitudRecibidaDTO);
        addToUserPreferences(indiceUltimoAdd,solicitudRecibidaDTO.getNumero(),solicitudRecibidaDTO.getUserEmisor());
    }

    private int addTosolRecibidaDTO(SolicitudDTO solicitudRecibidaDTO){
        solRecibidaDTO.add(solicitudRecibidaDTO);
        return solRecibidaDTO.indexOf(solicitudRecibidaDTO);
    }

    private void addToUserPreferences(int indice,long numero, String nombreEmisor){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.registrarEnListaCacheByTipoProperty(context, Constantes.PROPERTY_SOLICITUD_RECIBIDA, indice, Long.toString(numero), nombreEmisor);
    }

    public void delete(int index){
        deleteAllFromUserPreferences(context);
        deleteFromsolRecibidaDTOByObject(index);
        /*Se recrean con lista actualizada*/
        addToCacheFromLista(solRecibidaDTO);
         /*Se recrean con lista actualizada*/
    }

    public void deleteAll(){
        deleteAllFromUserPreferences(context);
        solRecibidaDTO.clear();
    }
    private void deleteFromsolRecibidaDTOByObject(int index){
        solRecibidaDTO.remove(index);
    }

    private void deleteAllFromUserPreferences(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.eliminaAllByPropertyEnCache(context, solRecibidaDTO, Constantes.PROPERTY_SOLICITUD_RECIBIDA);
    }

    public ArrayList<SolicitudDTO> getsolRecibidaDTOFromCache(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        ArrayList<SolicitudDTO>listafinalSolicitudes = new ArrayList<SolicitudDTO>();
        SolicitudDTO solicitudRecibidaDTO = null;
        listaGetsCache = crudSharedPreferences.getListFromCacheByProperty(context,Constantes.PROPERTY_SOLICITUD_RECIBIDA);
        for(int i = 0 ;i < listaGetsCache.size() ; i++){
            String cadenaObtenida = listaGetsCache.get(i);
            solicitudRecibidaDTO = new SolicitudDTO();
            solicitudRecibidaDTO = getSolicitudRecibidaDTOFromString(cadenaObtenida);
            listafinalSolicitudes.add(solicitudRecibidaDTO);
        }
        return listafinalSolicitudes;
    }

    private SolicitudDTO getSolicitudRecibidaDTOFromString(String temp){
        SolicitudDTO solicitudRecibidaDTO = new SolicitudDTO();
        String numero = "";
        String emisor = "";
        int index = 0;
        index = temp.indexOf("_");
        numero = temp.substring(0, index);
        emisor = temp.substring(index+1,temp.length());
        solicitudRecibidaDTO.setNumero(Integer.parseInt(numero));
        solicitudRecibidaDTO.setUserEmisor(emisor);
        return solicitudRecibidaDTO;
    }

    public void addToCacheFromLista(ArrayList<SolicitudDTO> solRecibidaDTO){
        SolicitudDTO solicitudRecibidaDTO = null;
        for(int i = 0;i<solRecibidaDTO.size();i++){
            solicitudRecibidaDTO = new SolicitudDTO();
            solicitudRecibidaDTO = solRecibidaDTO.get(i);
            addToUserPreferences(i,solicitudRecibidaDTO.getNumero(),solicitudRecibidaDTO.getUserEmisor());
        }
    }
}
