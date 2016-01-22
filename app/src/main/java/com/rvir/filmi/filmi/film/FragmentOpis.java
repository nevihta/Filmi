package com.rvir.filmi.filmi.film;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentOpis extends Fragment {
    private Film film;
    private ImageView priljubljeno;
    private ImageView ogledano;
    private ImageView neOgledano;
    private ImageView wishlist;
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

        //add favourites
        //manjka: if film ze na seznamu, slika x, else slika y
        priljubljeno = (ImageView) view.findViewById(R.id.fave);
        if(idFilma==140607){
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
               }
                else{
                   priljubljeno.setImageResource(R.drawable.filmi_heart);
                   priljubljeno.setTag("fave");
               }
            }
        });

        //ze ogledano - manjka: ce na listi potem x visible, y invis, drugace obratno
        ogledano = (ImageView) view.findViewById(R.id.watched);
        neOgledano = (ImageView) view.findViewById(R.id.notWatched);

        if(idFilma==140607){
            neOgledano.setVisibility(View.GONE);
        }
        else{
            ogledano.setVisibility(View.INVISIBLE);
        }

        //listener - samo, ce doda na seznam ogledanih
        neOgledano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               neOgledano.setVisibility(View.GONE);
                ogledano.setVisibility(View.VISIBLE);
            }
        });

        //wishlist - ce je na seznamu ogledanih, ne sme biti prikazano!
        wishlist = (ImageView) view.findViewById(R.id.wish);
        if(idFilma==12345){
            wishlist.setVisibility(View.GONE);
        }else{
            //ce je na wishlisti ali ne
            if(idFilma==140607){
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
                }
                else{
                    wishlist.setImageResource(R.drawable.filmi_star);
                    wishlist.setTag("wish");
                }
            }
        });

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

        }
    }

}
