package com.rvir.filmi.baza.beans;

public class Prijatelji {

    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("id_prijatelja")
    private String id_prijatelja;
    @com.google.gson.annotations.SerializedName("id_uporabnika")
    private String id_uporabnika;
    @com.google.gson.annotations.SerializedName("up_ime")
    private String up_ime;

    public Prijatelji(){

    }

    public Prijatelji(String id, String id_prijatelja, String id_uporabnika, String up_ime){
        this.id=id;
        this.id_prijatelja=id_prijatelja;
        this.id_uporabnika=id_uporabnika;
        this.setUp_ime(up_ime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_prijatelja() {
        return id_prijatelja;
    }

    public void setId_prijatelja(String id_prijatelja) {
        this.id_prijatelja = id_prijatelja;
    }

    public String getId_uporabnika() {
        return id_uporabnika;
    }

    public void setId_uporabnika(String id_uporabnika) {
        this.id_uporabnika = id_uporabnika;
    }

    public String getUp_ime() {
        return up_ime;
    }

    public void setUp_ime(String up_ime) {
        this.up_ime = up_ime;
    }

    @Override
    public String toString(){
        return getUp_ime();
    }


}
