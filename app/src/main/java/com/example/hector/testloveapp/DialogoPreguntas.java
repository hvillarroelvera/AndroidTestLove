package com.example.hector.testloveapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import java.util.ArrayList;

import DTO.PreguntaDTO;

/**
 * Created by hectorfrancisco on 19-05-2016.
 */
public class DialogoPreguntas extends DialogFragment{

    private ArrayList<String> preguntas=new ArrayList<String>();
    ArrayList<PreguntaDTO>listaPreguntas=new ArrayList<PreguntaDTO>();

    public ArrayList<PreguntaDTO> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(ArrayList<PreguntaDTO> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    private short tipo;

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public String[] getArrayFromArrayListPreguntas(){
        String[] arrayPreguntas = new String[this.listaPreguntas.size()];
        PreguntaDTO preguntaDTO = null;
        for(int i=0;i<this.listaPreguntas.size();i++){
            preguntaDTO = this.listaPreguntas.get(i);
            arrayPreguntas[i] = preguntaDTO.getPregunta();
        }
        return arrayPreguntas;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Selección de Pregunta")
                .setItems(getArrayFromArrayListPreguntas(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ClickDialogoPreguntasListener clickDialogoPreguntasListener = (ClickDialogoPreguntasListener) getActivity();
                        clickDialogoPreguntasListener.onClickPregunta(listaPreguntas.get(item).getPregunta(),
                                listaPreguntas.get(item).getNumero(), getTipo(),item);
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    public interface ClickDialogoPreguntasListener{
        void onClickPregunta(String pregunta,long numeroPregunta,short tipo,int index);
    }
}
