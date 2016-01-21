package com.rvir.filmi.baza.beans;

public class Kategorija {

    private int idKategorije;
    private String naziv;

    public Kategorija(){

    }

    public int getIdKategorije() {
        return idKategorije;
    }

    public void setIdKategorije(int idKategorije) {
        this.idKategorije = idKategorije;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public String toString(){
        return this.getNaziv();
    }
}
