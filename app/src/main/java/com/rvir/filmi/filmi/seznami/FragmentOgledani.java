package com.rvir.filmi.filmi.seznami;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.baza.sqlLite.PotrebnoSinhroniziratDataSource;
import com.rvir.filmi.baza.sqlLite.SeznamiDataSource;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;

import java.util.ArrayList;

public class FragmentOgledani extends Fragment implements OgledaniInterface{
    private SeznamiDataSource seznamids;
    private FilmiDataSource filmids;
    private PotrebnoSinhroniziratDataSource sinhds;

    View view = null;
    private ArrayList<Film> ogledaniFilmi = null;
    private boolean prijavljen=false;
    private boolean registriran=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_seznami_fragment_ogledani, container, false);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null)
            prijavljen=true;

        filmids=new FilmiDataSource(getContext());
        filmids.open();

        seznamids = new SeznamiDataSource(getContext());
        seznamids.open();

        sinhds=new PotrebnoSinhroniziratDataSource(getContext());
        sinhds.open();
        registriran=sinhds.registriran();


        GetOgledaniTask task = new GetOgledaniTask();
        task.execute();

        return view;

    }

    private class GetOgledaniTask extends AsyncTask<String, Void, ArrayList<Film>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(getActivity());
            //pDialog.setMessage("Prosimo, počakajte...");
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected ArrayList<Film> doInBackground(String... urls) {
            //iz baze filme
            ogledaniFilmi = new ArrayList<>();
            ogledaniFilmi=seznamids.pridobiOgledane();

          /*  Film f = new Film();
            f.setNaslov("title1"); f.setKategorije("Comedy, Science fiction, Drama, Action"); f.setIdFilmApi(140607);
            ogledaniFilmi.add(f);

            Film c = new Film();
            c.setNaslov("title2"); c.setKategorije("Comedy, Science fiction, Drama, Action, Fantasy"); c.setIdFilmApi(206647);
            ogledaniFilmi.add(c);*/

            return ogledaniFilmi;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> result) {
            //if(pDialog.isShowing())
             //   pDialog.dismiss();
            //izpis rezultatov
            //if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.listOgledani);
                SeznamiOgledaniAdapter spa = new SeznamiOgledaniAdapter(getActivity(), ogledaniFilmi, FragmentOgledani.this);
                listView.setAdapter(spa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = ogledaniFilmi.get(position).getIdFilmApi();

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", (int) idFilma);
                        startActivity(myIntent);

                    }
                });
            //}

        }
    }

    @Override
    public View remove(int idFilma, int idFilmaApi) {
        //async task za zbrisat film iz seznama, in pridobit cel seznam ter ga prikaati (isti postExecute kot pri getOGledaniTask
        System.out.println("remove"+idFilma);
        seznamids.odstraniSSeznama(idFilma, "ogledan");

        if(prijavljen) {
            //preveri če ma internetno povezavo
            DeleteTask dt = new DeleteTask(getActivity(), "1", idFilmaApi+"");
        }
        else{
            if(registriran)
                sinhds.dodajSeznami(filmids.pridobiFilm(idFilmaApi), "1", "odstrani");
        }
        GetOgledaniTask task = new GetOgledaniTask();
        task.execute();

        return view;
    }

    @Override
    public void writeReview(int idFilma) {
        //intent, start activity to write review
        System.out.println("write review"+idFilma);
    }

    @Override
    public void recommend(int idFilma) {
        //intent, start activity to recommend to friend
        System.out.println("recommend"+idFilma);
    }


}
