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

public class FragmentWishlist extends Fragment implements WishlistInterface{
    private SeznamiDataSource seznamids;
    private FilmiDataSource filmids;
    private PotrebnoSinhroniziratDataSource sinhds;    View view = null;
    private ArrayList<Film> zeljeniFilmi = null;
    private boolean prijavljen=false;
    private boolean registriran=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_seznami_fragment_wishlist, container, false);

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
        ;

        GetWishlistTask task = new GetWishlistTask();
        task.execute();

        return view;

    }

    private class GetWishlistTask extends AsyncTask<String, Void, ArrayList<Film>> {
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
            zeljeniFilmi = new ArrayList<>();
            zeljeniFilmi=seznamids.pridobiWishListo();

           /* Film f = new Film();
            f.setNaslov("title1"); f.setKategorije("Comedy, Science fiction, Drama, Action"); f.setIdFilmApi(140607);
            zeljeniFilmi.add(f);

            Film c = new Film();
            c.setNaslov("title2"); c.setKategorije("Comedy, Science fiction, Drama, Action, Fantasy"); c.setIdFilmApi(206647);
            zeljeniFilmi.add(c);
*/
            return zeljeniFilmi;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> result) {
           // if(pDialog.isShowing())
            //    pDialog.dismiss();
            //izpis rezultatov
          //  if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.listWishlist);
                SeznamiWishlistAdapter swa = new SeznamiWishlistAdapter(getActivity(), zeljeniFilmi, FragmentWishlist.this);
                listView.setAdapter(swa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = zeljeniFilmi.get(position).getIdFilmApi();

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", (int) idFilma);
                        startActivity(myIntent);

                    }
                });
          //  }

        }
    }

    @Override
    public View remove(int idFilma, int idFilmaApi) {
        //async task za zbrisat film iz seznama, in pridobit cel seznam ter ga prikaati (isti postExecute kot pri getOGledaniTask
        System.out.println("remove" + idFilma);
        seznamids.odstraniSSeznama(idFilma, "wish");
        Film f= filmids.pridobiFilm(idFilmaApi);
        Log.i("film", "id" + f.getIdFilmApi());

        if(prijavljen) {
            //preveri če ma internetno povezavo
            DeleteTask dt = new DeleteTask(getActivity(), "3", idFilmaApi+"");
        }
        else{
            if(registriran)
                sinhds.dodajSeznami(filmids.pridobiFilm(idFilmaApi), "3", "odstrani");
        }
        GetWishlistTask task = new GetWishlistTask();
        task.execute();
        return view;
    }

    @Override
    public void putOnOgledani(int idFilma) {
        //zbrisi iz wishlist, dodaj na ogledani, nato pridobiti celoten wishlist in prikazati..
        System.out.println("put on ogledani"+idFilma);
    }


}


