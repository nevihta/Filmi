package com.rvir.filmi.filmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHandler {

    public ServiceHandler() {

    }

    public String downloadURL(String urlString) throws IOException {
        HttpURLConnection conn = null;
        InputStream input = null;
        String result = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            input = conn.getInputStream();
            result = stringFromInputStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(input!=null)
                input.close();
            conn.disconnect();
        }

        return result;

    }

    private String stringFromInputStream(InputStream input){
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }



}
