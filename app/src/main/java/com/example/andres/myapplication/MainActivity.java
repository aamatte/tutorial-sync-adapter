package com.example.andres.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static final String  VALOR = "1234123";

    String[] nombres = new String[] {"AGUILERA DONOSO SIMON ANDRES",
            "AGUIRRE ORELLANA CARLOS ALFONSO",
            "ALFARO CARPANETTI FRANCO RAUL",
            "Castro Retamal Jaime Esteban",
            "CAVADA MEDINA FABRIZIO REINALDO",
            "CELHAY RODRIGUEZ JUAN IGNACIO",
            "CHAUMIER   PIERRE- VICTOR",
            "CHICAO SOTO JAVIER ANTONIO",
            "CORREA VELASCO ENRIQUE JOSE",
            "Domínguez Manquenahuel Vicente Ignacio",
            "DRAGICEVIC HERNANDEZ VICENTE RAFAEL",
            "FERRER SALAS IGNACIO ANDRES",
            "GARCIA BUZETA NATALIA",
            "GOMEZ ARAYA RODRIGO NICOLAS TEOFILO",
            "HEYSEN PALACIOS JURGEN DIETER",
            "JADUE ABUAUAD PILAR IGNACIA",
            "LUCCHINI WORTZMAN FRANCESCA",
            "MARTI OLBRICH SANTIAGO ANDRES",
            "MATTE VALLEJOS ANDRES ARTURO",
            "MONSALVE SANTANDER GERALDINE NICOLE",
            "OCHAGAVIA BALBONTIN BALTAZAR",
            "OLIVA LARA SEBASTIAN ANDRES",
            "PERALTA NASIFF VICENTE PASTOR",
            "ROJAS VICTORIANO CARLOS IGNACIO",
            "SALATA RUIZ-TAGLE SEBASTIAN ALFONSO",
            "SIMON COMPTE FELIPE IGNACIO",
            "SINAY CODNER DIEGO",
            "SOTO SUAREZ ADRIAN ANDRES"};

    /** Genera arreglo de Items indexado por letra. Recibe arreglo de strings ordenados. */
    private Item[] generateIndexedItemArray(String[] nombres){

        ArrayList<Item> items = new ArrayList<Item>();

        char last = nombres[0].charAt(0);
        items.add(new Item(last+"", 0));

        for (int i=0; i<nombres.length; i++){

            if (last != nombres[i].charAt(0)){

                last = nombres[i].charAt(0);
                Item item = new Item(last+"", 0);
                items.add(item);
            }

            items.add(new Item(nombres[i],1));
        }

        Item[] lista = items.toArray(new Item[items.size()]);
        return lista;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView list = (ListView) findViewById(R.id.listView);

        final MiAdaptador adaptador = new MiAdaptador(this, R.layout.list_item , generateIndexedItemArray(nombres));

        list.setAdapter(adaptador);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = NuevaActividad.getIntent(MainActivity.this);

                // acá filtro por tipo de celda
                if (adaptador.getItem(position).getmTipoCelda()==1){
                    intent.putExtra(VALOR, adaptador.getItem(position).getmTexto());
                    startActivity(intent);
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
