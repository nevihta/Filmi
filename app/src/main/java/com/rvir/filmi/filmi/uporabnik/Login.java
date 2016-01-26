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
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.baza.sqlLite.PotrebnoSinhroniziratDataSource;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.filmi.FilmiActivity;
import com.rvir.filmi.filmi.seznami.SeznamiActivity;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private TextView upIme = null;
    private TextView geslo = null;

    private MobileServiceClient mClient;
    private MobileServiceTable<Uporabniki> mUporabnikiTable;
    private MobileServiceTable<SeznamAzure> mSeznamiTable;

    private PotrebnoSinhroniziratDataSource sinhds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        PotrebnoSinhroniziratDataSource sinhds = new PotrebnoSinhroniziratDataSource(this);
        sinhds.open();

        Boolean reg= sinhds.registriran();

        TextView registracija = (TextView) findViewById(R.id.registracijaButton);
        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Registracija.class);
                startActivity(i);
            }
        });

        if(reg){
            registracija.setVisibility(View.INVISIBLE);
            TextView tw = (TextView) findViewById(R.id.textViewRegistracija);
            tw.setVisibility(View.INVISIBLE);

        }


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
                editor.putString("upIme", up.get(0).getUpIme());
                editor.putString("koda", up.get(0).getKoda());
                editor.commit();

                Log.i("idUp", up.get(0).getId());
                SinhronizacijaTask task= new SinhronizacijaTask();
                task.execute(up.get(0).getId());

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
                Log.i("v sinh je", "task");
                MobileServiceList<SeznamAzure> result;
                //pridobi seznam vnosov v sezname, ki jih je potrebno vstaviti...
                ArrayList<ArrayList<SeznamAzure>> sa = new ArrayList<ArrayList<SeznamAzure>>();
                sa=sinhds.pridobiSeznameZaSinhronizacijo(id[0]);

                Log.i("sa size", sa.size()+"");

                ArrayList<SeznamAzure> dodaj = sa.get(0);
                Log.i("dodaj", dodaj.size() +"");
                ArrayList<SeznamAzure> odstrani = sa.get(1);
                Log.i("odstrani", odstrani.size() + "");

                for(int i=0; i<dodaj.size(); i++){
                    mSeznamiTable.insert(dodaj.get(i));
                    if(dodaj.get(i).getTkIdTipa()==1){
                        MobileServiceList<SeznamAzure> s1;
                        s1=mSeznamiTable.where().field("tk_id_tipa").eq(3).and().field("tk_id_filma").eq(odstrani.get(i).getTkIdFilma()).and().field("tk_id_uporabnika").eq(odstrani.get(i).getTkIdUporabnika()).execute().get();
                        mSeznamiTable.delete(s1.get(0));
                    }

                }
                for(int i=0; i<odstrani.size(); i++){
                    result=mSeznamiTable.where().field("tk_id_tipa").eq(odstrani.get(i).getTkIdTipa()).and().field("tk_id_filma").eq(odstrani.get(i).getTkIdFilma()).and().field("tk_id_uporabnika").eq(odstrani.get(i).getTkIdUporabnika()).execute().get();
                    mSeznamiTable.delete(result.get(0));
                }

                sinhds.izbrisiNaCakanju();

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
