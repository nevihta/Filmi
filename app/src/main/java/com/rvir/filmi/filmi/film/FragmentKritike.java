package com.rvir.filmi.filmi.film;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class FragmentKritike extends Fragment {
    private Film film;
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_film_fragment_kritike, container, false);
        int idFilma = getActivity().getIntent().getExtras().getInt("id");
        System.out.println(idFilma);

        String url = "https://api.themoviedb.org/3/movie/"+idFilma+"?api_key=be86b39865e582aa63d877d88266bcfc&append_to_response=reviews";

        GetJSONKritikeTask task = new GetJSONKritikeTask();
        task.execute(url);

        return view;

    }

    private class GetJSONKritikeTask extends AsyncTask<String, Void, Film> {
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
                System.out.println(urls[0]);
                film =  filmParser.parseKritike(input);

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
            if(result.getKritike().size()>0) {
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

                TextView novaKritika = (TextView) view.findViewById(R.id.dodajKritiko);
                novaKritika.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), AddKritikaActivity.class);
                     myIntent.putExtra("id", (int) film.getIdFilmApi()); //idFIlma ali idFIlmaApi??
                     startActivity(myIntent);
                    }
                });

                //kritike

                ListView listView = ( ListView ) view.findViewById(R.id.listKritike);
                KritikeAdapter ka = new KritikeAdapter(getActivity(), film.getKritike());
                listView.setAdapter(ka);
            }

        }
    }

}
