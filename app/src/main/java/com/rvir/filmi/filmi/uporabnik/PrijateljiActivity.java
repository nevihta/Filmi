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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.Prijatelji;
import com.rvir.filmi.baza.beans.Uporabniki;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;
import com.rvir.filmi.filmi.filmi.FilmiActivity;
import com.rvir.filmi.filmi.seznami.SeznamiOgledaniAdapter;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class PrijateljiActivity extends AppCompatActivity implements PrijateljiInterface {

    private ArrayList<Prijatelji> prijatelji=null;
    private String idUp;

    EditText koda;


    private MobileServiceClient mClient;
    private MobileServiceTable<Prijatelji> mPrijateljiTable;
    private MobileServiceTable<Uporabniki> mUporabnikiTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijatelji);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        idUp=sharedpreferences.getString("idUporabnika", null);

        try {
            // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                    this);
            mPrijateljiTable = mClient.getTable(Prijatelji.class);
            mUporabnikiTable = mClient.getTable(Uporabniki.class);
        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        PridobiPrijateljeTask task=new PridobiPrijateljeTask();
        task.execute(idUp);

        koda = (EditText)findViewById(R.id.editKoda);
        //koda.setText("QBW1Tk40");

        Button dodaj = (Button) findViewById(R.id.dodajButton);
        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("akcija", "dodaj" + koda.getText().toString());
                PoisciPrijateljaTask task=new PoisciPrijateljaTask();
                task.execute(koda.getText().toString());
            }
        });




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

    @Override
    public void remove(Prijatelji p) {
        Log.i("v remove je", "t");
        OdstraniPrijateljaTask task = new OdstraniPrijateljaTask();
        task.execute(p);
    }

    private class PridobiPrijateljeTask extends AsyncTask<String, Void, MobileServiceList<Prijatelji>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PrijateljiActivity.this);
            pDialog.setMessage("Prosimo počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected MobileServiceList<Prijatelji> doInBackground(String...idUp) {
            MobileServiceList<Prijatelji> result=null;

            try {
                result= mPrijateljiTable.where().field("id_uporabnika").eq(idUp[0]).execute().get();

            } catch (Exception e) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(MobileServiceList<Prijatelji> p) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            prijatelji=p;

            ListView listView = ( ListView ) findViewById(R.id.listPrijatelji);
            PrijateljiAdapter pa = new  PrijateljiAdapter(PrijateljiActivity.this, prijatelji, PrijateljiActivity.this);
            listView.setAdapter(pa);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Prijatelji prijatelj = prijatelji.get(position);

                    //na stran od prijatelja
               /* Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                myIntent.putExtra("id", prijatelj);
                startActivity(myIntent);*/

                }
            });


        }
    }
    private class DodajPrijateljaTask extends AsyncTask<Prijatelji, Void, Prijatelji> {
        private ProgressDialog pDialog;

        //tu si ostala

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PrijateljiActivity.this);
            pDialog.setMessage("Prosimo počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Prijatelji doInBackground(Prijatelji...p) {

            try {
                mPrijateljiTable.insert(p[0]).get();

            } catch (Exception e) {
            }
        return p[0];
        }

        @Override
        protected void onPostExecute(Prijatelji p) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            TextView vnosKode = (TextView)findViewById(R.id.editKoda);
            vnosKode.setText(null);
            PridobiPrijateljeTask task=new PridobiPrijateljeTask();
            task.execute(idUp);


        }
    }
    private class PoisciPrijateljaTask extends AsyncTask<String, Void, MobileServiceList<Uporabniki>> {
            private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(PrijateljiActivity.this);
                pDialog.setMessage("Iskanje prijatelja...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected MobileServiceList<Uporabniki> doInBackground(String...koda) {
                MobileServiceList<Uporabniki> result=null;

                try {
                    result=mUporabnikiTable.where().field("koda").eq(koda[0]).execute().get();

                } catch (Exception e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(MobileServiceList<Uporabniki> u) {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                if(u.size()>0){

                    if(!u.get(0).getId().equals(idUp))
                    {
                        Prijatelji p = new Prijatelji();
                        //vnese se s shranjene seje - kak koli pač naredima
                        p.setUp_ime(u.get(0).getUpIme());
                        p.setId_uporabnika(idUp);
                        p.setId_prijatelja(u.get(0).getId());

                        DodajPrijateljaTask task=new DodajPrijateljaTask();
                        task.execute(p);
                        }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Sebe ne morete dodati med prijatelje!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Napačna koda!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
    }
    private class OdstraniPrijateljaTask extends AsyncTask<Prijatelji, Void, Prijatelji> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(PrijateljiActivity.this);
            //pDialog.setMessage("Prosimo počakajte...");
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected Prijatelji doInBackground(Prijatelji...p) {

            try {
                Log.i("prijatelj je",p[0].getUp_ime() + p[0].getId()+"");
                mPrijateljiTable.delete(p[0]);
                Log.i("naredlo je", "upaaam");

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Prijatelji p) {
           // if (pDialog.isShowing())
            //    pDialog.dismiss();
            PridobiPrijateljeTask task=new PridobiPrijateljeTask();
            task.execute(idUp);


        }
    }

}
