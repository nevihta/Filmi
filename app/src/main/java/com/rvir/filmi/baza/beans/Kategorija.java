package com.rvir.filmi.baza.beans;

public class Kategorija {

    private int idKategorije;
    private int idKategorijaApi;
    private String naziv;

    public Kategorija(){

    }

    public int getIdKategorije() {
        return idKategorije;
    }

    public void setIdKategorije(int idKategorije) {
        this.idKategorije = idKategorije;
    }

    public int getIdKategorijaApi() {
        return idKategorijaApi;
    }

    public void setIdKategorijaApi(int idKategorijaApi) {
        this.idKategorijaApi = idKategorijaApi;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
