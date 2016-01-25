package com.rvir.filmi.filmi.film;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.SeznamAzure;
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

public class FragmentOpis extends Fragment {
    private Film film;
    private ImageView priljubljeno;
    private ImageView ogledano;
    private ImageView neOgledano;
    private ImageView wishlist;
    private FilmiDataSource filmids;
    private SeznamiDataSource seznamids;
    Boolean baza;
    int id=0;
    private boolean prijavljen=false;
    private String idUp;

    private MobileServiceClient mClient;
    private MobileServiceTable<SeznamAzure> mSeznamiTable;

    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_film_fragment_opis, container, false);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null){
            prijavljen=true;
            idUp=sharedpreferences.getString("idUporabnika", null);
        }



        final int idFilma = getActivity().getIntent().getExtras().getInt("id");
        System.out.println(idFilma);

        filmids = new FilmiDataSource(getContext());
        seznamids = new SeznamiDataSource(getContext());

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


        String seznam;
        Boolean wish=false;
        Boolean ogledan=false;
        Boolean priljubljen=false;

        baza=true;

        seznamids.open();
        filmids.open();
        film=filmids.pridobiFilm(idFilma);

        if(film == null){
            baza=false;
            Log.i("film", "ni ga v bazi");
            String url1 = "https://api.themoviedb.org/3/movie/"+idFilma+"?api_key=be86b39865e582aa63d877d88266bcfc&append_to_response=videos";
            String urlIgralci = "https://api.themoviedb.org/3/movie/"+idFilma+"/credits?api_key=be86b39865e582aa63d877d88266bcfc";

            GetJSONOpisTask task = new GetJSONOpisTask();
            task.execute(url1, urlIgralci);
        }
        else
        {
            napolniView(film);
            id=film.getIdFilma();

            ArrayList<String> seznami = seznamids.vrniSeznameNaKaterihJeFilm(id);
            Log.i("seznami", seznami.size() + "");

            for(int i=0; i<seznami.size(); i++){
                seznam=seznami.get(i);
                Log.i("seznam:" , seznam);
                if (seznam.equals("wish"))
                    wish=true;
                else if(seznam.equals("ogledan"))
                    ogledan=true;
                else if (seznam.equals("priljubljen"))
                    priljubljen=true;
            }
        }


        //add favourites
        //manjka: if film ze na seznamu, slika x, else slika y
        priljubljeno = (ImageView) view.findViewById(R.id.fave);
        if(priljubljen){
            priljubljeno.setImageResource(R.drawable.filmi_heart);
            priljubljeno.setTag("fave");
        }
        else{
            priljubljeno.setImageResource(R.drawable.filmi_heart_empty);
            priljubljeno.setTag("notFave");
        }

        //listener - odstrani ali doda na seznam + spremeni ikono
        priljubljeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(priljubljeno.getTag().equals("fave")){
                   priljubljeno.setImageResource(R.drawable.filmi_heart_empty);
                   priljubljeno.setTag("notFave");
                   seznamids.odstraniSSeznama(id, "priljubljen");
                   if(prijavljen) {
                       //if povezava
                        SinhronizacijaTask task = new SinhronizacijaTask();
                        task.execute("odstrani", "2", film.getIdFilmApi() + "");
                    }
               }
                else{
                   priljubljeno.setImageResource(R.drawable.filmi_heart);
                   priljubljeno.setTag("fave");
                   if (!baza)
                       id=filmids.dodajFilm(film);
                   seznamids.dodajNaSeznam(id, "priljubljen");
                   if(prijavljen) {
                       //if povezava
                       SinhronizacijaTask task = new SinhronizacijaTask();
                       task.execute("dodaj", "2", film.getIdFilmApi() + "", film.getNaslov());
                   }
               }
            }
        });

        //ze ogledano - manjka: ce na listi potem x visible, y invis, drugace obratno
        ogledano = (ImageView) view.findViewById(R.id.watched);
        neOgledano = (ImageView) view.findViewById(R.id.notWatched);

        if(ogledan){
            neOgledano.setVisibility(View.GONE);
        }
        else{
            ogledano.setVisibility(View.INVISIBLE);
        }

        //listener - samo, ce doda na seznam ogledanih
        neOgledano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("v neogledano je", "T");
                neOgledano.setVisibility(View.GONE);
                ogledano.setVisibility(View.VISIBLE);
                if (!baza)
                    id=filmids.dodajFilm(film);
                seznamids.dodajNaSeznam(id, "ogledan");
                if(prijavljen) {
                    //if povezava
                    SinhronizacijaTask task = new SinhronizacijaTask();
                    task.execute("dodaj", "1", film.getIdFilmApi() + "", film.getNaslov());
                    wishlist.setVisibility(View.GONE);
                }

            }
        });

        //wishlist - ce je na seznamu ogledanih, ne sme biti prikazano!
        wishlist = (ImageView) view.findViewById(R.id.wish);
        if(ogledan){
            wishlist.setVisibility(View.GONE);
        }else{
            //ce je na wishlisti ali ne
            if(wish){
                wishlist.setImageResource(R.drawable.filmi_star);
                wishlist.setTag("wish");
            }
            else{
                wishlist.setImageResource(R.drawable.filmi_star_empty);
                wishlist.setTag("notWish");
            }

        }

        //listener - odstrani ali doda na seznam + spremeni ikono
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wishlist.getTag().equals("wish")){
                    wishlist.setImageResource(R.drawable.filmi_star_empty);
                    wishlist.setTag("notWish");
                    seznamids.odstraniSSeznama(id, "wish");
                    if (prijavljen) {
                        //if povezava
                        SinhronizacijaTask task = new SinhronizacijaTask();
                        task.execute("odstrani", "3", film.getIdFilmApi() + "");
                    }
                }
                else {
                    wishlist.setImageResource(R.drawable.filmi_star);
                    wishlist.setTag("wish");
                    if (!baza)
                        id = filmids.dodajFilm(film);
                    seznamids.dodajNaSeznam(id, "wish");
                    if (prijavljen) {
                        //if povezava
                        SinhronizacijaTask task = new SinhronizacijaTask();
                        task.execute("dodaj", "3", film.getIdFilmApi() + "", film.getNaslov());
                    }
                }
            }
        });

        Log.i("id filma", ""+ id);
        //recommend to friend
        ImageView share = (ImageView) view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send recommendation
            }
        });


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
                napolniView(result);
            }

        }
    }

    private void napolniView(Film film){

            ImageView imageView = (ImageView) view.findViewById(R.id.poster);
            Picasso.with(getActivity().getBaseContext())
                        .load(film.getUrlDoSlike())
                        .resize(170,240)
                    .into(imageView);
            TextView textView = ( TextView ) view.findViewById(R.id.title);
        textView.setText(film.getNaslov());
            textView = ( TextView ) view.findViewById(R.id.categories);
        textView.setText(film.getKategorije());
            textView = ( TextView ) view.findViewById(R.id.year);
        textView.setText(""+film.getLetoIzida());
            textView = ( TextView ) view.findViewById(R.id.ocena);
        textView.setText(""+film.getOcena());
            textView = ( TextView ) view.findViewById(R.id.rezija); System.out.println("kat: " + film.getIgralci());
        textView.setText(film.getReziserji());
            textView = ( TextView ) view.findViewById(R.id.igralci);
        textView.setText(film.getIgralci());
            textView = ( TextView ) view.findViewById(R.id.opis);
        textView.setText(film.getOpis());

                //video =/
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


               idUp="5555";

                if(argumenti[0].equals("dodaj")){
                    SeznamAzure s = new SeznamAzure();
                    s=new SeznamAzure(Integer.parseInt(argumenti[1]), idUp, Integer.parseInt(argumenti[2]),argumenti[3]);
                    mSeznamiTable.insert(s);
                    if(argumenti[1].equals("1")){
                        MobileServiceList<SeznamAzure> sa;
                        sa=mSeznamiTable.where().field("tk_id_tipa").eq(3).and().field("tk_id_filma").eq(argumenti[2]).and().field("tk_id_uporabnika").eq(idUp).execute().get();
                        mSeznamiTable.delete(sa.get(0));
                    }

                }
                else if(argumenti[0].equals("odstrani")){
                    MobileServiceList<SeznamAzure> s;
                    s=mSeznamiTable.where().field("tk_id_tipa").eq(argumenti[1]).and().field("tk_id_filma").eq(argumenti[2]).and().field("tk_id_uporabnika").eq(idUp).execute().get();
                    mSeznamiTable.delete(s.get(0));
                }



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
