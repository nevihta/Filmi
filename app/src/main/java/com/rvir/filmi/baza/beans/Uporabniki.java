package com.rvir.filmi.baza.beans;


public class Uporabniki {

    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("koda")
    private String koda;
    @com.google.gson.annotations.SerializedName("uporabnisko_ime")
    private String upIme;
    @com.google.gson.annotations.SerializedName("email")
    private String email;
    @com.google.gson.annotations.SerializedName("geslo")
    private String geslo;

    public Uporabniki(){

    }

    public Uporabniki(String id, String koda, String uporabnisko_ime, String email, String geslo){
        this.setId(id);
        this.setKoda(koda);
        this.setUpIme(uporabnisko_ime);
        this.setEmail(email);
        this.setGeslo(geslo);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKoda() {
        return koda;
    }

    public void setKoda(String koda) {
        this.koda = koda;
    }

    public String getUpIme() {
        return upIme;
    }

    public void setUpIme(String upIme) {
        this.upIme = upIme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeslo() {
        return geslo;
    }

    public void setGeslo(String geslo) {
        this.geslo = geslo;
    }
}
