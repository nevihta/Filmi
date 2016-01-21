package com.rvir.filmi.filmi.film;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentOpis extends Fragment {
    private Film film;
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_film_fragment_opis, container, false);
        int idFilma = getActivity().getIntent().getExtras().getInt("id");
        System.out.println(idFilma);

        String url1 = "https://api.themoviedb.org/3/movie/"+idFilma+"?api_key=be86b39865e582aa63d877d88266bcfc&append_to_response=videos";
        String urlIgralci = "https://api.themoviedb.org/3/movie/"+idFilma+"/credits?api_key=be86b39865e582aa63d877d88266bcfc";

        GetJSONOpisTask task = new GetJSONOpisTask();
        task.execute(url1, urlIgralci);

        return view;

    }

    private class GetJSONOpisTask extends AsyncTask<String, Void, Film> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Film doInBackground(String... urls) {
            FilmJSONParser filmParser = new FilmJSONParser();
            try {
                String input = new ServiceHandler().downloadURL(urls[0]);
                film =  filmParser.parse(input);
                String input2 = new ServiceHandler().downloadURL(urls[1]);
                Film film2 = filmParser.parseIgralciRezija(input2);
                film.setIgralci(film2.getIgralci());
                film.setReziserji(film2.getReziserji());

                return film;
            } catch (IOException e) {
            }

            return film;
        }

        @Override
        protected void onPostExecute(Film result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            //izpis rezultatov
            if(result!=null) {
               //izpis
                TextView textView = ( TextView ) view.findViewById(R.id.textView);
                textView.setText(film.getOpis());
            }

        }
    }

}
