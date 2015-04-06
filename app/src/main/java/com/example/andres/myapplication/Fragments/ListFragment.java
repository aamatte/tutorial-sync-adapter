package com.example.andres.myapplication.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andres.myapplication.Model.Item;
import com.example.andres.myapplication.MyAdapter;
import com.example.andres.myapplication.Persistence.DatabaseContract;
import com.example.andres.myapplication.R;

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
    private ArrayList<Item> indexedItemArray = new ArrayList<Item>();
    private String[] nombres;
    private MyAdapter adaptador;
    private ListView listView;
    DatabaseContract.Students.StudentsDbHelper mDbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);
        adaptador = new MyAdapter(v.getContext() , R.layout.list_item , indexedItemArray);

        if (savedInstanceState != null){
            nombres = savedInstanceState.getStringArray("alumnos");
            generateIndexedItemArray();
        }
        else if (!selectStudentsFromDb()){
            DownloadFileTask downloadFileTask = new DownloadFileTask();
            downloadFileTask.execute(linkDescargaArchivo);
        }
        // Inflate the layout for this fragment
        listView = (ListView) v.findViewById(R.id.list_students);


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

    @Override
    public void onSaveInstanceState(Bundle savedState) {

        // Note: getValues() is a method in your ArrayAdaptor subclass
        savedState.putStringArray("alumnos", nombres);
        super.onSaveInstanceState(savedState);

    }

    public void addStudent(String name, String firstLastname, String secondLastname){
        addItemToIndexedArray(name, firstLastname, secondLastname);

        addStudentToDb(name, firstLastname, secondLastname);
    }

    private void addItemToIndexedArray(String nombre, String firstLastname, String secondLastname){

        String[] newArray = new String[nombres.length+1];
        nombre = Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1);

        for (int i=0; i<nombres.length; i++){
            newArray[i] = nombres[i];
        }
        newArray[nombres.length] = firstLastname + " " + secondLastname + " " + nombre;
        Arrays.sort(newArray);
        nombres = newArray;
        generateIndexedItemArray();
    }

    private void addStudentToDb(String name, String firstLastname, String secondLastname){

        if (mDbHelper == null) {
            mDbHelper = DatabaseContract.Students.StudentsDbHelper.getInstance(getActivity());
        }


        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Students.COLUMN_NAME_STUDENT_NAMES, name);
        values.put(DatabaseContract.Students.COLUMN_NAME_FIRST_LASTNAME, firstLastname);
        values.put(DatabaseContract.Students.COLUMN_NAME_SECOND_LASTNAME, secondLastname);

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.Students.TABLE_NAME,
                null,
                values);


    }

    private boolean selectStudentsFromDb(){

        if (mDbHelper == null) {
            mDbHelper = DatabaseContract.Students.StudentsDbHelper.getInstance(getActivity());
        }



        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        Cursor c = db.query(DatabaseContract.Students.TABLE_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            DatabaseContract.Students.COLUMN_NAME_FIRST_LASTNAME +" ASC", null);

        ArrayList<String> students = new ArrayList<String>();

        c.moveToFirst();

        if (c.getCount()<1){
            return false;
        }

        while (c.moveToNext()){
            String names = c.getString(c.getColumnIndexOrThrow(DatabaseContract.Students.COLUMN_NAME_STUDENT_NAMES)).toUpperCase();
            String firstLast = c.getString(c.getColumnIndexOrThrow(DatabaseContract.Students.COLUMN_NAME_FIRST_LASTNAME)).toUpperCase();
            String secondLast = c.getString(c.getColumnIndexOrThrow(DatabaseContract.Students.COLUMN_NAME_SECOND_LASTNAME)).toUpperCase();
            students.add(firstLast + " " + secondLast + " " + names);
        }

        if (students.size()>0){
            nombres = students.toArray(new String[students.size()]);
            generateIndexedItemArray();
            return true;
        }

        return false;


    }


    private void generateIndexedItemArray(){

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

        indexedItemArray = items;
        adaptador.clear();
        adaptador.addAll(items);
        adaptador.notifyDataSetChanged();

    }

    // Generate indexed Array and add the students to the database. Only use when there is no data in the database.
    private void generateIndexedItemArray(String archivo){
        if (archivo.length() ==0 || archivo == null) return;
        nombres = archivo.split("\n");
        ArrayList<Item> items = new ArrayList<Item>();

        char last = nombres[0].charAt(0);
        Item firstLetter = new Item(last + "", 0);
        items.add(firstLetter);


        for (int i=0; i<nombres.length; i++){

            if (last != nombres[i].charAt(0)){
                nombres[i] = nombres[i].toUpperCase();
                last = nombres[i].charAt(0);
                Item item = new Item(last+"", 0);
                items.add(item);
            }
            String[] nombreDesordenado = nombres[i].split(" ");
            String nombreOrdenado = nombreOrdenado(nombreDesordenado);
            items.add(new Item(nombreOrdenado,1));
            String[] a = nombreOrdenado.split(" ");
            if (a.length == 4) addStudentToDb(a[0] + " " + a[1], a[2], a[3]);
            else if (a.length == 3) addStudentToDb(a[0], a[1], a[2]);
            else if (a.length == 2) addStudentToDb(a[0] , a[1], "");
            else addStudentToDb(a[a.length-1] , "", "");
        }

        indexedItemArray = items;
        adaptador.addAll(items);
        adaptador.notifyDataSetChanged();

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
                nombreOrdenado += nombreDesordenado[i] + " ";
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




    private class DownloadFileTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String archivo = performRequest(params[0]);
            return archivo;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            generateIndexedItemArray(result);
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
