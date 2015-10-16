package com.example.andres.myapplication.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class GetRequest extends Request {

    /**
     * Perform a GET request to {@link #API_URL}.
     * @param token Authorization token
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
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(GET);
            urlConnection.setRequestProperty(AUTHORIZATION, TOKEN_HEADER + token);
            InputStream is;

            int responseCode = urlConnection.getResponseCode();

            is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String response = "";
            String line;
            while ((line = reader.readLine()) != null) {
                response += line + "\n";
            }
            reader.close();
            urlConnection.disconnect();
            responses.add(response);
            return responses;
        }
        catch (Exception ex) { }
        finally {
            if (urlConnection != null)  urlConnection.disconnect();
        }
        return responses;
    }
}
