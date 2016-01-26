package com.rvir.filmi.filmi.priporocila;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;
import com.rvir.filmi.filmi.film.FilmActivity;
import com.rvir.filmi.filmi.filmi.FilmiAdapter;
import com.rvir.filmi.filmi.filmi.PopularniJSONParser;
import com.rvir.filmi.filmi.seznami.DeleteTask;
import com.rvir.filmi.filmi.seznami.PriljubljeniInterface;
import com.rvir.filmi.filmi.seznami.SeznamiPriljubljeniAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentApiPriporocila extends Fragment {
    View view = null;
    private ArrayList<Film> priporoceniFilmi = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_priporocila_fragment_api, container, false);

        //pridobit neki random id filma iz seznama priljubljenih al pa ogledanih, ce nic na seznamu pa kaj?
        String url = "https://api.themoviedb.org/3/movie/140607/similar?api_key=be86b39865e582aa63d877d88266bcfc";

        GetApiPriporoceniTask task = new GetApiPriporoceniTask();
        task.execute(url);

        return view;

    }

    private class GetApiPriporoceniTask extends AsyncTask<String, Void, ArrayList<Film>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Film> doInBackground(String... urls) {
            PopularniJSONParser popularniParser = new PopularniJSONParser();
            try {
                String input = new ServiceHandler().downloadURL(urls[0]);
                priporoceniFilmi =  popularniParser.parse(input);

                return priporoceniFilmi;
            } catch (IOException e) {
            }

            return priporoceniFilmi;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            //izpis rezultatov
            if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.listApiPriporocila);
                FilmiAdapter fa = new FilmiAdapter(getActivity(), priporoceniFilmi);
                listView.setAdapter(fa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = priporoceniFilmi.get(position).getIdFilmApi();

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", (int) idFilma);
                        startActivity(myIntent);

                    }
                });
            }

        }
    }

}
