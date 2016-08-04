package com.example.hector.frangments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hector.adaptadores.AdaptadorSolicitudesPendientes;
import com.example.hector.adaptadores.RecyclerViewClickListener;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.R;

import java.util.ArrayList;

import DTO.SolicitudDTO;

/**
 * Created by hvillarroel on 21-07-2016.
 */
public class SolicitudContestadaFragment extends Fragment implements RecyclerViewClickListener {

    private RecyclerView recView;
    private ArrayList<SolicitudDTO> datos = new ArrayList<SolicitudDTO>();

    public SolicitudContestadaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_solicitud_contestada, container, false);
        setData();
        //Inicialización RecyclerView
        recView = (RecyclerView) rootView.findViewById(R.id.RecViewSolContestadaPendiente);
        recView.setHasFixedSize(true);

        final AdaptadorSolicitudesPendientes adaptador = new AdaptadorSolicitudesPendientes(datos,this);

        recView.setAdapter(adaptador);
        recView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return rootView;
    }

    public void setData(){
        Bundle bundle  = this.getArguments();
        if(bundle != null){
            datos = bundle.getParcelableArrayList(Constantes.PARCEL_LISTA_SOL_CONTESTADA);
        }

    }



    @Override
    public void recyclerViewListClicked(View v, int position, String itemId) {

    }
}
