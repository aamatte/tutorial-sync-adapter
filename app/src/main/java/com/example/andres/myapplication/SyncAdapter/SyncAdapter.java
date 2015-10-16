package com.example.andres.myapplication.SyncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

import com.example.andres.myapplication.Authenticator.AccountAuthenticator;
import com.example.andres.myapplication.Model.Student;
import com.example.andres.myapplication.Networking.Request;
import com.example.andres.myapplication.Provider.StudentsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Clase SyncAdapter que implementa los métodos relevantes para la sincronzación.
 * El método {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)}
 * es el más relevante, ya que este es el que se llama cuando se va a realizar la sincronización.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver mContentResolver;
    private String mToken;
    private AccountManager mAccountManager;

    public SyncAdapter (Context context, boolean autoInitialize){
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }

    /**
     * Llamado cuando se sincroniza. Acá se debe hacer el llamado al servidor y la
     * actualizacion de datos locales.
     * @param account
     * @param extras
     * @param authority
     * @param provider
     * @param syncResult
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        try {
            // No importa que el thread se bloquee ya que es asincrónico.
            mToken = mAccountManager.blockingGetAuthToken(account,
                    AccountAuthenticator.ACCOUNT_TYPE, true);

            Request request = Request.createRequest(Request.GET);
            ArrayList<String> results = request.perform(mToken);

            if (results.size() == 1){
                updateData(results);
            }
        // Manejo de errores
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes data coming from the cloud, POST or GET, and upgrade the corresponding values
     * calling corresponding methods.
     */
    protected void updateData(ArrayList<String> responses)
            throws JSONException, RemoteException, OperationApplicationException {
        if (responses == null) return;
        if (responses.size()>0){
            String response = responses.get(0);
            JSONArray jsonArray = new JSONArray(response);
            Uri uri = StudentsContract.STUDENTS_URI;
            Cursor cursor = mContentResolver.query(StudentsContract.STUDENTS_URI,
                    null, null, null, null);
            ArrayList<String> students = new ArrayList<>();
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String names = cursor.getString(cursor.getColumnIndexOrThrow(
                        StudentsContract.StudentsColumns.NAMES));
                String firstLast = cursor.getString(cursor.getColumnIndexOrThrow(
                        StudentsContract.StudentsColumns.FIRST_LASTNAME));
                String secondLast = cursor.getString(cursor.getColumnIndexOrThrow(
                        StudentsContract.StudentsColumns.SECOND_LASTNAME));
                students.add(firstLast + " " + secondLast + " " + names);
            }
            for (int i=0; i< jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString(Student.NAME);
                String firstLastname = jsonObject.getString(Student.FIRST_LASTNAME);
                String secondLastname = jsonObject.getString(Student.SECOND_LASTNAME);
                int idCloud = jsonObject.getInt(Student.ID);
                if (!students.contains(firstLastname + " " + secondLastname + " " + name)){
                    ContentValues values = new ContentValues();
                    values.put(StudentsContract.StudentsColumns.NAMES, name);
                    values.put(StudentsContract.StudentsColumns.FIRST_LASTNAME, firstLastname);
                    values.put(StudentsContract.StudentsColumns.SECOND_LASTNAME, secondLastname);
                    values.put(StudentsContract.StudentsColumns.ID_CLOUD, idCloud);
                    mContentResolver.insert(uri , values);
                }
            }
            cursor.close();
        }
    }
}
