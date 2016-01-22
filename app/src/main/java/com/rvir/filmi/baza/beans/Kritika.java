package com.rvir.filmi.baza.beans;


public class Kritika {

    @com.google.gson.annotations.SerializedName("id_kritike")
    private int idKritike;
    @com.google.gson.annotations.SerializedName("id")
    private String idKritikaApi;
    @com.google.gson.annotations.SerializedName("besedilo")
    private String besedilo;
    @com.google.gson.annotations.SerializedName("avtor")
    private String avtor;
    @com.google.gson.annotations.SerializedName("tk_id_filma")
    private int tkIdFilma;

    public Kritika() {

    }

    public Kritika(int id_kritike, String id, String besedilo, String avtor, int tk_id_filma){
        this.idKritike=id_kritike;
        this.idKritikaApi=id;
        this.besedilo=besedilo;
        this.avtor=avtor;
        this.tkIdFilma=tk_id_filma;
    }

    public int getIdKritike() {
        return idKritike;
    }

    public void setIdKritike(int idKritike) {
        this.idKritike = idKritike;
    }

    public String getIdKritikaApi() {
        return idKritikaApi;
    }

    public void setIdKritikaApi(String idKritikaApi) {
        this.idKritikaApi = idKritikaApi;
    }

    public String getBesedilo() {
        return besedilo;
    }

    public void setBesedilo(String besedilo) {
        this.besedilo = besedilo;
    }

    public String getAvtor() {
        return avtor;
    }

    public void setAvtor(String avtor) {
        this.avtor = avtor;
    }

    public int getTkIdFilma() {
        return tkIdFilma;
    }

    public void setTkIdFilma(int tkIdFilma) {
        this.tkIdFilma = tkIdFilma;
    }
}
