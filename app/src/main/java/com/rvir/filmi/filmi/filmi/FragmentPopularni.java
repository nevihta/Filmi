package com.rvir.filmi.filmi.filmi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.ServiceHandler;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentPopularni extends Fragment {
    private ArrayList<com.rvir.filmi.baza.beans.Film> popularniFilmi = null;
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // https://api.themoviedb.org/3/movie/popular?page=1&api_key=be86b39865e582aa63d877d88266bcfc
        //za image: http://image.tmdb.org/t/p/w500/fYzpM9GmpBlIC893fNjoWCwE24H.jpg
        view = inflater.inflate(R.layout.content_filmi_fragment_popularni, container, false);

        String url = "https://api.themoviedb.org/3/movie/popular?page=1&api_key=be86b39865e582aa63d877d88266bcfc";

        GetJSONPopularniTask task = new GetJSONPopularniTask();
        task.execute(url);

        return view;
    }

    private class GetJSONPopularniTask extends AsyncTask<String, Void, ArrayList<com.rvir.filmi.baza.beans.Film>> {
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
                popularniFilmi =  popularniParser.parse(input);

                return popularniFilmi;
            } catch (IOException e) {
            }

            return popularniFilmi;
        }

        @Override
        protected void onPostExecute(ArrayList<com.rvir.filmi.baza.beans.Film> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();
            //izpis rezultatov
            if(result.size()>0) {
                ListView listView = ( ListView ) view.findViewById(R.id.list);
                FilmiAdapter fa = new FilmiAdapter(getActivity(),popularniFilmi);
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
