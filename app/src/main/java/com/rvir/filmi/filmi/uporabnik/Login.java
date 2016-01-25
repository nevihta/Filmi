package com.rvir.filmi.filmi.uporabnik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.baza.beans.Seznami;
import com.rvir.filmi.baza.beans.Uporabniki;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.filmi.FilmiActivity;
import com.rvir.filmi.filmi.seznami.SeznamiActivity;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private TextView upIme = null;
    private TextView geslo = null;

    private MobileServiceClient mClient;
    private MobileServiceTable<Uporabniki> mUporabnikiTable;
    private MobileServiceTable<SeznamAzure> mSeznamiTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        upIme = (TextView) findViewById(R.id.editText);
        geslo = (TextView) findViewById(R.id.editGeslo);

        Button prijava = (Button) findViewById(R.id.prijavaButton);
        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((upIme.getText().toString().trim().equals("")) || (geslo.getText().toString().trim().equals(""))) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Vsa polja so obvezna!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    PrijavaTask task = new PrijavaTask();
                    task.execute(upIme.getText().toString(), geslo.getText().toString());
                }
            }
        });

        TextView registracija = (TextView) findViewById(R.id.registracijaButton);
        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Registracija.class);
                startActivity(i);
            }
        });



        TextView odjava = (TextView) findViewById(R.id.OdjavaButton);
        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    private class PrijavaTask extends AsyncTask<String, Void, MobileServiceList<Uporabniki>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Prijava poteka...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected MobileServiceList<Uporabniki> doInBackground(String...strings) {
            MobileServiceList<Uporabniki> result=null;

            try {
                result= mUporabnikiTable.where().field("uporabnisko_ime").eq(strings[0]).and().field("geslo").eq(strings[1]).execute().get();



            } catch (Exception e) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(MobileServiceList<Uporabniki> up) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(up.size()==1) {
                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("idUporabnika", up.get(0).getId());
                editor.commit();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Prijava ni bila uspešna!", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SinhronizacijaTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(Registracija.this);
            //pDialog.setMessage("Registracija poteka...");
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... id) {
            try {

                //pridobi seznam vnosov v sezname, ki jih je potrebno vstaviti...

                //SeznamAzure s=new SeznamAzure(1, id[0], ogledani.get(i).getIdFilmApi(), ogledani.get(i).getNaslov());
               // mSeznamiTable.insert(s);
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
