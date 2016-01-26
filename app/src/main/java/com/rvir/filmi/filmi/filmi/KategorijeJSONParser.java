package com.rvir.filmi.filmi.filmi;

import android.util.Log;

import com.rvir.filmi.baza.beans.Kategorija;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KategorijeJSONParser {
    private ArrayList<Kategorija> kategorije = null;

    public KategorijeJSONParser(){

    }

    public ArrayList<Kategorija> parse(String inputString)  {
        kategorije = new ArrayList<Kategorija>();

        try {
            JSONObject jObj = new JSONObject(inputString);
            JSONArray jArray = jObj.getJSONArray("genres");
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                Kategorija k = new Kategorija();
                k.setIdKategorije(Integer.parseInt(jObject.get("id").toString()));
                k.setNaziv(jObject.getString("name"));
                kategorije.add(k);
            }

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }

        return kategorije;
    }

}
