package com.rvir.filmi.filmi.seznami;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;
import com.rvir.filmi.filmi.filmi.FilmiAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class PrijateljevSeznamActivity extends AppCompatActivity {
    ArrayList<Film> filmiPrijatelja = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijateljev_seznam);

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

        Intent myIntent = getIntent();
        final int idPrijatelja = Integer.parseInt(myIntent.getExtras().getString("idPrijatelja")); System.out.println("idPrijatelja:"+idPrijatelja);
        final String seznam = myIntent.getExtras().getString("vrstaSeznama"); System.out.println("vrstaSeznama:"+seznam);

        GetSeznamPrijateljaTask task = new GetSeznamPrijateljaTask();
        task.execute();


    }

    private class GetSeznamPrijateljaTask extends AsyncTask<String, Void, ArrayList<Film>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PrijateljevSeznamActivity.this);
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Film> doInBackground(String... urls) {

           //pridobi seznam iz baze

            filmiPrijatelja = new ArrayList<>();
            Film f = new Film(); f.setNaslov("star wars");f.setKategorije("action"); f.setIdFilmApi(140607);
            filmiPrijatelja.add(f);
            Film ff = new Film(); ff.setNaslov("star wars2");ff.setKategorije("action"); ff.setIdFilmApi(140607);
            filmiPrijatelja.add(ff);

            return filmiPrijatelja;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            //izpis rezultatov
            if(result!=null) {
                ListView listView = ( ListView ) findViewById(R.id.listFilmiPrijatelja);
                FilmiAdapter fa = new FilmiAdapter(PrijateljevSeznamActivity.this, filmiPrijatelja);
                listView.setAdapter(fa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = filmiPrijatelja.get(position).getIdFilmApi();

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", (int) idFilma);
                        startActivity(myIntent);

                    }
                });
            }

        }
    }

}
