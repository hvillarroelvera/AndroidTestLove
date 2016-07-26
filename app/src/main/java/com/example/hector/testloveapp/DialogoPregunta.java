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

import DTO.PreguntaDTO;

/**
 * Created by hectorfrancisco on 31-05-2016.
 */
public class DialogoPregunta extends DialogFragment {

    private EditText txtRespuesta;
    private TextView textViewPregunta;
    private Button botonResponder;
    private PreguntaDTO preguntaDTO = new PreguntaDTO();

    public PreguntaDTO getPreguntaDTO() {
        return preguntaDTO;
    }

    public void setPreguntaDTO(PreguntaDTO preguntaDTO) {
        this.preguntaDTO = preguntaDTO;
    }

    public DialogoPregunta() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogoPregunta newInstance(String title) {
        DialogoPregunta frag = new DialogoPregunta();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogo_pregunta, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        textViewPregunta= (TextView) view.findViewById(R.id.lblPregunta);
        txtRespuesta = (EditText) view.findViewById(R.id.editTextRespuesta);
        botonResponder = (Button) view.findViewById(R.id.btnResponder);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Responder pregunta");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        txtRespuesta.requestFocus();
        textViewPregunta.append(" " + getPreguntaDTO().getPregunta());

        botonResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClickDialogoPreguntaListener clickDialogoResponderListener =
                        (ClickDialogoPreguntaListener)getActivity();
                clickDialogoResponderListener.onClickResponder(txtRespuesta.getText().toString());
                getDialog().dismiss();
            }
        });
    }

    public interface ClickDialogoPreguntaListener {
        void onClickResponder(String texto);
    }
}
