package com.rvir.filmi.filmi.seznami;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rvir.filmi.baza.sqlLite.FilmiDataSource;
import com.rvir.filmi.filmi.R;

public class FragmentWishlist extends Fragment {
    private FilmiDataSource datasource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.content_seznami_fragment_wishlist, container, false);
/*
        Film film = new Film();
        film.setIgralci("igralec 1, Igralec 2");
        film.setMojaOcena("5");
        film.setOpis("to je opis");
        film.setReziserji("test delovanja");
        film.setLetoIzida(1885);
        film.setUrlDoSlike("");
        film.setIdFilmApi(225);
        film.setNaslov("to je naslov filma");
        film.setOcena("7");


        datasource = new FilmiDataSource(getContext());
        datasource.open();


        datasource.dodajFilm(film);
        Film f = datasource.pridobiFilm(1);
        Log.i("test filma", f.getNaslov());
*/
        return view;

    }

}
