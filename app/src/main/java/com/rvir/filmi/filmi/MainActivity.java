package com.rvir.filmi.filmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.rvir.filmi.filmi.filmi.FilmiActivity;
import com.rvir.filmi.filmi.priporocila.PriporocilaActivity;
import com.rvir.filmi.filmi.seznami.SeznamiActivity;
import com.rvir.filmi.filmi.uporabnik.Login;
import com.rvir.filmi.filmi.uporabnik.PrijateljiActivity;
import com.rvir.filmi.filmi.uporabnik.ProfilActivity;

public class MainActivity extends AppCompatActivity {
    public static final String seja = "MyPrefs";
    private boolean prijavljen=false;
    String idUp="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setContentView(R.layout.app_bar_main);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setContentView(R.layout.activity_main_landscape);
                break;
        }

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
        {
            prijavljen=true;
            idUp=sharedpreferences.getString("idUporabnika", null);
        }

        Log.i("prijavljen", prijavljen + "");

        Button filmi = (Button) findViewById(R.id.buttonFilmi);
        filmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), FilmiActivity.class);
                startActivity(i);
            }
        });

        Button search = (Button) findViewById(R.id.home_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), FilmiActivity.class);
                i.putExtra("OpenTab", 2);
                startActivity(i);
            }
        });

        Button prijava = (Button) findViewById(R.id.buttonPrijava);
        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Login.class);
                startActivity(i);
            }
        });

        Button priporoci = (Button) findViewById(R.id.buttonPriporoci);
        priporoci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PriporocilaActivity.class);
                startActivity(i);
            }
        });



        Button seznami = (Button) findViewById(R.id.buttonSeznami);
        seznami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SeznamiActivity.class);
                startActivity(i);
            }
        });

        Button prijatelji = (Button) findViewById(R.id.buttonPrijatelji);
        prijatelji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PrijateljiActivity.class);
                startActivity(i);
            }
        });

        Button profil = (Button) findViewById(R.id.buttonProfil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfilActivity.class);
                i.putExtra("id", idUp);
                startActivity(i);
            }
        });


        if(!prijavljen) {
            prijatelji.setVisibility(View.GONE);
            prijava.setVisibility(View.VISIBLE);
            profil.setVisibility(View.GONE);
        }
        else{
            prijatelji.setVisibility(View.VISIBLE);
            //prijava.setVisibility(View.GONE);
            //profil.setVisibility(View.VISIBLE);

        }

    }


}
