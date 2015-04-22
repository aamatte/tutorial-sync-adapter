package com.example.andres.myapplication.SyncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
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
import android.util.Log;

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
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by andres on 19-04-15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    String students;
    String token;
    AccountManager mAccountManager;

    public static final String url= "https://radiant-savannah-9544.herokuapp.com/api/students";




    public SyncAdapter (Context context, boolean autoInitialize, String students){
        super(context, autoInitialize);
        Log.i("SyncAdapter", "SyncAdapter initialized");
        this.mContentResolver = context.getContentResolver();
        this.students = students;
        mAccountManager = AccountManager.get(context);

    }


    // Llamando cuando se sincroniza
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


        try {
            Log.i("SyncAdapter", "Performing sync");
            token = mAccountManager.blockingGetAuthToken(account, "normal" , true);
            ArrayList<String> results = performRequest(url, "GET");
            Log.i("SyncAdapter", "Result OK");
            // ejecutado con metodo GET
            if (results.size() == 1){
                Log.i("SyncAdapter", "Update data");

                updateData(results, "GET");
            }
            // ejecutado con POST
            else{

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        }
    }
    // Getting or posting data from/to cloud
    public ArrayList<String> performRequest(String urlString, String modo) throws IOException {
        Log.i("SyncAdapter", "Entering performRequest");
        URL url;
        HttpURLConnection urlConnection = null;

        ArrayList<String> responses = new ArrayList<String>();
        try {
            url = new URL(urlString);


            if (modo.compareTo("GET") == 0){
                Log.i("SyncAdapter", "Entering GET");
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("SyncAdapter", "connection opened");

                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization", "Token token=" + token);
                Log.i("SyncAdapter", "token setted "+token);

                InputStream is;

                int responseCode = urlConnection.getResponseCode();
                Log.i("SyncAdapter", "Response code " + responseCode);


                is = urlConnection.getInputStream();

                InputStreamReader isr = new InputStreamReader(is);


                BufferedReader reader = new BufferedReader(isr);

                String response = "";

                String line;


                while ((line = reader.readLine()) != null) {
                    Log.i("SyncAdapter", line + "\n");
                    response += line + "\n";
                }
                reader.close();
                urlConnection.disconnect();

                responses.add(response);

                return responses;
            }
            else if (modo.compareTo("POST") == 0){
                JSONArray jsonArray = new JSONArray(students);

                for (int i = 0; i< jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");

                    OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(object.toString());
                    wr.flush();

                    int responsecode = urlConnection.getResponseCode();

                    if (responsecode==201){
                        BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                        String output= "";
                        String a;
                        while ((a = br.readLine()) != null) {
                            output += a;
                        }

                        responses.add(output);
                    }



                }
                return responses;
            }

        }

        catch (UnknownHostException e) {


        } catch (Exception ex) {


        } finally {
            if (urlConnection != null)  urlConnection.disconnect();
        }

        return responses;

    }


    /*
     Processes data coming from the cloud, POST or GET, and upgrade the corresponding values calling
     corresponding methods.
  */
    protected void updateData(ArrayList<String> responses, String modo) throws JSONException, RemoteException, OperationApplicationException {

        if (modo.compareTo("POST")==0){

            if (responses == null) return;

            for (int i=0; i<responses.size(); i++) {

            }

        }

        else{

            if (responses.size()>0){

                String response = responses.get(0);
                // Delete all and update with this data

                JSONArray jsonArray = new JSONArray(response);
                Uri uri = StudentsContract.Columns.CONTENT_URI;

                Cursor c = mContentResolver.query(StudentsContract.Columns.CONTENT_URI, null, null, null, StudentsContract.Columns.DEFAULT_SORT_ORDER);
                ArrayList<String> students = new ArrayList<String>();

                c.moveToFirst();

                while (c.moveToNext()){
                    String names = c.getString(c.getColumnIndexOrThrow(StudentsContract.Columns.NAMES));
                    String firstLast = c.getString(c.getColumnIndexOrThrow(StudentsContract.Columns.FIRST_LASTNAME));
                    String secondLast = c.getString(c.getColumnIndexOrThrow(StudentsContract.Columns.SECOND_LASTNAME));
                    students.add(firstLast + " " + secondLast + " " + names);
                }


                for (int i=0; i< jsonArray.length(); i++){
                    JSONObject jso = jsonArray.getJSONObject(i);
                    String name = jso.getString("name");
                    String fln = jso.getString("first_lastname");
                    String sln = jso.getString("second_lastname");
                    int idCloud = jso.getInt("id");
                    if (!students.contains(fln + " " + sln + " " + name)){
                        ContentValues values = new ContentValues();
                        values.put(StudentsContract.Columns.NAMES, name);
                        values.put(StudentsContract.Columns.FIRST_LASTNAME, fln);
                        values.put(StudentsContract.Columns.SECOND_LASTNAME, sln);
                        values.put(StudentsContract.Columns.ID_CLOUD, idCloud);

                        mContentResolver.insert(uri , values);
                    }

                }

            }
        }
    }


}
