package com.rvir.filmi.filmi.filmi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.rvir.filmi.filmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentPopularni extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // https://api.themoviedb.org/3/movie/popular?page=1&api_key=be86b39865e582aa63d877d88266bcfc
        //za image: http://image.tmdb.org/t/p/w500/fYzpM9GmpBlIC893fNjoWCwE24H.jpg
        View view = inflater.inflate(R.layout.content_filmi_fragment_popularni, container, false);

        final ArrayList<Movie> seznam = new ArrayList<>();
        seznam.add(new Movie("url", "http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg"));
        seznam.add(new Movie("test", "http://image.tmdb.org/t/p/w500/fNByoFAFaJzNUxs6AGQ160zriIx.jpg"));

        ListView listView = ( ListView ) view.findViewById(R.id.list);
        FilmiAdapter fa = new FilmiAdapter(getActivity(),seznam);
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

        return view;
    }
}
