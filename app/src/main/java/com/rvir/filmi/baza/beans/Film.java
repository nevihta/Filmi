package com.rvir.filmi.baza.beans;

import java.util.ArrayList;

public class Film {

    private int idFilma;
    private int idFilmApi;
    private String Naslov;
    private int letoIzida;
    private String kategorije;
    private String igralci;
    private String reziserji;
    private String opis;
    private String ocena;
    private String mojaOcena;
    private String urlDoSlike;
    private ArrayList<Kritika> kritike;

    public Film(){

    }


    public int getIdFilma() {
        return idFilma;
    }

    public void setIdFilma(int idFilma) {
        this.idFilma = idFilma;
    }

    public int getIdFilmApi() {
        return idFilmApi;
    }

    public void setIdFilmApi(int idFilmApi) {
        this.idFilmApi = idFilmApi;
    }

    public String getNaslov() {
        return Naslov;
    }

    public void setNaslov(String naslov) {
        Naslov = naslov;
    }

    public int getLetoIzida() {
        return letoIzida;
    }

    public void setLetoIzida(int letoIzida) {
        this.letoIzida = letoIzida;
    }

    public String getIgralci() {
        return igralci;
    }

    public void setIgralci(String igralci) {
        this.igralci = igralci;
    }

    public String getReziserji() {
        return reziserji;
    }

    public void setReziserji(String reziserji) {
        this.reziserji = reziserji;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getOcena() {
        return (ocena);
    }

    public void setOcena(String ocena) {
        this.ocena = ocena;
    }

    public String getKategorije() {
        return kategorije;
    }

    public void setKategorije(String kategorije) {
        this.kategorije = kategorije;
    }

    public ArrayList<Kritika> getKritike() {
        return kritike;
    }

    public void setKritike(ArrayList<Kritika> kritike) {
        this.kritike = kritike;
    }

    public String getUrlDoSlike() {
        return urlDoSlike;
    }

    public void setUrlDoSlike(String urlDoSlike) {
        this.urlDoSlike = urlDoSlike;
    }

    public String getMojaOcena() {
        return mojaOcena;
    }

    public void setMojaOcena(String mojaOcena) {
        this.mojaOcena = mojaOcena;
    }
}
