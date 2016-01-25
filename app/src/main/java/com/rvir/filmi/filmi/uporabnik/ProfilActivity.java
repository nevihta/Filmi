package com.rvir.filmi.filmi.uporabnik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;
import com.rvir.filmi.filmi.seznami.SeznamiActivity;

public class ProfilActivity extends AppCompatActivity {
Film film = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.filmi_home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        GetProfilTask task = new GetProfilTask();
        task.execute();


    }

    private class GetProfilTask extends AsyncTask<String, Void, Film> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ProfilActivity.this);
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Film doInBackground(String... urls) {
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
            if(sharedpreferences.getString("idUporabnika", null)!=null){
               // int idUporabnik = sharedpreferences.getString("idUporabnika", null);
            }
            //pridobi: kritike, + one 3 sezname

            return film;
        }

        @Override
        protected void onPostExecute(Film result) {
            if(pDialog.isShowing())
                pDialog.dismiss();

            //izpis rezultatov
            //priljubljeni - 2 filma - ce jih nima - set gone view
            TextView fave1 = (TextView) findViewById(R.id.fave1);
            fave1.setText("film1");
            fave1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                   int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */               }
            });

            TextView fave2 = (TextView) findViewById(R.id.fave2);
            fave2.setText("film2");
            fave2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                   int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */               }
            });

            //ogledani - isto kot priljubljeni
            TextView watched1 = (TextView) findViewById(R.id.watched1);
            watched1.setText("film1");
            watched1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                   int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */               }
            });
            TextView watched2 = (TextView) findViewById(R.id.watched2);
            watched2.setText("film2");
            watched2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                   int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */               }
            });

            //wishlist - isto kot prej
            TextView wish1 = (TextView) findViewById(R.id.wish1);
            wish1.setText("film1");
            wish1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                   int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */               }
            });

            TextView wish2 = (TextView) findViewById(R.id.wish2);
            wish2.setText("film2");
            wish2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                   int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */               }
            });

            //kritike?? manjka stran...

            //nastavit na View.gone ce ni dovolj itemov v seznamu
            TextView moreFave = (TextView) findViewById(R.id.moreFave);
            moreFave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), SeznamiActivity.class);
                    myIntent.putExtra("OpenTab", 0);
                    startActivity(myIntent);
                }
            });

            TextView moreWatched = (TextView) findViewById(R.id.moreWatched);
            moreWatched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), SeznamiActivity.class);
                    myIntent.putExtra("OpenTab", 1);
                    startActivity(myIntent);
                }
            });

            TextView moreWish = (TextView) findViewById(R.id.moreWish);
            moreWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), SeznamiActivity.class);
                    myIntent.putExtra("OpenTab", 2);
                    startActivity(myIntent);
                }
            });

            TextView moreKritik = (TextView) findViewById(R.id.moreKritike);
            moreKritik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //manjka stran za vse njegove kritike
                   // Intent myIntent = new Intent(v.getContext(), AddKritikaActivity.class);
                   // myIntent.putExtra("id", (int) film.getIdFilmApi()); //idFIlma ali idFIlmaApi??
                   // startActivity(myIntent);
                }
            });



        }
    }
}
