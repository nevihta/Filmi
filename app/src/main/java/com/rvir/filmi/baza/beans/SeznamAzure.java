package com.rvir.filmi.baza.beans;

public class SeznamAzure {

    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("tk_id_tipa")
    private int tkIdTipa;
    @com.google.gson.annotations.SerializedName("tk_id_uporabnika")
    private String tkIdUporabnika;
    @com.google.gson.annotations.SerializedName("tk_id_filma")
    private int tkIdFilma;
    @com.google.gson.annotations.SerializedName("naslov_filma")
    private String naslovFilma;

    public SeznamAzure() {

    }

    public SeznamAzure(int tk_id_tipa, String tk_id_uporabnika, int tk_id_filma, String naslov_filma) {
        this.tkIdTipa=tk_id_tipa;
        this.tkIdUporabnika=tk_id_uporabnika;
        this.tkIdFilma=tk_id_filma;
        this.naslovFilma=naslov_filma;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTkIdTipa() {
        return tkIdTipa;
    }

    public void setTkIdTipa(int tkIdTipa) {
        this.tkIdTipa = tkIdTipa;
    }

    public String getTkIdUporabnika() {
        return tkIdUporabnika;
    }

    public void setTkIdUporabnika(String tkIdUporabnika) {
        this.tkIdUporabnika = tkIdUporabnika;
    }

    public int getTkIdFilma() {
        return tkIdFilma;
    }

    public void setTkIdFilma(int tkIdFilma) {
        this.tkIdFilma = tkIdFilma;
    }

    public String getNaslovFilma() {
        return naslovFilma;
    }

    public void setNaslovFilma(String naslovFilma) {
        this.naslovFilma = naslovFilma;
    }
}
