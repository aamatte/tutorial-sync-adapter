package com.example.andres.myapplication.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andres.myapplication.Model.Item;
import com.example.andres.myapplication.Model.Student;
import com.example.andres.myapplication.MyAdapter;
import com.example.andres.myapplication.Persistence.DatabaseContract;
import com.example.andres.myapplication.Persistence.StudentsDbHelper;
import com.example.andres.myapplication.Provider.StudentsContract;
import com.example.andres.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Item> indexedItemArray = new ArrayList<Item>();
    private ArrayList<String> nombres;
    private ArrayList<Student> students;
    private MyAdapter adaptador;
    private ListView listView;
    StudentsDbHelper mDbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);
        adaptador = new MyAdapter(v.getContext() , R.layout.list_item , indexedItemArray);
        students = new ArrayList<>();
        if (!selectStudentsFromDb()){
            mListener.onGetStudentsFromCloud();
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

    public void addStudent(Student student){
        if (!studentInDb(student)) return;
        addStudentToIndexedArray(student);
        addStudentToDb(student);
    }

    private void addStudentToIndexedArray(Student student){
        students.add(student);
        generateIndexedItemArray();
    }

    private boolean addStudentToDb(Student student){
        if (mDbHelper == null) {
            mDbHelper = StudentsDbHelper.getInstance(getActivity());
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();



        ContentValues values = new ContentValues();
        values.put(StudentsContract.StudentsColumns.NAMES, student.getNames());
        values.put(StudentsContract.StudentsColumns.FIRST_LASTNAME, student.getFirstLastname());
        values.put(StudentsContract.StudentsColumns.SECOND_LASTNAME, student.getSecondLastname());
        values.put(StudentsContract.StudentsColumns.ID_CLOUD, student.getIdCloud());

        Uri mNewUri = getActivity().getContentResolver().insert(
                StudentsContract.STUDENTS_URI,   // the user dictionary content URI
                values                          // the values to insert
        );                                  // Valores

        if (mNewUri != null)
            return true;
        return false;
    }

    private boolean selectStudentsFromDb(){

        if (mDbHelper == null) {
            mDbHelper = StudentsDbHelper.getInstance(getActivity());
        }

        // Selecciono todas las columnas. Podría dejarse como null.
        String[] projection = {DatabaseContract.Students.COLUMN_NAME_STUDENT_NAMES,
                               DatabaseContract.Students.COLUMN_NAME_FIRST_LASTNAME,
                               DatabaseContract.Students.COLUMN_NAME_SECOND_LASTNAME,
                               DatabaseContract.Students.COLUMN_ID_CLOUD
                               };

        // Se deja como null porque no se requiere filtrar. Un ejemplo podria ser asi:
        // String selection = DatabaseContract.Students.COLUMN_NAME_FIRST_LASTNAME + "= ?"
        String selection = null;

        // Podria ser:
        // String[] selectionArgs = new String[]{ "Apellido" };
        String[] selectionArgs = null;


        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c = db.query(DatabaseContract.Students.TABLE_NAME,   // Tabla
                            projection,                             // Columnas a retornar
                            selection,                              // Columnas de WHERE
                            selectionArgs,                          // Valores de WHERE
                            null,                                   // Group by
                            null,                                   // Filtro por columnas de grupos
                            DatabaseContract.Students.COLUMN_NAME_FIRST_LASTNAME +" ASC"); // Sort order

        ArrayList<Student> studentsInDb = new ArrayList<Student>();

        c.moveToFirst();

        if (c.getCount()<1){
            return false;
        }

        while (c.moveToNext()){
            String names = c.getString(c.getColumnIndexOrThrow(DatabaseContract.Students.COLUMN_NAME_STUDENT_NAMES)).toUpperCase();
            String firstLast = c.getString(c.getColumnIndexOrThrow(DatabaseContract.Students.COLUMN_NAME_FIRST_LASTNAME)).toUpperCase();
            String secondLast = c.getString(c.getColumnIndexOrThrow(DatabaseContract.Students.COLUMN_NAME_SECOND_LASTNAME)).toUpperCase();
            int idCloud = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.Students.COLUMN_ID_CLOUD));
            studentsInDb.add(new Student(names, firstLast, secondLast, idCloud));
        }

        if (studentsInDb.size()>0){
            students = studentsInDb;
            generateIndexedItemArray();
            return true;
        }

        return false;


    }


    private void generateIndexedItemArray(){

        ArrayList<Item> items = new ArrayList<Item>();

        Collections.sort(students, new CustomComparator());

        char last = students.get(0).getFirstLastname().charAt(0);

        items.add(new Item(last+"", 0));

        for (int i=0; i<students.size(); i++){
            Student s = students.get(i);
            if (last != s.getFirstLastname().charAt(0)){

                last = s.getFirstLastname().charAt(0);
                Item item = new Item(last+"", 0);
                items.add(item);
            }
            items.add(new Item(s.getNames() + " " + s.getFirstLastname() + " " + s.getSecondLastname() + " ", 1));
        }

        indexedItemArray = items;
        adaptador.clear();
        adaptador.addAll(items);
        adaptador.notifyDataSetChanged();

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

    public boolean studentInDb(Student s){
        for (int i=0; i<students.size(); i++){
            if (students.get(i).getNames().equals(s.getNames()) &&
                    students.get(i).getFirstLastname().equals(s.getFirstLastname())&&
                    students.get(i).getSecondLastname().equals(s.getSecondLastname())){
                return false;
            }
        }
        return true;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteractionList(Item item);
        void onGetStudentsFromCloud();
    }


    public class CustomComparator implements Comparator<Student> {
        @Override
        public int compare(Student o1, Student o2) {
            return o1.getFirstLastname().compareTo(o2.getFirstLastname());
        }
    }
}
