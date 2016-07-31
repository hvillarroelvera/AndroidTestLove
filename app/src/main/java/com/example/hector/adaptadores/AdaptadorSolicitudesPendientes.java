package com.example.hector.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hector.testloveapp.R;

import java.util.ArrayList;

import DTO.SolicitudDTO;

/**
 * Created by hectorfrancisco on 17-07-2016.
 */
public class AdaptadorSolicitudesPendientes extends RecyclerView.Adapter<AdaptadorSolicitudesPendientes.SolicitudViewHolder>  {

    private ArrayList<SolicitudDTO> datos;
    private static RecyclerViewClickListener recyclerViewClickListener;

    public AdaptadorSolicitudesPendientes(ArrayList<SolicitudDTO> datos,RecyclerViewClickListener recyclerViewClickListener) {
        this.datos = datos;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    public ArrayList<SolicitudDTO> getDatos() {
        return datos;
    }

    public void setDatos(ArrayList<SolicitudDTO> datos) {
        this.datos = datos;
    }

    @Override
    public SolicitudViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_solicitudes_pendientes, parent, false);

        SolicitudViewHolder svh = new SolicitudViewHolder(itemView);

        return svh;
    }

    @Override
    public void onBindViewHolder(SolicitudViewHolder holder, int position) {
        SolicitudDTO item = datos.get(position);

        holder.bindSolicitud(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }


    public static class SolicitudViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView emisor;
        private TextView receptor;


        public SolicitudViewHolder(View itemView) {
            super(itemView);

            emisor = (TextView)itemView.findViewById(R.id.nombreEmisorFragSolEnv);
            receptor = (TextView)itemView.findViewById(R.id.nombreReceptorFragSolEnv);

            itemView.setOnClickListener(this);
        }

        public void bindSolicitud(SolicitudDTO solicitudDTO) {
            emisor.setText((solicitudDTO.getUserEmisor() != null
                    && !solicitudDTO.getUserEmisor().equals(""))
                    ? solicitudDTO.getUserEmisor() : "");
            receptor.setText((solicitudDTO.getUserReceptor()!=null
                            &&!solicitudDTO.getUserReceptor().equals(""))
                            ? solicitudDTO.getUserReceptor() : "");
        }


        @Override
        public void onClick(View v) {
            recyclerViewClickListener.recyclerViewListClicked(v,this.getLayoutPosition());
        }
    }
}
