package com.rvir.filmi.filmi.seznami;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class FragmentPriljubljeni extends Fragment implements PriljubljeniInterface {
    private SeznamiDataSource seznamids;
    private FilmiDataSource filmids;
    private PotrebnoSinhroniziratDataSource sinhds;
    View view = null;
    private ArrayList<Film> priljubljeniFilmi = null;
    private boolean prijavljen=false;
    private boolean registriran=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_seznami_fragment_priljubljeni, container, false);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null)
            prijavljen=true;

        filmids=new FilmiDataSource(getContext());

        seznamids = new SeznamiDataSource(getContext());

        sinhds=new PotrebnoSinhroniziratDataSource(getContext());
        sinhds.open();
        registriran=sinhds.registriran();

        GetPriljubljeniTask task = new GetPriljubljeniTask();
        task.execute();

        return view;

    }

    private class GetPriljubljeniTask extends AsyncTask<String, Void, ArrayList<Film>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            filmids.open();
            seznamids.open();

        }

        @Override
        protected ArrayList<Film> doInBackground(String... urls) {
            //iz baze filme
            priljubljeniFilmi = new ArrayList<>();
            priljubljeniFilmi=seznamids.pridobiPriljubljene();
            /*
            Film f = new Film();
            f.setNaslov("title1"); f.setKategorije("Comedy, Science fiction, Drama, Action"); f.setIdFilmApi(140607);
            priljubljeniFilmi.add(f);

            Film c = new Film();
            c.setNaslov("title2"); c.setKategorije("Comedy, Science fiction, Drama, Action, Fantasy"); c.setIdFilmApi(206647);
            priljubljeniFilmi.add(c);*/

            return priljubljeniFilmi;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> result) {
           // if(pDialog.isShowing())
            //    pDialog.dismiss();
            //izpis rezultatov
           // if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.listPriljubljeni);
                SeznamiPriljubljeniAdapter spa = new SeznamiPriljubljeniAdapter(getActivity(), priljubljeniFilmi, FragmentPriljubljeni.this);
                listView.setAdapter(spa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = priljubljeniFilmi.get(position).getIdFilmApi();

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", (int) idFilma);
                        startActivity(myIntent);

                    }
                });
           // }

        }
    }

    @Override
    public View remove(int idFilma, int idFilmaApi) {
        //async task za zbrisat film iz seznama, in pridobit cel seznam ter ga prikazati (isti postExecute kot pri getOGledaniTask
        System.out.println("remove" + idFilma);
        seznamids.odstraniSSeznama(idFilma, "priljubljen");

        if(prijavljen) {
            //preveri če ma internetno povezavo
            DeleteTask dt = new DeleteTask(getActivity(), "2", idFilmaApi+"");
        }
        else{
            if(registriran)
                sinhds.dodajSeznami(filmids.pridobiFilm(idFilmaApi), "2", "odstrani");
        }
        seznamids.close();
        filmids.close();

        GetPriljubljeniTask task = new GetPriljubljeniTask();
        task.execute();
        return view;

    }


}
