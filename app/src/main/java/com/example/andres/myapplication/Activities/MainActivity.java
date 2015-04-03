package com.example.andres.myapplication.Activities;

import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andres.myapplication.Fragments.AddStudentDialogFragment;
import com.example.andres.myapplication.Fragments.ListFragment;
import com.example.andres.myapplication.Model.Item;
import com.example.andres.myapplication.R;
import com.example.andres.myapplication.Fragments.StudentFragment;

public class MainActivity extends ActionBarActivity implements ListFragment.OnFragmentInteractionListener, StudentFragment.OnFragmentInteractionListener, AddStudentDialogFragment.NoticeDialogListener {

    public static final String CODE_NAME = "name";
    /** Genera arreglo de Items indexado por letra. Recibe arreglo de strings ordenados. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            AddStudentDialogFragment dialog = new AddStudentDialogFragment();
            dialog.show(this.getFragmentManager(), "dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteractionList(Item item) {

        String name = item.getmTexto();

        StudentFragment studentFrag = (StudentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.student_fragment);

        // http://codereview.stackexchange.com/questions/64045/two-fragments-in-landscape-mode-challenge

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
    public void onFragmentInteractionStudent() {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        ListFragment listFrag = (ListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        if (listFrag == null) return;

        listFrag.addStudent(name);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
