package com.rvir.filmi.baza.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rvir.filmi.baza.beans.Kategorija;
import com.rvir.filmi.baza.beans.Kritika;

import java.util.ArrayList;

public class KritikeDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private static final String[] KRITIKA_COLUMNS = {SQLiteHelper.ID_KRITIKE, SQLiteHelper.ID_KRITIKE_API, SQLiteHelper.BESEDILO,
            SQLiteHelper.AVTOR, SQLiteHelper.TK_ID_FILM};

    public KritikeDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<Kritika> PridobiKritikeFilma(int id){

        Cursor cursor =
                database.query(SQLiteHelper.TABELA_KRITIKE,  // a. table
                        KRITIKA_COLUMNS, // b. column names
                        SQLiteHelper.TK_ID_FILM + "  = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        ArrayList<Kritika> kritike = new ArrayList<Kritika>();
        Kritika kritika;

        while (!cursor.isAfterLast()) {
            kritika=cursorToKritika(cursor);
            kritike.add(kritika);
            cursor.moveToNext();
        }

        cursor.close();

        return kritike;
    }

    public void DodajKritike(ArrayList<Kritika> kritike){
        ContentValues values = new ContentValues();
        for(Kritika k : kritike) {
            values.put(SQLiteHelper.ID_KRITIKE_API, k.getIdKritikaApi());
            values.put(SQLiteHelper.BESEDILO, k.getBesedilo());
            values.put(SQLiteHelper.AVTOR, k.getAvtor());
            values.put(SQLiteHelper.TK_ID_FILM, k.getTkIdFilma());
            database.insert(SQLiteHelper.TABELA_KRITIKE, null, values);
        }
    }

    public void DodajKritiko(Kritika k){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.ID_KRITIKE_API, k.getIdKritikaApi());
        values.put(SQLiteHelper.BESEDILO, k.getBesedilo());
        values.put(SQLiteHelper.AVTOR, k.getAvtor());
        values.put(SQLiteHelper.TK_ID_FILM, k.getTkIdFilma());

        database.insert(SQLiteHelper.TABELA_KRITIKE, null, values);
    }

    private Kritika cursorToKritika(Cursor cursor) {
        Kritika kritika= new Kritika();
        kritika.setIdKritike(Integer.parseInt(cursor.getString(0)));
        kritika.setIdKritikaApi(cursor.getString(1));
        kritika.setBesedilo(cursor.getString(2));
        kritika.setAvtor(cursor.getString(3));
        return kritika;
    }


}
