package com.rvir.filmi.filmi.filmi;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;
import com.rvir.filmi.filmi.film.FilmActivity;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentIskanje extends Fragment {
    View view = null;
    private FilmiDataSource datasource;
    private EditText searchText = null;
    private ArrayList<Film> rezultatiIskanja = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_filmi_fragment_iskanje, container, false);

        Button searchButton = (Button) view.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = (EditText) view.findViewById(R.id.searchText);

                String url = "http://api.themoviedb.org/3/search/movie?api_key=be86b39865e582aa63d877d88266bcfc&query="+ searchText.getText().toString();

                GetJSONIskanjeTask task = new GetJSONIskanjeTask();
                task.execute(url);
            }
        });

        return view;

    }

    private class GetJSONIskanjeTask extends AsyncTask<String, Void, ArrayList<Film>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Film> doInBackground(String... urls) {
            PopularniJSONParser popularniParser = new PopularniJSONParser();
            try {
                String input = new ServiceHandler().downloadURL(urls[0]);
                rezultatiIskanja =  popularniParser.parse(input);

                return rezultatiIskanja;
            } catch (IOException e) {
            }

            return rezultatiIskanja;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            //izpis rezultatov
            if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.listIskanje);
                FilmiAdapter fa = new FilmiAdapter(getActivity(), rezultatiIskanja);
                listView.setAdapter(fa);

                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = rezultatiIskanja.get(position).getIdFilmApi();

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", (int) idFilma);
                        startActivity(myIntent);

                    }
                });
            }

        }
    }

}
