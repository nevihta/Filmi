package com.rvir.filmi.baza.beans;


import android.text.format.Time;

import java.io.Serializable;
import java.sql.Date;

public class Kritika  implements Serializable {


    @com.google.gson.annotations.SerializedName("id")
    private String idKritika;
    @com.google.gson.annotations.SerializedName("besedilo")
    private String besedilo;
    @com.google.gson.annotations.SerializedName("avtor")
    private String avtor;
    @com.google.gson.annotations.SerializedName("tk_id_avtorja")
    private String idAvtor;
    @com.google.gson.annotations.SerializedName("tk_id_filma")
    private int tkIdFilma;
    @com.google.gson.annotations.SerializedName("naslov_filma")
    private String naslovF;
    @com.google.gson.annotations.SerializedName("vnos")
    private String vnos;

    public Kritika() {

    }

    public Kritika( String id, String besedilo, String avtor, String tk_id_avtorja, int tk_id_filma, String naslov_filma, String vnos){
        this.idKritika=id;
        this.besedilo=besedilo;
        this.avtor=avtor;
        this.idAvtor=tk_id_avtorja;
        this.tkIdFilma=tk_id_filma;
        this.naslovF=naslov_filma;
        this.vnos=vnos;
    }


    public String getIdKritikaApi() {
        return idKritika;
    }

    public void setIdKritika(String idKritikaApi) {
        this.idKritika = idKritikaApi;
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

    public String getIdAvtor() {
        return idAvtor;
    }

    public void setIdAvtor(String idAvtor) {
        this.idAvtor = idAvtor;
    }

    public String getNaslovF() {
        return naslovF;
    }

    public void setNaslovF(String naslovF) {
        this.naslovF = naslovF;
    }


    public String getVnos() {
        return vnos;
    }

    public void setVnos(String vnos) {
        this.vnos = vnos;
    }
}
