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
import android.widget.AdapterView;
import android.widget.ListView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Kritika;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;
import com.rvir.filmi.filmi.filmi.FilmiAdapter;

import java.util.ArrayList;

public class SeznamKritikActivity extends AppCompatActivity {
    ArrayList<Kritika> vseKritike = null;
    private String idUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seznam_kritik);

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
        if(sharedpreferences.getString("idUporabnika", null)!=null)
            idUp=sharedpreferences.getString("idUporabnika", null);

        GetSeznamKritikTask task = new GetSeznamKritikTask();
        task.execute();


    }

    private class GetSeznamKritikTask extends AsyncTask<String, Void, ArrayList<Kritika>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SeznamKritikActivity.this);
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Kritika> doInBackground(String... urls) {

           //pridobi seznam iz baze
            vseKritike = new ArrayList<>();
            Kritika f = new Kritika(); f.setNaslovF("star wars");f.setBesedilo(" To je moja kritika blablabla");
            vseKritike.add(f);
            Kritika ff = new Kritika(); ff.setNaslovF("star wars2");ff.setBesedilo(" To je moja kritika2 blablabla. Delaaa");
            vseKritike.add(ff);

            return vseKritike;
        }

        @Override
        protected void onPostExecute(ArrayList<Kritika> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            //izpis rezultatov
            if(result!=null) {
                ListView listView = ( ListView ) findViewById(R.id.listVseKritike);
                MojeKritikeAdapter ma = new MojeKritikeAdapter(SeznamKritikActivity.this, vseKritike);
                listView.setAdapter(ma);

            }

        }
    }

}
