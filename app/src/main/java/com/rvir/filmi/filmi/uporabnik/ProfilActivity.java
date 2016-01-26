package com.rvir.filmi.filmi.uporabnik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Kritika;
import com.rvir.filmi.baza.beans.Prijatelji;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.baza.beans.Uporabniki;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;
import com.rvir.filmi.filmi.seznami.PrijateljevSeznamActivity;
import com.rvir.filmi.filmi.seznami.SeznamiActivity;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProfilActivity extends AppCompatActivity {
    Film film = null;
    private String idUp;
    private boolean mojProfil=false;
    TextView koda;
    TextView upIme;
    String idProfila;
    private ProgressDialog pDialog;
    private SharedPreferences sharedpreferences;

    private MobileServiceClient mClient;
    private MobileServiceTable<Uporabniki> mUporabnikiTable;
    private MobileServiceTable<SeznamAzure> mSeznamiTable;
    private MobileServiceTable<Kritika> mKritikeTable;


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

        idProfila=getIntent().getExtras().getString("id");
        String imeP = getIntent().getExtras().getString("upIme");

        sharedpreferences = getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null)
            idUp=sharedpreferences.getString("idUporabnika", null);

        try {
            // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                    this);
            mSeznamiTable = mClient.getTable(SeznamAzure.class);
            mKritikeTable = mClient.getTable(Kritika.class);
            mUporabnikiTable = mClient.getTable(Uporabniki.class);
        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        TextView textId=(TextView) findViewById(R.id.idLabel);
        koda=(TextView) findViewById(R.id.idUporabnika);
        upIme=(TextView) findViewById(R.id.ime);
        String k="";

        if(idProfila.equals(idUp)){
            mojProfil=true;
            imeP=sharedpreferences.getString("upIme", null);
            k= sharedpreferences.getString("koda", null);
        }

        koda.setText(k);
        upIme.setText(imeP);

        if(!mojProfil){
            textId.setVisibility(View.INVISIBLE);
            koda.setVisibility(View.INVISIBLE);
        }

        GetSeznamiTask task = new GetSeznamiTask();
        task.execute(idProfila);

        GetKritikeTask task2 = new GetKritikeTask();
        task2.execute(idProfila);
    }

    private class GetSeznamiTask extends AsyncTask<String, Void, ArrayList<ArrayList<SeznamAzure>>> {

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
        protected ArrayList<ArrayList<SeznamAzure>> doInBackground(String... urls) {

            Log.i("id profila=", urls[0]);

            MobileServiceList<SeznamAzure> priljubljeni=null;
            MobileServiceList<SeznamAzure> ogledani=null;
            MobileServiceList<SeznamAzure> wish=null;

            try {
                ogledani = mSeznamiTable.where().field("tk_id_tipa").eq(1).and().field("tk_id_uporabnika").eq(idProfila).execute().get();
                priljubljeni = mSeznamiTable.where().field("tk_id_tipa").eq(2).and().field("tk_id_uporabnika").eq(idProfila).execute().get();
                wish = mSeznamiTable.where().field("tk_id_tipa").eq(3).and().field("tk_id_uporabnika").eq(idProfila).execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            ArrayList<ArrayList<SeznamAzure>> result=new ArrayList<ArrayList<SeznamAzure>>();
            result.add(ogledani);
            result.add(priljubljeni);
            result.add(wish);

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<SeznamAzure>> result) {


            final ArrayList<SeznamAzure> ogledani = result.get(0);
            final ArrayList<SeznamAzure> priljubljeni = result.get(1);
            final ArrayList<SeznamAzure> wish = result.get(2);

            //izpis rezultatov
            //priljubljeni - 2 filma - ce jih nima - set gone view
            TextView fave1 = (TextView) findViewById(R.id.fave1);
            fave1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idFilma = priljubljeni.get(0).getTkIdFilma();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);

                }
            });

            TextView fave2 = (TextView) findViewById(R.id.fave2);
            fave2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idFilma = priljubljeni.get(1).getTkIdFilma();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
               }
            });

             //ogledani - isto kot priljubljeni
            TextView watched1 = (TextView) findViewById(R.id.watched1);
            watched1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idFilma = ogledani.get(0).getTkIdFilma();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 }
            });
            TextView watched2 = (TextView) findViewById(R.id.watched2);
            watched2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idFilma = ogledani.get(0).getTkIdFilma();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
                }
            });

            //wishlist - isto kot prej
            TextView wish1 = (TextView) findViewById(R.id.wish1);
            wish1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idFilma = wish.get(0).getTkIdFilma();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);

                }
            });

            TextView wish2 = (TextView) findViewById(R.id.wish2);
            wish2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idFilma = wish.get(0).getTkIdFilma();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
                }
            });


            //nastavit na View.gone ce ni dovolj itemov v seznamu
            TextView moreFave = (TextView) findViewById(R.id.moreFave);
            moreFave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(idProfila.equals(idUp)){
                        Intent myIntent = new Intent(v.getContext(), SeznamiActivity.class);
                        myIntent.putExtra("OpenTab", 0);
                        startActivity(myIntent);
                    }else{
                        Intent myIntent = new Intent(v.getContext(), PrijateljevSeznamActivity.class);
                        myIntent.putExtra("idPrijatelja", idUp);
                        myIntent.putExtra("vrstaSeznama","fave");
                        myIntent.putExtra("seznam", priljubljeni);
                        startActivity(myIntent);
                    }

                }
            });

            TextView moreWatched = (TextView) findViewById(R.id.moreWatched);
            moreWatched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(idProfila.equals(idUp)){
                        Intent myIntent = new Intent(v.getContext(), SeznamiActivity.class);
                        myIntent.putExtra("OpenTab", 1);
                        startActivity(myIntent);
                    }else{
                        Intent myIntent = new Intent(v.getContext(), PrijateljevSeznamActivity.class);
                        myIntent.putExtra("idPrijatelja", idUp);
                        myIntent.putExtra("vrstaSeznama","watched");
                        myIntent.putExtra("seznam", ogledani);
                        startActivity(myIntent);
                    }
                }
            });

            TextView moreWish = (TextView) findViewById(R.id.moreWish);
            moreWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(idProfila.equals(idUp)){
                        Intent myIntent = new Intent(v.getContext(), SeznamiActivity.class);
                        myIntent.putExtra("OpenTab", 2);
                        startActivity(myIntent);
                    }else{
                        Intent myIntent = new Intent(v.getContext(), PrijateljevSeznamActivity.class);
                        myIntent.putExtra("idPrijatelja", idUp);
                        myIntent.putExtra("vrstaSeznama","wish");
                        myIntent.putExtra("seznam", wish);
                        startActivity(myIntent);
                    }
                }
            });

            TextView noneFave = (TextView) findViewById(R.id.noneFave);
            TextView noneWatched = (TextView) findViewById(R.id.noneWatched);
            TextView noneWish = (TextView) findViewById(R.id.noneWish);

            //logika
            if((priljubljeni!=null)&&(priljubljeni.size()!=0)){
                if(priljubljeni.size()<2){
                    fave2.setVisibility(View.GONE);
                    moreFave.setVisibility(View.GONE);
                }
                else
                    fave2.setText(priljubljeni.get(1).getNaslovFilma());
                fave1.setText(priljubljeni.get(0).getNaslovFilma());
            }
            else{
                fave2.setVisibility(View.GONE);
                fave1.setVisibility(View.GONE);
                moreFave.setVisibility(View.GONE);
                noneFave.setVisibility(View.VISIBLE);
            }

            if((ogledani!=null)&&(ogledani.size()!=0)){
                if(ogledani.size()<2){
                    watched2.setVisibility(View.GONE);
                    moreWatched.setVisibility(View.GONE);
                }
                else
                    watched2.setText(ogledani.get(1).getNaslovFilma());
                watched1.setText(ogledani.get(0).getNaslovFilma());
            }
            else{
                watched2.setVisibility(View.GONE);
                watched1.setVisibility(View.GONE);
                moreWatched.setVisibility(View.GONE);
                noneWatched.setVisibility(View.VISIBLE);
            }

            if((wish!=null)&&(wish.size()!=0)){
                if(wish.size()<2){
                    moreWish.setVisibility(View.GONE);
                    wish2.setVisibility(View.GONE);
                }
                else
                    wish2.setText(wish.get(1).getNaslovFilma());
                wish1.setText(wish.get(0).getNaslovFilma());
            }
            else{
                wish2.setVisibility(View.GONE);
                wish1.setVisibility(View.GONE);
                moreWish.setVisibility(View.GONE);
                noneWish.setVisibility(View.VISIBLE);
            }
        }
    }

    private class GetKritikeTask extends AsyncTask<String, Void, ArrayList<Kritika>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<Kritika> doInBackground(String... urls) {

            Log.i("id profila=", urls[0]);

            MobileServiceList<Kritika> kritike=null;

            try {
                kritike = mKritikeTable.where().field("tk_id_avtorja").eq(idProfila).orderBy("vnos", QueryOrder.Ascending).execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return kritike;
        }

        @Override
        protected void onPostExecute(final ArrayList<Kritika> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            TextView kritika1 = (TextView) findViewById(R.id.kritika1);
            kritika1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                 int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */
                }
            });

            TextView kritika2 = (TextView) findViewById(R.id.kritika2);
            kritika2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 /*                 int idFilma = getIdFilmApi();
                    Intent myIntent = new Intent(v.getContext(), FilmActivity.class);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);
 */               }
            });

            //kritike?? manjka stran...


            TextView noneKritik= (TextView) findViewById(R.id.noneKritik);

            TextView moreKritik = (TextView) findViewById(R.id.moreKritike);
            moreKritik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), SeznamKritikActivity.class);
                    myIntent.putExtra("kritike", result);
                    startActivity(myIntent);
                }
            });

            if((result!=null)&&(result.size()!=0)){
                if(result.size()<2){
                    moreKritik.setVisibility(View.GONE);
                    kritika2.setVisibility(View.GONE);
                }
                else
                    kritika2.setText("Kritika filma " + result.get(1).getNaslovF());
                kritika1.setText("Kritika filma " + result.get(0).getNaslovF());
            }
            else{
                kritika2.setVisibility(View.GONE);
                kritika1.setVisibility(View.GONE);
                moreKritik.setVisibility(View.GONE);
                noneKritik.setVisibility(View.VISIBLE);
            }






        }
    }


}
