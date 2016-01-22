package com.rvir.filmi.filmi.uporabnik;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.rvir.filmi.baza.beans.Uporabniki;
import com.rvir.filmi.filmi.R;

import java.net.MalformedURLException;


public class Registracija extends AppCompatActivity {

    private TextView upIme=null;
    private TextView geslo=null;
    private TextView email=null;
   // Uporabniki u=null;
    private String id=null;

    private MobileServiceClient mClient;
    private MobileServiceTable<Uporabniki> mUporabnikiTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                    this);
            mUporabnikiTable = mClient.getTable(Uporabniki.class);
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
                    //preveri email
                   /* GeneratorKode gk = new GeneratorKode();
                    String koda = gk.generirajKodo();
                    //ustvarimo uporabnika
                    Uporabniki u = new Uporabniki();
                    u.setKoda(koda);
                    u.setUpIme(upIme.getText().toString());
                    u.setEmail(email.getText().toString());
                    u.setGeslo(geslo.getText().toString());

                    RegistracijaTask task = new RegistracijaTask();
                    task.execute(u);*/


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

                   Intent intent = new Intent(getApplicationContext(), Login.class);
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
                Log.i("naredlo je", "upaaam");

            } catch (Exception e) { }
            return id;
        }

        @Override
        protected void onPostExecute(String id) {
            if(pDialog.isShowing())
                pDialog.dismiss();

            //še sinhronizacija fali
            }
        }
}
