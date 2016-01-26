package com.rvir.filmi.filmi.film;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.Kritika;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;

import java.net.MalformedURLException;
import java.sql.Timestamp;

public class AddKritikaActivity extends AppCompatActivity  {
    int idFilmaApi = 0;
    String naslovF="";
    RatingBar ratingBar = null;
    Boolean prijavljen=false;
    String idUp;
    String upIme;
    private FilmiDataSource filmids;
    private EditText mnenje;

    private MobileServiceClient mClient;
    private MobileServiceTable<Kritika> mKritikaTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kritika);

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

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null){
            prijavljen=true;
            idUp=sharedpreferences.getString("idUporabnika", null);
            upIme=sharedpreferences.getString("upIme", null);
        }

        filmids=new FilmiDataSource(this);
        filmids.open();

        try {
            // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                     this);
            mKritikaTable= mClient.getTable(Kritika.class);

        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        idFilmaApi = getIntent().getExtras().getInt("id");
        naslovF=getIntent().getExtras().getString("naslov");
        String mojaOcena=getIntent().getExtras().getString("ocena");

        if(mojaOcena==null)
            mojaOcena="0.0";

        TextView n = (TextView) findViewById(R.id.title);
        n.setText(naslovF);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(mojaOcena));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                SpremeniMojoOcenoTask task = new SpremeniMojoOcenoTask();
                task.execute(ratingBar.getRating() + "");
            }
        });

        mnenje = (EditText) findViewById(R.id.editKritika);

        Button shrani = (Button) findViewById(R.id.dodajKritikaButton);
        shrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mnenje.getText().toString().trim().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Besedilo ne sme ostati prazno!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    GetDodajKritikaTask task = new GetDodajKritikaTask();
                    task.execute(ratingBar.getRating()+"",mnenje.getText().toString() );
                }


            }
        });

        if(!prijavljen){
            mnenje.setVisibility(View.GONE);
            shrani.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.opomba)).setVisibility(View.VISIBLE);
        }

    }


    private class GetDodajKritikaTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddKritikaActivity.this);
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //dodaj v bazo kritiko

            System.out.println("st_zvezdic" + args[0]);
            System.out.println("mnenje: " + args[1]);
            System.out.println("apiFIlmaID: " + idFilmaApi);


            Kritika k= new Kritika();
            k.setAvtor(upIme);
            k.setIdAvtor(idUp);
            k.setBesedilo(args[1]);
            k.setTkIdFilma(idFilmaApi);
            k.setNaslovF(naslovF);
            java.util.Date date= new java.util.Date();
            Log.i("timestamp", new Timestamp(date.getTime())+"");
            //k.setVnos(cas);
            //Log.i("Cas je", k.getVnos().toString());

            mKritikaTable.insert(k);

            return "vBazi";
        }

        @Override
        protected void onPostExecute(String vBazi) {
            if(pDialog.isShowing())
                pDialog.dismiss();

            if(vBazi.equals("vBazi")) {
                Intent myIntent = new Intent(getBaseContext(), FilmActivity.class);
                myIntent.putExtra("id", (int) idFilmaApi);
                startActivity(myIntent);
            }


        }
    }

    private class SpremeniMojoOcenoTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog


        }

        @Override
        protected String doInBackground(String... args) {

            System.out.println("st_zvezdic" + args[0]);

            filmids.spremeniMojoOceno(idFilmaApi, args[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String vBazi) {
            filmids.close();

        }
    }

}
