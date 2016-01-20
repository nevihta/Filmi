package com.rvir.filmi.baza.beans;

import java.util.ArrayList;

public class Seznami {

    private ArrayList<Film> ogledani;
    private ArrayList<Film> priljubljeni;
    private ArrayList<Film> wishList;

    public Seznami(){

    }

    public ArrayList<Film> getOgledani() {
        return ogledani;
    }

    public void setOgledani(ArrayList<Film> ogledani) {
        this.ogledani = ogledani;
    }

    public ArrayList<Film> getPriljubljeni() {
        return priljubljeni;
    }

    public void setPriljubljeni(ArrayList<Film> priljubljeni) {
        this.priljubljeni = priljubljeni;
    }

    public ArrayList<Film> getWishList() {
        return wishList;
    }

    public void setWishList(ArrayList<Film> wishList) {
        this.wishList = wishList;
    }
}
