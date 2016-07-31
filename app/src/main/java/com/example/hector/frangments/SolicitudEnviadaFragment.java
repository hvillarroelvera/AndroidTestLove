package com.example.hector.frangments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hector.DAO.SolicitudEnviadaDAO;
import com.example.hector.adaptadores.AdaptadorSolicitudesPendientes;
import com.example.hector.adaptadores.RecyclerViewClickListener;
import com.example.hector.testloveapp.Constantes;
import com.example.hector.testloveapp.DialogoAgregar;
import com.example.hector.testloveapp.R;

import java.util.ArrayList;

import DTO.SolicitudDTO;

public class SolicitudEnviadaFragment extends Fragment implements RecyclerViewClickListener{

    private RecyclerView recView;
    private ArrayList<SolicitudDTO> datos = new ArrayList<SolicitudDTO>();
    private FloatingActionButton fabAgregarContacto;
    private FragmentManager fragmentManager;
    private DialogoAgregar dialogoAgregar;
    OnSolicitudEnviadaListener mCallback;
    AdaptadorSolicitudesPendientes adaptador;

    public SolicitudEnviadaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_solicitud_enviada, container, false);
        fabAgregarContacto=(FloatingActionButton) rootView.findViewById(R.id.fabAgregarContacto);
        setData();
        //Inicialización RecyclerView
        recView = (RecyclerView) rootView.findViewById(R.id.RecViewSolEnviadaPendiente);
        recView.setHasFixedSize(true);

        adaptador = new AdaptadorSolicitudesPendientes(datos,this);

        recView.setAdapter(adaptador);
        recView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        fabAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mCallback.onClickAgregarContacto();
                fragmentManager = getActivity().getFragmentManager();
                dialogoAgregar = DialogoAgregar.newInstance("Agregar contacto");
                dialogoAgregar.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
                dialogoAgregar.setTipoDialogo(Constantes.TIPO_DIALOGO_AGREGAR_CONTACTO);
                dialogoAgregar.show(fragmentManager, "dialogo_agregar");
            }
        });

        return rootView;
    }

    public AdaptadorSolicitudesPendientes getAdaptador() {
        return adaptador;
    }

    public ArrayList<SolicitudDTO> getDatos() {
        return datos;
    }

    public void setData(){
        Bundle bundle  = this.getArguments();
        if(bundle != null){
            datos = bundle.getParcelableArrayList(Constantes.PARCEL_LISTA_SOL_ENVIADA);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSolicitudEnviadaListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSolicitudEnviadaListener");
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Toast.makeText(getActivity(), "Position clickeada "+position, Toast.LENGTH_SHORT).show();

    }


    public interface OnSolicitudEnviadaListener{
        public void onClickAgregarContacto();
    }

}
