package com.rvir.filmi.baza.beans;


public class FilmAzure {
    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("id_filma")
    private int idFilmApi;
    @com.google.gson.annotations.SerializedName("naslov")
    private String Naslov;

    public FilmAzure(){

    }

    public FilmAzure(int id_filma, String naslov){
        this.idFilmApi=id_filma;
        this.Naslov=naslov;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


}
