package com.rvir.filmi.baza.beans;

public class SeznamAzure {

    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("tk_id_filma")
    private int tkIdFilma;
    @com.google.gson.annotations.SerializedName("tk_id_tipa")
    private int tkIdTipa;
    @com.google.gson.annotations.SerializedName("tk_id_uporabnika")
    private String tkIdUporabnika;

    public SeznamAzure() {

    }

    public SeznamAzure(int tk_id_filma, int tk_id_tipa, String tk_id_uporabnika) {
        this.setTkIdFilma(tk_id_filma);
        this.setTkIdTipa(tk_id_tipa);
        this.setTkIdUporabnika(tk_id_uporabnika);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTkIdFilma() {
        return tkIdFilma;
    }

    public void setTkIdFilma(int tkIdFilma) {
        this.tkIdFilma = tkIdFilma;
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
}
