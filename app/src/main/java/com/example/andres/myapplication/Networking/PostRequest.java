package com.example.andres.myapplication.Networking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class PostRequest extends Request {

    /**
     * Perform a POST request to {@link #API_URL}.
     * @param token Authorization token.
     * @return String responses.
     * @throws IOException
     */
    @Override
    public ArrayList<String> perform(String token) throws IOException {
        URL url;
        HttpURLConnection urlConnection = null;

        ArrayList<String> responses = new ArrayList<>();
        try {
            url = new URL(API_URL);
            JSONArray jsonArray = new JSONArray("");

            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
                urlConnection.setRequestProperty(ACCEPT, APPLICATION_JSON);
                urlConnection.setRequestProperty(AUTHORIZATION, TOKEN_HEADER + token);
                urlConnection.setRequestMethod(POST);

                OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(object.toString());
                wr.flush();

                int responsecode = urlConnection.getResponseCode();

                if (responsecode==201){
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader((urlConnection.getInputStream())));
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
        catch (Exception ex) { }
        finally {
            if (urlConnection != null)  urlConnection.disconnect();
        }
        return responses;
    }
}
