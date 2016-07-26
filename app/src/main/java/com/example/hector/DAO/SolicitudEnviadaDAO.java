package com.example.hector.DAO;

import android.content.Context;

import java.util.ArrayList;

import DTO.SolicitudDTO;
import DTO.SolicitudEnviadaDTO;

import com.example.hector.sharedpreferences.CrudSharedPreferences;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Util;

/**
 * Created by hectorfrancisco on 09-07-2016.
 */
public class SolicitudEnviadaDAO {

    private ArrayList<SolicitudDTO> solEnviadaDTO = new ArrayList<SolicitudDTO>();
    private Context context;
    private Util util;

    public SolicitudEnviadaDAO(Context context) {
        this.util = new Util();
        this.context = context;
        /*INI Se inicializa lista de solicitudes*/
        this.solEnviadaDTO = getSolEnviadaDTOFromCache(context);
         /*FIN Se inicializa lista de solicitudes*/
    }

    public ArrayList<SolicitudDTO> getSolEnviadaDTO() {
        return solEnviadaDTO;
    }

    public void setSolEnviadaDTO(ArrayList<SolicitudDTO> solEnviadaDTO) {
        this.solEnviadaDTO = solEnviadaDTO;
    }

    public SolicitudEnviadaDAO(ArrayList<SolicitudDTO> solEnviadaDTO,Context context) {
        this.util = new Util();
        this.solEnviadaDTO = solEnviadaDTO;
        this.context = context;
        /*Se agrega a cache lista recuperada de servidor*/
        addToCacheFromLista(solEnviadaDTO);
         /*Se agrega a cache lista recuperada de servidor*/
    }

    public void add(SolicitudDTO solicitudDTO){
        int indiceUltimoAdd = 0;
        indiceUltimoAdd = addToSolEnviadaDTO(solicitudDTO);
        addToUserPreferences(indiceUltimoAdd, solicitudDTO.getNumero(), solicitudDTO.getUserReceptor());
    }

    private int addToSolEnviadaDTO(SolicitudDTO solicitudDTO){
        solEnviadaDTO.add(solicitudDTO);
        return solEnviadaDTO.indexOf(solicitudDTO);
    }

    private void addToUserPreferences(int indice,long numero, String userReceptor){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.registrarEnListaCacheByTipoProperty(context, Constantes.PROPERTY_SOLICITUD_ENVIADA, indice, Long.toString(numero), userReceptor);
    }

    public void delete(int index){
        deleteAllFromUserPreferences(context);
        deleteFromSolEnviadaDTOByObject(index);
        /*Se recrean con lista actualizada*/
        addToCacheFromLista(solEnviadaDTO);
         /*Se recrean con lista actualizada*/
    }

    public void deleteAll(){
        deleteAllFromUserPreferences(context);
        solEnviadaDTO.clear();
    }

    private void deleteFromSolEnviadaDTOByObject(int index){
        solEnviadaDTO.remove(index);
    }

    private void deleteAllFromUserPreferences(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.eliminaAllByPropertyEnCache(context, solEnviadaDTO, Constantes.PROPERTY_SOLICITUD_ENVIADA);
    }

    public ArrayList<SolicitudDTO> getSolEnviadaDTOFromCache(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        ArrayList<SolicitudDTO>listafinalSolicitudes = new ArrayList<SolicitudDTO>();
        SolicitudDTO solicitudDTO = null;
        listaGetsCache = crudSharedPreferences.getListFromCacheByProperty(context,Constantes.PROPERTY_SOLICITUD_ENVIADA);
        for(int i = 0 ;i < listaGetsCache.size() ; i++){
            String cadenaObtenida = listaGetsCache.get(i);
            solicitudDTO = new SolicitudDTO();
            solicitudDTO = getSolicitudDTOFromString(cadenaObtenida);
            listafinalSolicitudes.add(solicitudDTO);
        }
        return listafinalSolicitudes;
    }

    private SolicitudDTO getSolicitudDTOFromString(String temp){
        SolicitudDTO solicitudDTO = new SolicitudDTO();
        String numero = "";
        String contacto = "";
        int index = 0;
        index = temp.indexOf("_");
        numero = temp.substring(0, index);
        contacto = temp.substring(index+1,temp.length());
        solicitudDTO.setNumero(Integer.parseInt(numero));
        solicitudDTO.setUserReceptor(contacto);
        return solicitudDTO;
    }

    public void addToCacheFromLista(ArrayList<SolicitudDTO> solEnviadaDTO){
        SolicitudDTO solicitudDTO = null;
        for(int i = 0;i<solEnviadaDTO.size();i++){
            solicitudDTO = new SolicitudDTO();
            solicitudDTO = solEnviadaDTO.get(i);
            addToUserPreferences(i, solicitudDTO.getNumero(), solicitudDTO.getUserReceptor());
        }
    }

    public int deleteByNombreContacto(String nombreContacto){

        ArrayList<SolicitudDTO> solicitudDTOs = new ArrayList<SolicitudDTO>();
        SolicitudDTO solicitudDTO = null;
        solicitudDTOs = getSolEnviadaDTOFromCache(context);
        for (int i = 0;i<solicitudDTOs.size();i++){
            solicitudDTO = new SolicitudDTO();
            solicitudDTO = solicitudDTOs.get(i);
            if(solicitudDTO.getUserReceptor().equals(nombreContacto)){
                delete(i);
                return Constantes.SUCCESS;
            }
        }
        return Constantes.NOT_SUCCESS;
    }
}
