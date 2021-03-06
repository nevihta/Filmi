package com.rvir.filmi.baza.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Seznami;

import java.util.ArrayList;

public class SeznamiDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;


    public SeznamiDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Seznami pridobiVseSezname(){
        Seznami s = new Seznami();
        s.setOgledani(pridobiOgledane());
        s.setPriljubljeni(pridobiPriljubljene());
        s.setWishList(pridobiWishListo());
        return s;
    }

    public ArrayList<Film> pridobiOgledane(){
        String sql = "select f.* from  " + SQLiteHelper.TABELA_FILMI + " f, " + SQLiteHelper.TABELA_SEZNAMI + " s, " + SQLiteHelper.TABELA_TIP_SEZNAM + " t " +
                " where f." + SQLiteHelper.ID_FILMA+ "=s." + SQLiteHelper.TK_ID_FILM_S + " and s." + SQLiteHelper.TK_ID_TIP + "= t." +
                SQLiteHelper.ID_TIPA + " and t." + SQLiteHelper.NAZIV_T + "= 'ogledan'";
        Cursor cursor = database.rawQuery(sql, null);

        ArrayList<Film> filmi = new ArrayList<>();
        Film f;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            f=cursorToFilm(cursor);
            filmi.add(f);
            cursor.moveToNext();
        }
        cursor.close();
        return filmi;
    }

    public ArrayList<Film> pridobiPriljubljene(){
        String sql = "select f.* from  " + SQLiteHelper.TABELA_FILMI + " f, " + SQLiteHelper.TABELA_SEZNAMI + " s, " + SQLiteHelper.TABELA_TIP_SEZNAM + " t " +
                " where f." + SQLiteHelper.ID_FILMA+ "=s." + SQLiteHelper.TK_ID_FILM_S + " and s." + SQLiteHelper.TK_ID_TIP + "= t." +
                SQLiteHelper.ID_TIPA + " and t." + SQLiteHelper.NAZIV_T + "= 'priljubljen'";
        Cursor cursor = database.rawQuery(sql, null);

        ArrayList<Film> filmi = new ArrayList<>();
        Film f;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            f=cursorToFilm(cursor);
            filmi.add(f);
            cursor.moveToNext();
        }
        cursor.close();
        return filmi;
    }

    public ArrayList<Film> pridobiWishListo(){
        String sql = "select f.* from  " + SQLiteHelper.TABELA_FILMI + " f, " + SQLiteHelper.TABELA_SEZNAMI + " s, " + SQLiteHelper.TABELA_TIP_SEZNAM + " t " +
                " where f." + SQLiteHelper.ID_FILMA+ "=s." + SQLiteHelper.TK_ID_FILM_S + " and s." + SQLiteHelper.TK_ID_TIP + "= t." +
                SQLiteHelper.ID_TIPA + " and t." + SQLiteHelper.NAZIV_T + "= 'wish'";
        Cursor cursor = database.rawQuery(sql, null);

        ArrayList<Film> filmi = new ArrayList<>();
        Film f;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            f=cursorToFilm(cursor);
            filmi.add(f);
            cursor.moveToNext();
        }
        cursor.close();
        return filmi;
    }

    public void dodajNaSeznam(int id, String seznam){
        Log.i("v dodaj je", "seznam " + seznam);
        Cursor cursor =
                database.rawQuery("select " + SQLiteHelper.ID_TIPA + " from " + SQLiteHelper.TABELA_TIP_SEZNAM + " where " + SQLiteHelper.NAZIV_T + " = '" + seznam + "'", null);

        int idTipa=1;
        if ((cursor != null)&&(cursor.getCount()>0)){
            cursor.moveToFirst();
            idTipa = (Integer.parseInt(cursor.getString(0)));
        }
        cursor.close();


        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.TK_ID_FILM_S, id);
        values.put(SQLiteHelper.TK_ID_TIP, idTipa);
        database.insert(SQLiteHelper.TABELA_SEZNAMI, null, values);

        if(seznam.equals("ogledan")) {
            Log.i("v ogledan", "t");

            cursor =
                    database.rawQuery("select " + SQLiteHelper.ID_TIPA + " from " + SQLiteHelper.TABELA_TIP_SEZNAM + " where " + SQLiteHelper.NAZIV_T + " = 'wish'", null);

            if ((cursor != null)&&(cursor.getCount()>0)){
                cursor.moveToFirst();
                idTipa = (Integer.parseInt(cursor.getString(0)));
                cursor.close();
            }
            Log.i("idTipa", idTipa+"");

            database.delete(SQLiteHelper.TABELA_SEZNAMI, SQLiteHelper.TK_ID_TIP + "=" + idTipa + " AND " + SQLiteHelper.TK_ID_FILM + "=" + id, null);
        }
    }

    public void odstraniSSeznama(int id, String seznam){
        Cursor cursor =
                database.rawQuery("select " + SQLiteHelper.ID_TIPA + " from " + SQLiteHelper.TABELA_TIP_SEZNAM + " where " + SQLiteHelper.NAZIV_T + " = '" + seznam + "'", null);

        int idTipa=1;
        if ((cursor != null)&&(cursor.getCount()>0)){
            cursor.moveToFirst();
            idTipa = (Integer.parseInt(cursor.getString(0)));
            cursor.close();
        }

        database.delete(SQLiteHelper.TABELA_SEZNAMI, SQLiteHelper.TK_ID_TIP + "=" + idTipa + " AND " + SQLiteHelper.TK_ID_FILM + "=" + id, null);
    }

    public ArrayList<String> vrniSeznameNaKaterihJeFilm(int id){

        Cursor cursor= database.rawQuery("select t." + SQLiteHelper.NAZIV_T + " from " + SQLiteHelper.TABELA_TIP_SEZNAM + " t, " +
                SQLiteHelper.TABELA_SEZNAMI + " s where s." + SQLiteHelper.TK_ID_TIP + "= t." + SQLiteHelper.ID_TIPA + " AND s." +
                SQLiteHelper.TK_ID_FILM_S + " = " + id, null);

        ArrayList <String> seznami = new ArrayList<String>();

      // Log.i("cursor size", cursor.getCount()+"");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            seznami.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return seznami;
    }

    public boolean preveriCeJeFilmOgledan(int id){
        Cursor cursor= database.rawQuery("select f." + SQLiteHelper.NASLOV + " from " + SQLiteHelper.TABELA_FILMI + " f, " +
                SQLiteHelper.TABELA_SEZNAMI + " s where s." + SQLiteHelper.TK_ID_TIP + "=1 AND s." + SQLiteHelper.TK_ID_FILM + " = f." +
                SQLiteHelper.ID_FILMA + " AND f." + SQLiteHelper.ID_FILMA_API + "=" + id, null);

        if((cursor!=null)&&(cursor.getCount()>0))
            return true;

        return false;


    }

    public Film pridobiRandomFilm(){
        Film f=new Film();

        String sql = "select f.* from  " + SQLiteHelper.TABELA_FILMI + " f, " + SQLiteHelper.TABELA_SEZNAMI + " s, " + SQLiteHelper.TABELA_TIP_SEZNAM + " t " +
                "  where f." + SQLiteHelper.ID_FILMA+ "=s." + SQLiteHelper.TK_ID_FILM_S + " and s." + SQLiteHelper.TK_ID_TIP + "= t." +
                SQLiteHelper.ID_TIPA + " and t." + SQLiteHelper.NAZIV_T + "= 'priljubljen' ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = database.rawQuery(sql, null);

        if((cursor!=null)&&(cursor.getCount()==1)){
            cursor.moveToFirst();
            f =cursorToFilm(cursor);
        }
        else{
            sql = "select f.* from  " + SQLiteHelper.TABELA_FILMI + " f, " + SQLiteHelper.TABELA_SEZNAMI + " s, " + SQLiteHelper.TABELA_TIP_SEZNAM + " t " +
                    "  where f." + SQLiteHelper.ID_FILMA+ "=s." + SQLiteHelper.TK_ID_FILM_S + " and s." + SQLiteHelper.TK_ID_TIP + "= t." +
                    SQLiteHelper.ID_TIPA + " and t." + SQLiteHelper.NAZIV_T + "= 'wish' ORDER BY RANDOM() LIMIT 1";
            cursor = database.rawQuery(sql, null);
            if((cursor!=null)&&(cursor.getCount()==1)){
                cursor.moveToFirst();
                f =cursorToFilm(cursor);
            }
            else
                f=null;
        }

        return f;
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
