package com.rvir.filmi.filmi.seznami;

import android.view.View;

import com.rvir.filmi.baza.beans.Film;

public interface WishlistInterface {
    public View remove(int idFilma, int idFilmaApi);
    public View putOnOgledani(Film f);
}