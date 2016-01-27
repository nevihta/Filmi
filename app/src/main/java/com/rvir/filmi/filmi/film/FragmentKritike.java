package com.rvir.filmi.filmi.film;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Kritika;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.baza.beans.Seznami;
import com.rvir.filmi.baza.beans.Uporabniki;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.baza.sqlLite.SeznamiDataSource;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class FragmentKritike extends Fragment {
    private Film film;
    private FilmiDataSource filmids;
    private SeznamiDataSource seznamids;
    View view = null;
    Boolean prijavljen=false;
    int idFilma;
    private ProgressDialog pDialog;
    private MobileServiceClient mClient;
    private MobileServiceTable<Kritika> mKritikaTable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_film_fragment_kritike, container, false);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null) {
            prijavljen = true;
        }


        try {         // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                    getContext());
            mKritikaTable = mClient.getTable(Kritika.class);
        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        idFilma = getActivity().getIntent().getExtras().getInt("id");
        System.out.println(idFilma);

        filmids=new FilmiDataSource(getContext());
        filmids.open();

        seznamids= new SeznamiDataSource(getContext());
        seznamids.open();
        String url = "https://api.themoviedb.org/3/movie/"+idFilma+"?api_key=be86b39865e582aa63d877d88266bcfc&append_to_response=reviews";

        GetJSONKritikeTask task = new GetJSONKritikeTask();
        task.execute(url);

        return view;

    }

    private class GetJSONKritikeTask extends AsyncTask<String, Void, Film> {


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
                System.out.println(urls[0]);
                film = filmParser.parseKritike(input);
                //pridobi kritike z baze

                String ocena = filmids.pridobiMojoOceno(film.getIdFilmApi());
                film.setMojaOcena(ocena);


                return film;
            } catch (IOException e) {
            }

            return film;
        }

        @Override
        protected void onPostExecute(Film result) {

            //izpis rezultatov
            if (result.getKritike().size() > 0) {
                //izpis
                ImageView imageView = (ImageView) view.findViewById(R.id.poster);
                Picasso.with(getActivity().getBaseContext())
                        .load(film.getUrlDoSlike())
                        .resize(170, 240)
                        .into(imageView);
                TextView textView = (TextView) view.findViewById(R.id.title);
                textView.setText(film.getNaslov());
                textView = (TextView) view.findViewById(R.id.categories);
                textView.setText(film.getKategorije());
                textView = (TextView) view.findViewById(R.id.year);
                textView.setText("" + film.getLetoIzida());
                textView = (TextView) view.findViewById(R.id.ocena);
                textView.setText("" + film.getOcena());

                if (film.getMojaOcena() != null) {
                    textView = (TextView) view.findViewById(R.id.mojaOcenaLabel);
                    textView.setVisibility(View.VISIBLE);
                    textView = (TextView) view.findViewById(R.id.mojaOcena);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("" + film.getMojaOcena());
                }

                TextView novaKritika = (TextView) view.findViewById(R.id.dodajKritiko);
                novaKritika.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(v.getContext(), AddKritikaActivity.class);
                        myIntent.putExtra("id", (int) film.getIdFilmApi()); //idFIlma ali idFIlmaApi?
                        myIntent.putExtra("naslov", film.getNaslov());
                        startActivity(myIntent);
                    }
                });

                ImageView slikica = (ImageView) view.findViewById(R.id.write);

                Boolean ogl=seznamids.preveriCeJeFilmOgledan(idFilma);

               if((!prijavljen)||(!ogl)){
                   slikica.setVisibility(View.GONE);
                   novaKritika.setVisibility(View.GONE);
               }

                    PridobiKritikeTask task= new PridobiKritikeTask();
                    task.execute(idFilma + "");

                }

            }
        }

    private class PridobiKritikeTask extends AsyncTask<String, Void, MobileServiceList<Kritika>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected MobileServiceList<Kritika> doInBackground(String...strings) {
            MobileServiceList<Kritika> result=null;

            try {
                result= mKritikaTable.where().field("tk_id_filma").eq(strings[0]).execute().get();

            } catch (Exception e) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(MobileServiceList<Kritika> krit) {
            if (pDialog.isShowing())
                pDialog.dismiss();


            ArrayList<Kritika> kr=film.getKritike();
            for(int i=0; i<krit.size(); i++)
                kr.add(0, krit.get(i));

            //prva kritika na seznamu naj bo od uporabnika (ce obstaja seveda)
            ListView listView = (ListView) view.findViewById(R.id.listKritike);
            KritikeAdapter ka = new KritikeAdapter(getActivity(), kr);
            listView.setAdapter(ka);


        }
    }

    }
