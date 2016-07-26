package com.example.hector.testloveapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hectorfrancisco on 31-05-2016.
 */
public class DialogoRespuesta extends DialogFragment {

    private TextView lblRespuesta;
    private Button botonCorrecto;
    private Button botonIncorrecto;
    private String preguntaContestada;

    public String getPreguntaContestada() {
        return preguntaContestada;
    }

    public void setPreguntaContestada(String preguntaContestada) {
        this.preguntaContestada = preguntaContestada;
    }

    public DialogoRespuesta() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogoRespuesta newInstance(String title) {
        DialogoRespuesta frag = new DialogoRespuesta();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogo_respuesta, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        lblRespuesta = (TextView) view.findViewById(R.id.lblRespuesta);
        botonCorrecto = (Button) view.findViewById(R.id.btnCorrecto);
        botonIncorrecto = (Button) view.findViewById(R.id.btnIncorrecto);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Respuesta pregunta");
        getDialog().setTitle(title);
        lblRespuesta.append(" " + getPreguntaContestada());
        // Show soft keyboard automatically and request focus to field

        botonCorrecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClickDialogoRespuestaListener clickDialogoRespuestaListener =
                        (ClickDialogoRespuestaListener)getActivity();
                clickDialogoRespuestaListener.onClickCorrecto();
                getDialog().dismiss();
            }
        });
        botonIncorrecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClickDialogoRespuestaListener clickDialogoRespuestaListener =
                        (ClickDialogoRespuestaListener)getActivity();
                clickDialogoRespuestaListener.onClickIncorrecto();
                getDialog().dismiss();
            }
        });
    }

    public interface ClickDialogoRespuestaListener {
        void onClickCorrecto();
        void onClickIncorrecto();
    }
}
