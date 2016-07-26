package com.example.hector.testloveapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class VentanaPuntuacion extends AppCompatActivity {

    private TextView TextResultadoPuntuacionPreguntadas;
    private TextView TextResultadoPuntuacionContestadas;
    private long puntuacionContestadas;
    private long puntuacionPreguntadas;
    private String resultado;
    Context context;
    Util util=new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_puntuacion);
        context = getApplicationContext();
        TextResultadoPuntuacionPreguntadas=(TextView) findViewById(R.id.labelPuntuacionPreguntadas);
        TextResultadoPuntuacionContestadas=(TextView) findViewById(R.id.labelPuntuacionContestadas);

        puntuacionPreguntadas=util.getPuntuacionPreguntadasEnCache(context);
        puntuacionContestadas = util.getPuntuacionContestadasEnCache(context);
        resultado=util.getResultadoEnCache(context);


            TextResultadoPuntuacionContestadas.append("Preguntas recibidas ["+puntuacionContestadas+"]");
            TextResultadoPuntuacionPreguntadas.append("Preguntas enviadas ["+puntuacionPreguntadas+"]");

    }
}
