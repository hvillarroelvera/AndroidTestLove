package com.example.hector.testloveapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by hectorfrancisco on 26-05-2016.
 */
public class DialogoSolicitudContacto extends DialogFragment {

    private TextView txtNombreContacto;
    private Button botonAceptar;
    private Button botonRechazar;
    private String nombreUserSolicitante;

    public String getNombreUserSolicitante() {
        return nombreUserSolicitante;
    }

    public void setNombreUserSolicitante(String nombreUserSolicitante) {
        this.nombreUserSolicitante = nombreUserSolicitante;
    }

    public DialogoSolicitudContacto() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogoSolicitudContacto newInstance(String title) {
        DialogoSolicitudContacto frag = new DialogoSolicitudContacto();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogo_solicitud_contacto, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        txtNombreContacto = (TextView) view.findViewById(R.id.txt_nombre_contacto);
        botonAceptar = (Button) view.findViewById(R.id.btn_aceptar);
        botonRechazar = (Button) view.findViewById(R.id.btn_rechazar);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Solicitud contacto");
        getDialog().setTitle(title);
        txtNombreContacto.append(" " + getNombreUserSolicitante());
        // Show soft keyboard automatically and request focus to field

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClickDialogoSolicitudContactoListener clickDialogoSolicitudContactoListener =
                        (ClickDialogoSolicitudContactoListener)getActivity();
                clickDialogoSolicitudContactoListener.onClickAceptar();
                getDialog().dismiss();
            }
        });
        botonRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClickDialogoSolicitudContactoListener clickDialogoSolicitudContactoListener =
                        (ClickDialogoSolicitudContactoListener)getActivity();
                clickDialogoSolicitudContactoListener.onClickRechazar();
                getDialog().dismiss();
            }
        });
    }

    public interface ClickDialogoSolicitudContactoListener {
        void onClickAceptar();
        void onClickRechazar();
    }
}
