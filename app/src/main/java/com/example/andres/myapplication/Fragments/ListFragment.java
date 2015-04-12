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
import com.example.andres.myapplication.Model.Student;
import com.example.andres.myapplication.MyAdapter;
import com.example.andres.myapplication.Persistence.DatabaseManagement;
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


public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private final String linkDescargaArchivo = "https://drive.google.com/uc?export=download&id=0B8yZfi78R0lEd2NRb2dRZGtPM2M";
    private ArrayList<Item> indexedItemArray = new ArrayList<Item>();
    private ArrayList<String> nombres;
    private ArrayList<Student> students;
    private MyAdapter adaptador;
    private ListView listView;
    ArrayList<Integer> idCloudFromPhone;

    DatabaseManagement.Students.StudentsDbHelper mDbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);
        adaptador = new MyAdapter(v.getContext() , R.layout.list_item , indexedItemArray);
        students = new ArrayList<Student>();
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



    private ArrayList<Student> stringArrayToStudentArray(ArrayList<String>  stringArrayList, ArrayList<Student>  studentArrayList){
        if (studentArrayList==null){
            studentArrayList = new ArrayList<Student>();
        }
        for (int i=0; i<stringArrayList.size(); i++){
            String[] name = stringArrayList.get(i).split(" ");
            if (name.length == 4){
                studentArrayList.add(new Student(name[2] + " " + name[3], name[0], name[1]));
            }
            else if (name.length==3){
                studentArrayList.add(new Student(name[2], name[0], name[1]));
            }
            else if (name.length == 2){
                studentArrayList.add(new Student(name[1], name[0]));
            }
        }
        return studentArrayList;
    }

    private ArrayList<String> studentArrayToStringArray(ArrayList<Student> students,ArrayList<String>  strings ){
        if (strings==null){
            strings = new ArrayList<String>();
        }
        for (int i=0; i<students.size(); i++){
            Student s = students.get(i);
            strings.add(s.getFirstLastname() + " "+ s.getSecondLastname() +" "+ s.getNames());
        }
        return strings;
    }



    public void addStudent(Student student){

        // TODO: Agregar columna idCloud a tabla y adaptar metodos para eso
        addStudentToIndexedArray(student);
        addStudentToDb(student);
    }

    private void addStudentToIndexedArray(Student student){
        students.add(student);
        generateIndexedItemArray();
    }

    private void addStudentToDb(Student student){

        if (mDbHelper == null) {
            mDbHelper = DatabaseManagement.Students.StudentsDbHelper.getInstance(getActivity());
        }


        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(DatabaseManagement.Students.COLUMN_NAME_STUDENT_NAMES, student.getNames());
        values.put(DatabaseManagement.Students.COLUMN_NAME_FIRST_LASTNAME, student.getFirstLastname());
        values.put(DatabaseManagement.Students.COLUMN_NAME_SECOND_LASTNAME, student.getSecondLastname());
        values.put(DatabaseManagement.Students.COLUMN_ID_CLOUD, student.getIdCloud());

        db.insert(
                DatabaseManagement.Students.TABLE_NAME,
                null,
                values);


    }

    private boolean selectStudentsFromDb(){

        if (mDbHelper == null) {
            mDbHelper = DatabaseManagement.Students.StudentsDbHelper.getInstance(getActivity());
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //mDbHelper.onUpgrade(db, 1, 2);
        Cursor c = db.query(DatabaseManagement.Students.TABLE_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            DatabaseManagement.Students.COLUMN_NAME_FIRST_LASTNAME +" ASC", null);

        ArrayList<Student> studentsInDb = new ArrayList<Student>();

        c.moveToFirst();

        if (c.getCount()<1){
            return false;
        }

        while (c.moveToNext()){
            String names = c.getString(c.getColumnIndexOrThrow(DatabaseManagement.Students.COLUMN_NAME_STUDENT_NAMES)).toUpperCase();
            String firstLast = c.getString(c.getColumnIndexOrThrow(DatabaseManagement.Students.COLUMN_NAME_FIRST_LASTNAME)).toUpperCase();
            String secondLast = c.getString(c.getColumnIndexOrThrow(DatabaseManagement.Students.COLUMN_NAME_SECOND_LASTNAME)).toUpperCase();
            int idCloud = c.getInt(c.getColumnIndexOrThrow(DatabaseManagement.Students.COLUMN_ID_CLOUD));
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

    // Generate indexed Array and add the students to the database. Only use when there is no data in the database.
    private void generateIndexedItemArray(String archivo){
        if (archivo.length() ==0 || archivo == null) return;
        nombres = new ArrayList<String>(Arrays.asList(archivo.split("\n")));
        students = stringArrayToStudentArray(nombres, students);

        ArrayList<Item> items = new ArrayList<Item>();

        char last = students.get(0).getFirstLastname().charAt(0);
        Item firstLetter = new Item(last + "", 0);
        items.add(firstLetter);

        for (int i=0; i<students.size(); i++){
            Student s = students.get(i);

            if (last != s.getFirstLastname().charAt(0)){

                s.setNames(s.getNames().toUpperCase());
                s.setFirstLastname(s.getFirstLastname().toUpperCase());
                s.setSecondLastname(s.getSecondLastname().toUpperCase());

                last = s.getFirstLastname().charAt(0);
                Item item = new Item(last+"", 0);
                items.add(item);
            }

            items.add(new Item(s.getNames() + " " + s.getFirstLastname() + " " + s.getSecondLastname(),1));
            addStudentToDb(s);
        }

        indexedItemArray = items;
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

    public ArrayList<Student> fillPhoneDbWithCloudDb(JSONArray jsonArray) throws JSONException {
        ArrayList<Student> arrayList = new ArrayList<Student>();
        idCloudFromPhone = idCloudFromPhone();

        for (int i=0; i<jsonArray.length(); i++){

            JSONObject studentJSON = jsonArray.getJSONObject(i);

            String name = studentJSON.getString("name");
            String firstLastname = studentJSON.getString("first_lastname");
            String secondLastname = studentJSON.getString("second_lastname");
            int idCloud = studentJSON.getInt("id");
            Student student = new Student(name, firstLastname, secondLastname, idCloud);
            arrayList.add(student);

            if (!idCloudFromPhone.contains(idCloud)){
                student.setIdCloud(idCloud);
                addStudent(student);
                idCloudFromPhone.add(idCloud);
            }
        }
        return arrayList;
    }

    public ArrayList idCloudFromPhone() {
        ArrayList list = new ArrayList();
        for (int i=0; i<students.size(); i++) {
            list.add(students.get(i).getIdCloud());
        }
        return list;
     }

    public void phoneToCloud(ArrayList<Student> studentsInCloud) throws JSONException {
        ArrayList<Student> studentsNotInCloud = new ArrayList<Student>();
        for (int i=0; i<students.size(); i++){
            if (!idCloudFromPhone.contains(students.get(i).getIdCloud())){
                studentsNotInCloud.add(students.get(i));
            }
        }

        mListener.onAddStudentsToCloud(studentsNotInCloud);

    }

    public void mergeWithCloud(JSONArray jsonArray) throws JSONException {
        ArrayList<Student> studentsInCloud = fillPhoneDbWithCloudDb(jsonArray);
        phoneToCloud(studentsInCloud);

    }


    public void upgradeStudent(Student student) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.rawQuery("UPGRADE " + DatabaseManagement.Students.TABLE_NAME + " SET " +
                DatabaseManagement.Students.COLUMN_ID_CLOUD + "=" + student.getIdCloud() +
                " " + DatabaseManagement.Students.COLUMN_NAME_STUDENT_NAMES + "=" + student.getNames() +
                " " + DatabaseManagement.Students.COLUMN_NAME_FIRST_LASTNAME + "=" + student.getFirstLastname() +
                " " + DatabaseManagement.Students.COLUMN_NAME_SECOND_LASTNAME + "=" + student.getSecondLastname()
                , null);

    }




        @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteractionList(Item item);
        public void onAddStudentsToCloud(ArrayList<Student> students);
        public void onGetStudentsFromCloud();
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
