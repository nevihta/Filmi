package com.rvir.filmi.filmi.filmi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Kategorija;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentKategorije extends Fragment {
    View view = null;
    Spinner spinnerKategorije = null;
    private ArrayList<Film> filmi = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_filmi_fragment_kategorije, container, false);

        spinnerKategorije = (Spinner) view.findViewById(R.id.spinner);

        final ArrayList<Kategorija> vseKategorije = new ArrayList<>();
        Kategorija k = new Kategorija(); k.setIdKategorije(35);k.setNaziv("comedy"); vseKategorije.add(k);
        Kategorija c = new Kategorija(); c.setIdKategorije(12);c.setNaziv("adventure"); vseKategorije.add(c);

        ArrayAdapter<Kategorija> adapter = new ArrayAdapter<Kategorija>(getContext(),
                android.R.layout.simple_spinner_item, vseKategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategorije.setAdapter(adapter);

        spinnerKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int idKategorije = vseKategorije.get(position).getIdKategorije();

                String url = "https://api.themoviedb.org/3/genre/"+idKategorije+"/movies?page=1&api_key=be86b39865e582aa63d877d88266bcfc";

                GetJSONKategorijeTask task = new GetJSONKategorijeTask();
                task.execute(url);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return view;
    }

    private class GetJSONKategorijeTask extends AsyncTask<String, Void, ArrayList<Film>> {
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
        protected ArrayList<com.rvir.filmi.baza.beans.Film> doInBackground(String... urls) {
            PopularniJSONParser popularniParser = new PopularniJSONParser();
            try {
                String input = new ServiceHandler().downloadURL(urls[0]);
                filmi =  popularniParser.parse(input);

                return filmi;
            } catch (IOException e) {
            }

            return filmi;
        }

        @Override
        protected void onPostExecute(ArrayList<com.rvir.filmi.baza.beans.Film> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            //izpis rezultatov
            if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.listKategorije);
                FilmiAdapter fa = new FilmiAdapter(getActivity(), filmi);
                listView.setAdapter(fa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // int idFilma = seznam.get(position).getId();

                        //  Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        //  myIntent.putExtra("id", (int) idFilma);
                        //  startActivity(myIntent);

                    }
                });
            }

        }
    }
}
