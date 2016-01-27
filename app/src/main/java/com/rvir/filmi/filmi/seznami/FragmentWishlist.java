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

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.baza.sqlLite.PotrebnoSinhroniziratDataSource;
import com.rvir.filmi.baza.sqlLite.SeznamiDataSource;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class FragmentWishlist extends Fragment implements WishlistInterface{
    private SeznamiDataSource seznamids;
    private FilmiDataSource filmids;
    private PotrebnoSinhroniziratDataSource sinhds;
    View view = null;
    private boolean prijavljen=false;
    private boolean registriran=false;
    private String idUp;
    MobileServiceClient mClient;
    MobileServiceTable<SeznamAzure> mSeznamiTable=null;
    private ArrayList<Film> zeljeniFilmi = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_seznami_fragment_wishlist, container, false);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null) {
            prijavljen = true;
            idUp = sharedpreferences.getString("idUporabnika", null);
        }

        try {
            // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                    getContext());
            mSeznamiTable = mClient.getTable(SeznamAzure.class);

        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }


        filmids=new FilmiDataSource(getContext());
        filmids.open();
        seznamids = new SeznamiDataSource(getContext());
        seznamids.open();
        sinhds=new PotrebnoSinhroniziratDataSource(getContext());
        sinhds.open();
        registriran=sinhds.registriran();

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
            zeljeniFilmi = new ArrayList<>();
            zeljeniFilmi=seznamids.pridobiWishListo();

            return zeljeniFilmi;
        }

        @Override
        protected void onPostExecute(final ArrayList<Film> result) {

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
    public View putOnOgledani(Film f) {
        System.out.println("put on ogledani" + f.getIdFilmApi());
        seznamids.dodajNaSeznam(f.getIdFilma(), "ogledan");
        if (prijavljen) {
            //if povezava
            Log.i("v prijavljen je", "jup");
            SinhronizacijaTask task = new SinhronizacijaTask();
            task.execute("dodaj", "1", f.getIdFilmApi() + "", f.getNaslov(), f.getUrlDoSlike());

        }
        else{
            if(registriran)
                sinhds.dodajSeznami(f, "1", "dodaj");
        }

        GetWishlistTask task = new GetWishlistTask();
        task.execute();
        return view;
    }

    private class SinhronizacijaTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("on preExecte", "");
            // Showing progress dialog
            //pDialog = new ProgressDialog(Registracija.this);
            //pDialog.setMessage("Registracija poteka...");
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... argumenti) {
            try {

                 SeznamAzure s=new SeznamAzure(1, idUp, Integer.parseInt(argumenti[2]),argumenti[3], argumenti[4]);
                 mSeznamiTable.insert(s);

                MobileServiceList<SeznamAzure> sa;
                sa=mSeznamiTable.where().field("tk_id_tipa").eq(3).and().field("tk_id_filma").eq(Integer.parseInt(argumenti[2])).and().field("tk_id_uporabnika").eq(idUp).execute().get();
                mSeznamiTable.delete(sa.get(0));
            } catch (Exception e) { }
            return null;
        }

        @Override
        protected void onPostExecute(String id) {
            //if(pDialog.isShowing())
            //   pDialog.dismiss();

        }
    }
}


