package com.example.andres.myapplication;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements ListFragment.OnFragmentInteractionListener, StudentFragment.OnFragmentInteractionListener, AgregarAlumnoDialogFragment.NoticeDialogListener {


    public static final String CODE_NAME = "name";
    /** Genera arreglo de Items indexado por letra. Recibe arreglo de strings ordenados. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout, new ListFragment())
                    .commit();
        }


    }


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
            AgregarAlumnoDialogFragment dialog = new AgregarAlumnoDialogFragment();
            dialog.show(this.getFragmentManager(), "dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteractionList(Item item) {

        String name = item.getmTexto();

        // TODO: SI EL OTRO FRAGMENT NO ES NULO
        if (false){
            // Fragment de la lista
            StudentFragment studentFragment = new StudentFragment();

            Bundle args = new Bundle();
            args.putString(CODE_NAME, name);

            studentFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout, studentFragment)
                    .addToBackStack(null)
                    .commit();
        }
        Intent intent = new Intent(this,StudentActivity.class );
        intent.putExtra(CODE_NAME, name);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteractionStudent() {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        ListFragment listFrag = (ListFragment)
                getSupportFragmentManager().findFragmentById(R.id.layout);
        if (listFrag == null) return;
        listFrag.agregarAlumno(name);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
