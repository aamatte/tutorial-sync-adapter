package com.example.andres.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private final String linkDescargaArchivo = "https://drive.google.com/uc?export=download&id=0B8yZfi78R0lEd2NRb2dRZGtPM2M";
    private Item[] indexedItemArray = new Item[0];
    private String[] nombres;
    private MiAdaptador adaptador;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        listView = (ListView) v.findViewById(R.id.list_students);

        DownloadFileTask downloadFileTask = new DownloadFileTask();
        downloadFileTask.execute(linkDescargaArchivo);

        adaptador = new MiAdaptador(v.getContext() , R.layout.list_item , indexedItemArray);
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
        addItemToIndexedArray(name);
    }

    private void addItemToIndexedArray(String nombre){

        String[] newArray = new String[nombres.length+1];
        nombre = Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);

        for (int i=0; i<nombres.length; i++){
            newArray[i] = nombres[i];
        }
        newArray[nombres.length] = nombre;
        Arrays.sort(newArray);
        nombres = newArray;
        indexedItemArray = generateIndexedItemArray();
        adaptador = new MiAdaptador(getActivity(),  R.layout.list_item , indexedItemArray);
        listView.setAdapter(adaptador);
    }

    private Item[] generateIndexedItemArray(){

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
            String nombreOrdenado = nombreOrdenado(nombreDesordenado);
            items.add(new Item(nombreOrdenado,1));
        }

        Item[] lista = items.toArray(new Item[items.size()]);
        return lista;
    }


    private Item[] generateIndexedItemArray(String archivo){

        nombres = archivo.split("\n");
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
            String nombreOrdenado = nombreOrdenado(nombreDesordenado);
            items.add(new Item(nombreOrdenado,1));
        }

        Item[] lista = items.toArray(new Item[items.size()]);
        return lista;
    }

    private String nombreOrdenado(String[] nombreDesordenado){
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
            for (int i = 0; i<nombreDesordenado.length; i++){
                nombreOrdenado = nombreDesordenado[i] + " ";
            }
        }
        return nombreOrdenado;
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


    public interface OnFragmentInteractionListener {
        public void onFragmentInteractionList(Item item);
    }




    // TODO: DIFICULTAD: async comunicaction
    private class DownloadFileTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String archivo = performRequest(params[0]);
            return archivo;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            indexedItemArray = generateIndexedItemArray(result);
            adaptador = new MiAdaptador(getActivity(),  R.layout.list_item , indexedItemArray);
            listView.setAdapter(adaptador);
        }

        public String performRequest(String urlString) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();

                InputStream is = urlConnection.getInputStream();

                InputStreamReader isr = new InputStreamReader(is);

                BufferedReader reader = new BufferedReader(isr);

                String response = "";

                String line;

                while ((line = reader.readLine()) != null) {
                    response += line + "\n";
                }

                reader.close();
                urlConnection.disconnect();
                return response;



            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (urlConnection != null)  urlConnection.disconnect();
            }

            return null;

        }


    }

}
