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
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;
import com.rvir.filmi.filmi.filmi.FilmiAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class PrijateljevSeznamActivity extends AppCompatActivity {
    ArrayList<Film> filmiPrijatelja = null;
    ArrayList<SeznamAzure> seznamF = null;

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
        final String idPrijatelja = myIntent.getExtras().getString("idPrijatelja"); System.out.println("idPrijatelja:"+idPrijatelja);
        final String seznam = myIntent.getExtras().getString("vrstaSeznama"); System.out.println("vrstaSeznama:"+seznam);

        seznamF=(ArrayList<SeznamAzure>)myIntent.getExtras().get("seznam");

        TextView naziv = (TextView) findViewById(R.id.textView);
        if(seznam.equals("fave")) {
            naziv.setText("Priljubljeni filmi"); System.out.print("priljubljeni");
        }
        else if(seznam.equals("watched"))
            naziv.setText("Ogledani filmi");
        else if(seznam.equals("wish"))
            naziv.setText("Seznam želja");

        ListView listView = ( ListView ) findViewById(R.id.listFilmiPrijatelja);
        SeznamiPrijateljAdapter fa = new SeznamiPrijateljAdapter(PrijateljevSeznamActivity.this, seznamF);
        listView.setAdapter(fa);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idFilma = seznamF.get(position).getTkIdFilma();

                Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                myIntent.putExtra("id", (int) idFilma);
                startActivity(myIntent);

                }
            });
        }
}
