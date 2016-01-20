package com.rvir.filmi.baza.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rvir.filmi.baza.beans.Kategorija;

import java.util.ArrayList;

public class KategorijeDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private static final String[] KATEGORIJA_COLUMNS = {SQLiteHelper.ID_KATEGORIJE, SQLiteHelper.ID_KATEGORIJE_API, SQLiteHelper.NAZIV};

    public KategorijeDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void dodajKategorije(ArrayList<Kategorija> kategorije){
        ContentValues values = new ContentValues();
        for(Kategorija k : kategorije) {
            values.put(SQLiteHelper.ID_KATEGORIJE_API, k.getIdKategorijaApi());
            values.put(SQLiteHelper.NAZIV, k.getNaziv());
            database.insert(SQLiteHelper.TABELA_KATEGORIJA, null, values);
        }
    }

    public void dodajKategorijeFilma(ArrayList<Kategorija> kategorije, int id){
        ContentValues values = new ContentValues();
        Cursor cursor;
        int idKat;
        for(Kategorija k : kategorije) {

            cursor =database.rawQuery("select +" + SQLiteHelper.ID_KATEGORIJE + " from " + SQLiteHelper.TABELA_KATEGORIJA + " where " +
                    SQLiteHelper.ID_KATEGORIJE_API + " = " + k.getIdKategorijaApi(), null);
            if (cursor != null)
                cursor.moveToFirst();
            idKat=Integer.parseInt(cursor.getString(0));

            values.put(SQLiteHelper.TK_ID_KAT, idKat);
            values.put(SQLiteHelper.TK_ID_FILM_K, id);
            database.insert(SQLiteHelper.TABELA_KATEGORIJA, null, values);
        }
    }

    public ArrayList<Kategorija> pridobiKategorijeFilma(int id){

        String sql = "select k.* from  " + SQLiteHelper.TABELA_KATEGORIJA + " k, " + SQLiteHelper.TABELA_KAT_FILM + "kf " +
                "where k." + SQLiteHelper.ID_KATEGORIJE + "=kf." + SQLiteHelper.TK_ID_KAT + "and kf." + SQLiteHelper.TK_ID_FILM_K + "= " + id;
        Cursor cursor = database.rawQuery(sql, null);

        ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>();
        Kategorija kategorija;

        while (!cursor.isAfterLast()) {
            kategorija=cursorToKategorija(cursor);
            kategorije.add(kategorija);
            cursor.moveToNext();
        }
        cursor.close();

        return kategorije;

    }

    public ArrayList<Kategorija> pridobiKategorije(){

        Cursor cursor =
                database.query(SQLiteHelper.TABELA_KATEGORIJA,  // a. table
                        KATEGORIJA_COLUMNS, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>();
        Kategorija kategorija;

        while (!cursor.isAfterLast()) {
            kategorija=cursorToKategorija(cursor);
            kategorije.add(kategorija);
            cursor.moveToNext();
        }
        cursor.close();

        return kategorije;

    }

    private Kategorija cursorToKategorija(Cursor cursor) {
        Kategorija kategorija = new Kategorija();
        kategorija.setIdKategorije(Integer.parseInt(cursor.getString(0)));
        kategorija.setIdKategorijaApi(Integer.parseInt(cursor.getString(1)));
        kategorija.setNaziv(cursor.getString(2));
        return kategorija;
    }
}
