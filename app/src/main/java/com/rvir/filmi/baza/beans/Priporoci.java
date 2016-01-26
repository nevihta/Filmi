package com.rvir.filmi.baza.beans;

public class Priporoci {
    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("id_kdo")
    private String id_kdo;
    @com.google.gson.annotations.SerializedName("up_kdo")
    private String up_kdo;
    @com.google.gson.annotations.SerializedName("id_komu")
    private String id_komu;
    @com.google.gson.annotations.SerializedName("id_film")
    private String id_film;
    @com.google.gson.annotations.SerializedName("naslov_f")
    private String naslov_f;

    public Priporoci(){

    }
    public Priporoci(String id, String id_kdo, String up_kdo, String id_komu, String id_film, String naslov_f){
        this.id=id;
        this.id_kdo=id_kdo;
        this.up_kdo=up_kdo;
        this.id_komu=id_komu;
        this.id_film=id_film;
        this.naslov_f=naslov_f;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_kdo() {
        return id_kdo;
    }

    public void setId_kdo(String id_kdo) {
        this.id_kdo = id_kdo;
    }

    public String getUp_kdo() {
        return up_kdo;
    }

    public void setUp_kdo(String up_kdo) {
        this.up_kdo = up_kdo;
    }

    public String getId_komu() {
        return id_komu;
    }

    public void setId_komu(String id_komu) {
        this.id_komu = id_komu;
    }

    public String getId_film() {
        return id_film;
    }

    public void setId_film(String id_film) {
        this.id_film = id_film;
    }

    public String getNaslov_f() {
        return naslov_f;
    }

    public void setNaslov_f(String naslov_f) {
        this.naslov_f = naslov_f;
    }
}
