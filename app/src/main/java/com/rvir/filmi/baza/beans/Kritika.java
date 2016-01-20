package com.rvir.filmi.baza.beans;


public class Kritika {

    private int idKritike;
    private String idKritikaApi;
    private String besedilo;
    private String avtor;
    private int tkIdFilma;

    public Kritika() {

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
