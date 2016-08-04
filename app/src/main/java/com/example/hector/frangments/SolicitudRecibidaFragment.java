package com.example.hector.frangments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hector.adaptadores.AdaptadorSolicitudesPendientes;
import com.example.hector.adaptadores.AdaptadorSolicitudesRecibidas;
import com.example.hector.adaptadores.RecyclerViewClickListener;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.R;
import com.example.hector.frangments.dummy.DummyContent;

import java.util.ArrayList;

import DTO.SolicitudDTO;

public class SolicitudRecibidaFragment extends Fragment implements RecyclerViewClickListener {

    private RecyclerView recView;
    private ArrayList<SolicitudDTO> datos = new ArrayList<SolicitudDTO>();
    private AdaptadorSolicitudesRecibidas adaptadorSolicitudesRecibidas;

    public SolicitudRecibidaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_solicitud_recibida, container, false);
        setData();
        //Inicialización RecyclerView
        recView = (RecyclerView) rootView.findViewById(R.id.RecViewSolRecibidaPendiente);
        recView.setHasFixedSize(true);

        adaptadorSolicitudesRecibidas = new AdaptadorSolicitudesRecibidas(datos,this);

        recView.setAdapter(adaptadorSolicitudesRecibidas);
        recView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return rootView;
    }

    public void setData(){
        Bundle bundle  = this.getArguments();
        if(bundle != null){
            datos = bundle.getParcelableArrayList(Constantes.PARCEL_LISTA_SOL_RECIBIDA);
        }

    }

    @Override
    public void recyclerViewListClicked(View v, int position,String itemId) {
        Toast.makeText(getActivity(), "Position clickeada " + position, Toast.LENGTH_SHORT).show();
    }
}
