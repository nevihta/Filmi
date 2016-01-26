package com.rvir.filmi.filmi.priporocila;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Priporoci;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;

import java.util.ArrayList;

public class FragmentPrijateljiPriporocila extends Fragment implements PriporocilaInterface{
    View view = null;
    private ArrayList<Priporoci> priporoceniFilmi = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_priporocila_fragment_prijatelji, container, false);

        GetPrijateljiPriporoceniTask task = new GetPrijateljiPriporoceniTask();
        task.execute();

        return view;

    }

    private class GetPrijateljiPriporoceniTask extends AsyncTask<String, Void, ArrayList<Priporoci>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Priporoci> doInBackground(String... urls) {
            priporoceniFilmi = new ArrayList<>();
            Priporoci p = new Priporoci();
            p.setId("140607");p.setId_film("140607");p.setNaslov_f("Star Wars: The Force Awakens"); p.setUp_kdo("Anja");
            priporoceniFilmi.add(p);
            Priporoci f = new Priporoci(); f.setId("140607");f.setId_film("140607");f.setNaslov_f("Star Wars2: The Force Awakens"); f.setUp_kdo("Anja");
            //pridobi priporocene filme iz baze =)

            return priporoceniFilmi;
        }

        @Override
        protected void onPostExecute(ArrayList<Priporoci> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
           // izpis rezultatov
            if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.listPrijateljiPriporocila);
                PriporocilaAdapter pa = new PriporocilaAdapter(getActivity(), priporoceniFilmi, FragmentPrijateljiPriporocila.this);
                listView.setAdapter(pa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = Integer.parseInt(priporoceniFilmi.get(position).getId_film());

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", (int) idFilma);
                        startActivity(myIntent);

                    }
                });
            }

        }
    }

    @Override
    public View remove(int idPriporocila, int idFilmaApi) {
        System.out.println("remove priporocilo "+idFilmaApi);
      //odstrani iz seznama

        GetPrijateljiPriporoceniTask task = new GetPrijateljiPriporoceniTask();
        task.execute();

        return view;
    }

}
