package com.rvir.filmi.filmi.filmi;

import android.util.Log;

import com.rvir.filmi.baza.beans.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularniJSONParser {
    private ArrayList<Film> popularniFilmi = null;

    public PopularniJSONParser(){

    }

    public ArrayList<Film> parse(String inputString)  {
        popularniFilmi = new ArrayList<Film>();

        try {
            JSONObject jObj = new JSONObject(inputString);
            JSONArray jArray = jObj.getJSONArray("results");
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                Film film = new Film();
                film.setIdFilmApi(jObject.getInt("id"));
                film.setNaslov(jObject.getString("title"));
                film.setUrlDoSlike("http://image.tmdb.org/t/p/w500" + jObject.getString("poster_path"));
                film.setOcena(jObject.getString("vote_average"));
                popularniFilmi.add(film);
            }

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }

        return popularniFilmi;
    }
}
