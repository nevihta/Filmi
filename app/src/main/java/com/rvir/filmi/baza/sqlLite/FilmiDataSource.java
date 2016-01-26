package com.rvir.filmi.baza.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.util.Log;

import com.rvir.filmi.baza.beans.Film;

public class FilmiDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private static final String[] FILM_COLUMNS = {SQLiteHelper.ID_FILMA, SQLiteHelper.ID_FILMA_API, SQLiteHelper.NASLOV, SQLiteHelper.LETO_IZIDA,
             SQLiteHelper.KATEGORIJE, SQLiteHelper.IGRALCI, SQLiteHelper.REZISERJI, SQLiteHelper.OPIS, SQLiteHelper.OCENA, SQLiteHelper.MOJA_OCENA,
             SQLiteHelper.SLIKA, SQLiteHelper.VIDEO};

    public FilmiDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public Film pridobiFilm(int id){

        //pridobi osnovne podatke o filmu
        Cursor cursor =
                database.query(SQLiteHelper.TABELA_FILMI,  // a. table
                        FILM_COLUMNS, // b. column names
                        SQLiteHelper.ID_FILMA_API + "  = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        Film film=null;
        Log.i("cursor.getCount", cursor.getCount()+"");
        if((cursor != null)&&(cursor.getCount()>0)){
            cursor.moveToFirst();
            film = cursorToFilm(cursor);
            cursor.close();
        }

        //pridobi kritike


        return film;
    }

    public int dodajFilm(Film film){

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.ID_FILMA_API, film.getIdFilmApi());
        values.put(SQLiteHelper.NASLOV, film.getNaslov());
        values.put(SQLiteHelper.LETO_IZIDA, film.getLetoIzida());
        values.put(SQLiteHelper.KATEGORIJE, film.getKategorije());
        values.put(SQLiteHelper.IGRALCI, film.getIgralci());
        values.put(SQLiteHelper.REZISERJI, film.getReziserji());
        values.put(SQLiteHelper.OPIS, film.getOpis());
        values.put(SQLiteHelper.OCENA, film.getOcena());
        values.put(SQLiteHelper.MOJA_OCENA, film.getMojaOcena());
        values.put(SQLiteHelper.SLIKA, film.getUrlDoSlike());
        values.put(SQLiteHelper.VIDEO, film.getUrlVideo());

        //database.insert(SQLiteHelper.TABELA_FILMI, null, values);

        return (int)database.insert(SQLiteHelper.TABELA_FILMI, null, values);

    }

    public boolean spremeniMojoOceno(int id, String ocena){
        ContentValues newValues = new ContentValues();
        newValues.put(SQLiteHelper.MOJA_OCENA, ocena);
        return database.update(SQLiteHelper.TABELA_FILMI, newValues, SQLiteHelper.ID_FILMA_API + "=" + id, null)>0;

    }

    public String pridobiMojoOceno(int idFilma){
        String ocena="0.0";
        Cursor cursor =
                database.rawQuery("SELECT " + SQLiteHelper.MOJA_OCENA + " from " + SQLiteHelper.TABELA_FILMI + " where "+ SQLiteHelper.ID_FILMA_API +" = " + idFilma, null);

        if((cursor != null)&&(cursor.getCount()>0)){
            cursor.moveToFirst();
            ocena = cursor.getString(0);
        }
        cursor.close();

        return ocena;
    }

    private Film cursorToFilm(Cursor cursor) {
        Film film = new Film();
        film.setIdFilma(Integer.parseInt(cursor.getString(0)));
        film.setIdFilmApi(Integer.parseInt(cursor.getString(1)));
        film.setNaslov(cursor.getString(2));
        film.setLetoIzida(Integer.parseInt(cursor.getString(3)));
        film.setKategorije(cursor.getString(4));
        film.setIgralci(cursor.getString(5));
        film.setReziserji(cursor.getString(6));
        film.setOpis(cursor.getString(7));
        film.setOcena(cursor.getString(8));
        film.setMojaOcena(cursor.getString(9));
        film.setUrlDoSlike(cursor.getString(10));
        film.setUrlVideo(cursor.getString(11));

        return film;
    }





}
