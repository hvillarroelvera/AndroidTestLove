package com.example.hector.DAO;

import android.content.Context;

import com.example.hector.sharedpreferences.CrudSharedPreferences;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.Util;

import java.util.ArrayList;

import DTO.SolicitudDTO;

/**
 * Created by hectorfrancisco on 17-07-2016.
 */
public class SolicitudContestadaDAO {

    private ArrayList<SolicitudDTO> solContestadaDTO = new ArrayList<SolicitudDTO>();
    private Context context;
    private Util util;

    public SolicitudContestadaDAO(Context context) {
        this.util = new Util();
        this.context = context;
        /*INI Se inicializa lista de solicitudes*/
        this.solContestadaDTO = getSolContestadaDTOFromCache(context);
         /*FIN Se inicializa lista de solicitudes*/
    }

    public ArrayList<SolicitudDTO> getSolContestadaDTO() {
        return solContestadaDTO;
    }

    public void setSolContestadaDTO(ArrayList<SolicitudDTO> solContestadaDTO) {
        this.solContestadaDTO = solContestadaDTO;
    }

    public SolicitudContestadaDAO(ArrayList<SolicitudDTO> solContestadaDTO,Context context) {
        this.util = new Util();
        this.solContestadaDTO = solContestadaDTO;
        this.context = context;
        /*Se agrega a cache lista recuperada de servidor*/
        addToCacheFromLista(solContestadaDTO);
         /*Se agrega a cache lista recuperada de servidor*/
    }

    public void add(SolicitudDTO solicitudDTO){
        int indiceUltimoAdd = 0;
        indiceUltimoAdd = addToSolContestadaDTO(solicitudDTO);
        addToUserPreferences(indiceUltimoAdd, solicitudDTO.getNumero(), solicitudDTO.getUserReceptor());
    }

    private int addToSolContestadaDTO(SolicitudDTO solicitudDTO){
        solContestadaDTO.add(solicitudDTO);
        return solContestadaDTO.indexOf(solicitudDTO);
    }

    private void addToUserPreferences(int indice,long numero, String userReceptor){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.registrarEnListaCacheByTipoProperty(context, Constantes.PROPERTY_PREGUNTA_CONTESTADA, indice, Long.toString(numero), userReceptor);
    }

    public void delete(int index){
        deleteAllFromUserPreferences(context);
        deleteFromSolContestadaDTOByObject(index);
        /*Se recrean con lista actualizada*/
        addToCacheFromLista(solContestadaDTO);
         /*Se recrean con lista actualizada*/
    }

    private void deleteFromSolContestadaDTOByObject(int index){
        solContestadaDTO.remove(index);
    }

    private void deleteAllFromUserPreferences(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        crudSharedPreferences.eliminaAllByPropertyEnCache(context, solContestadaDTO, Constantes.PROPERTY_PREGUNTA_CONTESTADA);
    }

    public void deleteAll(){
        deleteAllFromUserPreferences(context);
        solContestadaDTO.clear();
    }

    public ArrayList<SolicitudDTO> getSolContestadaDTOFromCache(Context context){
        CrudSharedPreferences crudSharedPreferences = new CrudSharedPreferences();
        ArrayList<String>listaGetsCache = new ArrayList<String>();
        ArrayList<SolicitudDTO>listafinalSolicitudes = new ArrayList<SolicitudDTO>();
        SolicitudDTO solicitudDTO = null;
        listaGetsCache = crudSharedPreferences.getListFromCacheByProperty(context,Constantes.PROPERTY_PREGUNTA_CONTESTADA);
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
        solicitudDTOs = getSolContestadaDTOFromCache(context);
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
