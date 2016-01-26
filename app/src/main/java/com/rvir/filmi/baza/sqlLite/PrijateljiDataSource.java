package com.rvir.filmi.baza.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rvir.filmi.baza.beans.Prijatelji;

import java.util.ArrayList;

public class PrijateljiDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;


    public PrijateljiDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void dodajPrijatelja(Prijatelji p){
     /*   ContentValues values = new ContentValues();
        values.put(SQLiteHelper.TK_ID_PRI, p.getId_prijatelja());
        values.put(SQLiteHelper.UP_IME_PRI, p.getId_prijatelja());
        database.insert(SQLiteHelper.TABELA_PRIJATELJI, null, values);*/
    }

    public void odstraniPrijatelja(Prijatelji p){

    }

    public ArrayList<Prijatelji> pridobiPrijatelje(){
        ArrayList<Prijatelji> prijatelji = new ArrayList<Prijatelji>();



        return prijatelji;
    }

}
