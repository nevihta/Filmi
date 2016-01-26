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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.Query;
import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.baza.beans.Seznami;
import com.rvir.filmi.baza.beans.Uporabniki;
import com.rvir.filmi.baza.sqlLite.PotrebnoSinhroniziratDataSource;
import com.rvir.filmi.baza.sqlLite.SeznamiDataSource;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;

import java.net.MalformedURLException;
import java.util.ArrayList;


public class Registracija extends AppCompatActivity {

    private TextView upIme=null;
    private TextView geslo=null;
    private TextView email=null;
   // Uporabniki u=null;
    private String id=null;

    private MobileServiceClient mClient;
    private MobileServiceTable<Uporabniki> mUporabnikiTable;
    private MobileServiceTable<SeznamAzure> mSeznamiTable;
    private SeznamiDataSource seznamds;
    private PotrebnoSinhroniziratDataSource sinhds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

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

        sinhds = new PotrebnoSinhroniziratDataSource(this);
        sinhds.open();
        seznamds = new SeznamiDataSource(this);
        seznamds.open();

        try {
            // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                    this);
            mUporabnikiTable = mClient.getTable(Uporabniki.class);
            mSeznamiTable = mClient.getTable(SeznamAzure.class);

        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        upIme=(TextView) findViewById(R.id.editText);
        geslo=(TextView) findViewById(R.id.editGeslo);
        email=(TextView) findViewById(R.id.editEmail);

        Button registracija = (Button) findViewById(R.id.registracijaButton);
        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((upIme.getText().toString().trim().equals("")) || (geslo.getText().toString().trim().equals("")) || (email.getText().toString().equals(""))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Vsa polja so obvezna!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {

                    PreveriMailTask task = new PreveriMailTask();
                    task.execute(email.getText().toString());
                }
            }
        });

    }

    private class  PreveriMailTask extends AsyncTask<String, Void, MobileServiceList<Uporabniki>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Registracija.this);
            pDialog.setMessage("Preverjanje emaila....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected MobileServiceList<Uporabniki>  doInBackground(String... mail) {
            MobileServiceList<Uporabniki> result=null;
            try {
                 result= mUporabnikiTable.where().field("email").eq(mail[0]).execute().get();
            } catch (Exception e) { }
            return result;
        }

        @Override
        protected void onPostExecute(MobileServiceList<Uporabniki> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();

               if(result.size()==0){
                   GeneratorKode gk = new GeneratorKode();
                   String koda = gk.generirajKodo();
                   //ustvarimo uporabnika
                   Uporabniki u = new Uporabniki();
                   u.setKoda(koda);
                   u.setUpIme(upIme.getText().toString());
                   u.setEmail(email.getText().toString());
                   u.setGeslo(geslo.getText().toString());

                   RegistracijaTask task = new RegistracijaTask();
                   task.execute(u);

                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(intent);
               }
            else{
                   Toast toast = Toast.makeText(getApplicationContext(), "Email že obstaja!", Toast.LENGTH_SHORT);
                   toast.show();
               }


        }
    }

    private class RegistracijaTask extends AsyncTask<Uporabniki, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Registracija.this);
            pDialog.setMessage("Registracija poteka...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Uporabniki... up) {
            try {
                mUporabnikiTable.insert(up[0]).get();
                id=up[0].getId();
                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("idUporabnika", id);
                editor.putString("upIme", up[0].getUpIme());
                editor.putString("koda", up[0].getKoda());
                editor.commit();
                sinhds.registriraj();

            } catch (Exception e) { }
            return id;
        }

        @Override
        protected void onPostExecute(String id) {
            if(pDialog.isShowing())
                pDialog.dismiss();

            SinhronizacijaTask task = new SinhronizacijaTask();
            task.execute(id);
        }
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
        protected String doInBackground(String... id) {
            try {
                SeznamAzure s = new SeznamAzure();
                ArrayList<Film> priljubljeni= new ArrayList<Film>();
                priljubljeni = seznamds.pridobiPriljubljene();
                ArrayList<Film> ogledani= new ArrayList<Film>();
                ogledani = seznamds.pridobiOgledane();
                ArrayList<Film> wish= new ArrayList<Film>();
                wish = seznamds.pridobiWishListo();
                Log.i("pridibilo vse sezname", "");


                for(int i=0; i<ogledani.size(); i++){
                    s=new SeznamAzure(1, id[0], ogledani.get(i).getIdFilmApi(), ogledani.get(i).getNaslov());
                    mSeznamiTable.insert(s);
                }
                Log.i("ogledani vstavljeni", "");

                for(int i=0; i<priljubljeni.size(); i++){
                    s=new SeznamAzure(2, id[0], priljubljeni.get(i).getIdFilmApi(), priljubljeni.get(i).getNaslov());
                    mSeznamiTable.insert(s);
                }
                for(int i=0; i<wish.size(); i++){
                    s=new SeznamAzure(3, id[0], wish.get(i).getIdFilmApi(), wish.get(i).getNaslov());
                    mSeznamiTable.insert(s);
                }

                Log.i("naredlo je", "upaaam");


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
