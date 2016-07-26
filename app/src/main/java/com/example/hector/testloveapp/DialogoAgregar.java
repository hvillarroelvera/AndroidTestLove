package com.example.hector.testloveapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by hectorfrancisco on 25-05-2016.
 */
public class DialogoAgregar extends DialogFragment {

    private EditText txtIngresar;
    private Button botonIngresar;
    private short tipoDialogo;

    public short getTipoDialogo() {
        return tipoDialogo;
    }

    public void setTipoDialogo(short tipoDialogo) {
        this.tipoDialogo = tipoDialogo;
    }

    public DialogoAgregar() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogoAgregar newInstance(String title) {
        DialogoAgregar frag = new DialogoAgregar();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogo_agregar, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        txtIngresar = (EditText) view.findViewById(R.id.txt_ingresar);
        botonIngresar = (Button) view.findViewById(R.id.btn_ingresar);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Ingresar Pregunta");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        txtIngresar.requestFocus();

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClickDialogoAgregarListener clickDialogoAgregarListener =
                        (ClickDialogoAgregarListener)getActivity();
                clickDialogoAgregarListener.onClickAgregar(txtIngresar.getText().toString(),getTipoDialogo());
                getDialog().dismiss();
            }
        });
    }

    public interface ClickDialogoAgregarListener {
        void onClickAgregar(String texto,short tipoDialogo);
    }

}
