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


        Intent i = getIntent();
        final ArrayList<Kritika> result = (ArrayList<Kritika>)i.getExtras().get("kritike");
        if(result!=null) {
            ListView listView = ( ListView ) findViewById(R.id.listVseKritike);
            MojeKritikeAdapter ma = new MojeKritikeAdapter(SeznamKritikActivity.this, result);
            listView.setAdapter(ma);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int idFilma = result.get(position).getTkIdFilma();
                    System.out.print(idFilma);
                    Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                    myIntent.putExtra("OpenTab", 2);
                    myIntent.putExtra("id", (int) idFilma);
                    startActivity(myIntent);}
            });
        }


}
}
