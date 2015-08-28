package com.example.andres.myapplication.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andres.myapplication.Fragments.AddStudentDialogFragment;
import com.example.andres.myapplication.Fragments.ListFragment;
import com.example.andres.myapplication.Fragments.StudentFragment;
import com.example.andres.myapplication.Model.Item;
import com.example.andres.myapplication.Model.Student;
import com.example.andres.myapplication.Provider.StudentsContract;
import com.example.andres.myapplication.R;

public class MainActivity extends ActionBarActivity implements ListFragment.OnFragmentInteractionListener,  AddStudentDialogFragment.NoticeDialogListener {

    public static final String CODE_NAME = "name";
    public static final String ACCOUNT_TYPE = "com.example.andres.myapplication";
    AccountManager accountManager;
    Account mAccount;
    public static String token = "";

    private AccountManagerCallback<Bundle> mGetAuthTokenCallback = new AccountManagerCallback<Bundle>() {
        @Override
        public void run(final AccountManagerFuture<Bundle> arg0) {
            try {
                token = (String) arg0.getResult().get(AccountManager.KEY_AUTHTOKEN);
            } catch (Exception e) {
                // handle error
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentResolver mResolver = getContentResolver();

        accountManager = (AccountManager) getSystemService(
                ACCOUNT_SERVICE);

        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length == 0){
            // También se puede llamar a metodo accountManager.addAcount(...)
            Intent intent = new Intent(this, AuthenticatorActivity.class);
            intent.putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
            startActivity(intent);
        }
        else{
            mAccount = accounts[0];
            accountManager.getAuthToken(mAccount, "normal", null, this, mGetAuthTokenCallback, null);

            mResolver.setIsSyncable(mAccount, StudentsContract.AUTHORITY, 1);
            mResolver.setSyncAutomatically(mAccount, StudentsContract.AUTHORITY , true);
        }


        TableObserver observer = new TableObserver(null);
        /*
         * Registra el obsever para students
         */
        mResolver.registerContentObserver(StudentsContract.STUDENTS_URI, true, observer);

        setContentView(R.layout.activity_main);

    }

    public class TableObserver extends ContentObserver {
        /**
         * Crea un content observer
         *
         */
        public TableObserver(Handler handler) {
            super(handler);
        }

        /*
         * Define el método que es llamado cuando los datos en el content provider cambian.
         * Este método es solo para que haya compatibilidad con plataformas más viejas.
        */
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }
        /*
         * Define el método que es llamado cuando los datos en el content provider cambian.
         */
        @Override
        public void onChange(boolean selfChange, Uri changeUri) {

            if (mAccount != null) {
                // Corre la sincronizacion
                ContentResolver.requestSync(mAccount, StudentsContract.AUTHORITY, null);
            }
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
            AddStudentDialogFragment dialog = new AddStudentDialogFragment();
            dialog.show(this.getFragmentManager(), "dialog");
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * Opens a new activity with a student_fragments associated that displays the name of the student
     * @param item selected
     */
    @Override
    public void onFragmentInteractionList(Item item) {

        String name = item.getmTexto();

        StudentFragment studentFrag = (StudentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.student_fragment);

        if (studentFrag != null && studentFrag.isVisible()){
                studentFrag.setName(item.getmTexto());
        }
        else{
            Intent intent = new Intent(this,StudentActivity.class );
            intent.putExtra(CODE_NAME, name);
            startActivity(intent);
        }

    }


    @Override
    public void onGetStudentsFromCloud() {
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Student student) {
        ListFragment listFrag = (ListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        if (listFrag == null) return;

        listFrag.addStudent(student);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }



}
