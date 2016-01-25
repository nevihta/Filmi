package com.rvir.filmi.filmi.film;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;

public class AddKritikaActivity extends AppCompatActivity  {
    int idFilmaApi = 0;
    RatingBar ratingBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kritika);

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

        idFilmaApi = getIntent().getExtras().getInt("id");

        Button shrani = (Button) findViewById(R.id.dodajKritikaButton);
        shrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                EditText mnenje = (EditText) findViewById(R.id.editKritika);
                GetDodajKritikaTask task = new GetDodajKritikaTask();
                task.execute(ratingBar.getRating()+"",mnenje.getText().toString() );

            }
        });

    }

    private class GetDodajKritikaTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddKritikaActivity.this);
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //dodaj v bazo kritiko

            System.out.println("st_zvezdic"+args[0]);
            System.out.println("mnenje: "+args[1]);
            System.out.println("apiFIlmaID: "+idFilmaApi);

            return "vBazi";
        }

        @Override
        protected void onPostExecute(String vBazi) {
            if(pDialog.isShowing())
                pDialog.dismiss();

            if(vBazi.equals("vBazi")) {
                Intent myIntent = new Intent(getBaseContext(), FilmActivity.class);
                myIntent.putExtra("id", (int) idFilmaApi);
                startActivity(myIntent);
            }


        }
    }
}
