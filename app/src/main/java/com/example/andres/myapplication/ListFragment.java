package com.example.andres.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    // TODO: hacer que se pueble desde un archivo en internet
    private String[] nombres = new String[] {"AGUILERA DONOSO SIMON ANDRES",
            "AGUIRRE ORELLANA CARLOS ALFONSO",
            "ALFARO CARPANETTI FRANCO RAUL",
            "Castro Retamal Jaime Esteban",
            "CAVADA MEDINA FABRIZIO REINALDO",
            "CELHAY RODRIGUEZ JUAN IGNACIO",
            "CHAUMIER PIERRE VICTOR",
            "CHICAO SOTO JAVIER ANTONIO",
            "CORREA VELASCO ENRIQUE JOSE",
            "Domínguez Manquenahuel Vicente Ignacio",
            "DRAGICEVIC HERNANDEZ VICENTE RAFAEL",
            "FERRER SALAS IGNACIO ANDRES",
            "GARCIA BUZETA NATALIA",
            "GOMEZ ARAYA RODRIGO NICOLAS",
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        ListView listView = (ListView) v.findViewById(R.id.list_students);

        final MiAdaptador adaptador = new MiAdaptador(v.getContext() , R.layout.list_item , generateIndexedItemArray(nombres));
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // acá filtro por tipo de celda
                if (adaptador.getItem(position).getmTipoCelda()==1){
                    mListener.onFragmentInteractionList(adaptador.getItem(position));
                }
            }
        });


        return v;

    }

    public void agregarAlumno(String name){
        // TODO: hacer metodo para agregar a la lista
        Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
    }

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
            String[] nombreDesordenado = nombres[i].split(" ");
            String nombreOrdenado ="";
            if (nombreDesordenado.length==3){
                String apellido1= nombreDesordenado[0];
                String apellido2= nombreDesordenado[1];
                String nombre = nombreDesordenado[2];
                nombreOrdenado = nombre+" "+apellido1 + " "+ apellido2;
            }
            else if (nombreDesordenado.length==4){
                String apellido1= nombreDesordenado[0];
                String apellido2= nombreDesordenado[1];
                String nombre1 = nombreDesordenado[2];
                String nombre2 = nombreDesordenado[3];
                nombreOrdenado = nombre1+" "+nombre2 +" "+apellido1 + " "+ apellido2;
            }
            else{
                nombreOrdenado  = nombres[i];
            }
            items.add(new Item(nombreOrdenado,1));
        }

        Item[] lista = items.toArray(new Item[items.size()]);
        return lista;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteractionList(Item item);
    }

}
