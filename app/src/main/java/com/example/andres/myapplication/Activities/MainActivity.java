package com.example.andres.myapplication.Activities;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andres.myapplication.Fragments.AddStudentDialogFragment;
import com.example.andres.myapplication.Fragments.ListFragment;
import com.example.andres.myapplication.Fragments.StudentFragment;
import com.example.andres.myapplication.Model.Item;
import com.example.andres.myapplication.R;
import com.example.andres.myapplication.Services.SyncService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity implements ListFragment.OnFragmentInteractionListener, StudentFragment.OnFragmentInteractionListener, AddStudentDialogFragment.NoticeDialogListener {

    public static final String CODE_NAME = "name";
    SyncService mService;
    boolean mBound = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Bind to SyncService
        Intent intent = new Intent (this, SyncService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            SyncService.LocalBinder binder = (SyncService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
        else if (id == R.id.action_agregar){
            AddStudentDialogFragment dialog = new AddStudentDialogFragment();
            dialog.show(this.getFragmentManager(), "dialog");
        }

        else if (id == R.id.sync_students_button){
            if (mBound){
                JSONArray students = new JSONArray();
                try {
                    students = mService.getStudents();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //TODO: make operations with students here
                ListFragment listFrag = (ListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_list);
                try {
                    listFrag.mergeWithCloud(students);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteractionList(Item item) {

        String name = item.getmTexto();

        StudentFragment studentFrag = (StudentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.student_fragment);

        if (studentFrag != null && studentFrag.isVisible()){
                studentFrag.setNombre(item.getmTexto());
        }
        else{
            Intent intent = new Intent(this,StudentActivity.class );
            intent.putExtra(CODE_NAME, name);
            startActivity(intent);
        }

    }

    @Override
    public void onAddStudentsToCloud(ArrayList<String> students) {
        if (mBound){
            mService.addStudents(students);
        }
    }

    @Override
    public void onFragmentInteractionStudent() {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name, String firstLastname, String secondLastname) {
        ListFragment listFrag = (ListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        if (listFrag == null) return;

        listFrag.addStudent(name, firstLastname, secondLastname);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
