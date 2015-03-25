package com.example.andres.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by andres on 19-03-15.
 */
public class NuevaActividad extends Activity{

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, NuevaActividad.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nuevo);

        String valor = getIntent().getStringExtra(MainActivity.VALOR);
        TextView texto = (TextView) findViewById(R.id.texto);
        texto.setText(valor);

    }
}
