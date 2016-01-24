package com.rvir.filmi.filmi.film;

import android.util.Log;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Kategorija;
import com.rvir.filmi.baza.beans.Kritika;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilmJSONParser {
    private Film film = null;

    public FilmJSONParser(){

    }

    public Film parse(String inputString)  {
        film = new Film();

        try {
            JSONObject jObj = new JSONObject(inputString);
            film.setIdFilmApi(jObj.getInt("id")); System.out.println(film.getIdFilmApi());
            film.setNaslov(jObj.getString("title")); System.out.println(film.getNaslov());
            film.setOpis(jObj.getString("overview")); System.out.println(film.getOpis());
            film.setUrlDoSlike("http://image.tmdb.org/t/p/w500" + jObj.getString("poster_path")); System.out.println(film.getUrlDoSlike());
            film.setOcena(jObj.getString("vote_average")); System.out.println(film.getOcena());
            film.setLetoIzida(Integer.parseInt(jObj.getString("release_date").substring(0, 4))); System.out.println(film.getLetoIzida());

            JSONArray jArray = jObj.getJSONArray("genres");
            String kategorije = "";
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                kategorije+=jObject.getString("name")+", ";
            }
            if(kategorije.length()>3) {
                film.setKategorije(kategorije.substring(0, kategorije.length() - 2));
                System.out.println(kategorije);
            }
            else
                film.setKategorije(" ");

            //video
            JSONArray jArray2 = jObj.getJSONObject("videos").getJSONArray("results");
            for(int j=0; j < jArray2.length(); j++) {
                JSONObject jObject2 = jArray2.getJSONObject(j);
                String key = jObject2.getString("key");
                System.out.println("https://www.youtube.com/watch?v="+key);
            }

            //film.setTrailer

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }

        return film;
    }

    public Film parseIgralciRezija(String inputString){
        System.out.println("v igralci/rezija: ");
        String igralci="";
        String rezija="";
        try {
            JSONObject jObj = new JSONObject(inputString);

            JSONArray jArray = jObj.getJSONArray("cast");
            for(int i=0; i < jArray.length() && i < 6; i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                igralci+=jObject.getString("name")+", ";
            }
            if(igralci.length()>3)
                film.setIgralci(igralci.substring(0, igralci.length()-2));
            else
                film.setIgralci(" ");

            //mogoce se direktorja?
            JSONArray jArray2 = jObj.getJSONArray("crew");
            int stReziserjev = 0;
            for(int j=0; j < jArray2.length() && stReziserjev < 3; j++) {
                JSONObject jObject2 = jArray2.getJSONObject(j);
                if(jObject2.getString("job").equals("Producer")) {
                    rezija += jObject2.getString("name") + ", ";
                    stReziserjev+=1;
                }

            }
            if(rezija.length()>3)
                film.setReziserji(rezija.substring(0,rezija.length()-2));
            else
                film.setReziserji(" ");

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }

        return film;
    }

    public Film parseKritike(String inputString){
        film = new Film();
        try {
            JSONObject jObj = new JSONObject(inputString);
            film.setIdFilmApi(jObj.getInt("id")); System.out.println(film.getIdFilmApi());
            film.setNaslov(jObj.getString("title")); System.out.println(film.getNaslov());
            film.setUrlDoSlike("http://image.tmdb.org/t/p/w500" + jObj.getString("poster_path")); System.out.println(film.getUrlDoSlike());
            film.setOcena(jObj.getString("vote_average")); System.out.println(film.getOcena());
            film.setLetoIzida(Integer.parseInt(jObj.getString("release_date").substring(0, 4))); System.out.println(film.getLetoIzida());

            JSONArray jArray = jObj.getJSONArray("genres");
            String kategorije = "";
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                kategorije+=jObject.getString("name")+", ";
            }
            if(kategorije.length()>3) {
                film.setKategorije(kategorije.substring(0, kategorije.length() - 2));
                System.out.println(kategorije);
            }
            else
                film.setKategorije(" ");

            //problem: kaj ce to null? crash crash
            ArrayList<Kritika> kritike = new ArrayList<>();
            JSONArray jArray2 = jObj.getJSONObject("reviews").getJSONArray("results");
            for(int j=0; j < jArray2.length(); j++) {
                JSONObject jObject2 = jArray2.getJSONObject(j);
                Kritika k = new Kritika();
                k.setAvtor(jObject2.getString("author"));
                k.setBesedilo(jObject2.getString("content"));
                kritike.add(k);
            }

            film.setKritike(kritike);

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }

        return film;
    }

}
