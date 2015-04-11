package com.example.andres.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SyncService extends Service {

    private final IBinder mBinder = new LocalBinder();

    public SyncService() {
    }

    /**
     * Clase usada por el cliente Binder.
     */
    public class LocalBinder extends Binder {
        public SyncService getService(){
            // Returns the SyncService isntance. With this clients can call the public methods.
            return SyncService.this;
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Get JSON with students in app web DB
     */
    public JSONArray getStudents() throws JSONException, ExecutionException, InterruptedException {

        String jsonResponse = (new RequestTask()).execute("http://radiant-savannah-9544.herokuapp.com/students.json").get();
        JSONArray jsonArray = new JSONArray(jsonResponse);
        return jsonArray;
    }

    /**
     * Add student to the web app DB if not added
     */
    public void addStudents(ArrayList<String> students){
    // TODO: HACER ESTOOOOOOOOO

    }

    private class RequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String archivo = performRequest(params[0]);
            return archivo;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
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
