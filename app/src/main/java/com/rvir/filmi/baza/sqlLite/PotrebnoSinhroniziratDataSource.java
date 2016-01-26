package com.rvir.filmi.baza.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.SeznamAzure;

import java.util.ArrayList;

public class PotrebnoSinhroniziratDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private static final String[] REGISTRIRAN_COLUMNS = {SQLiteHelper.ID_REG, SQLiteHelper.REGISTRIRAN};
    private static final String[] SINH_SEZ_COLUMNS = {SQLiteHelper.ID_SINH_SEZ, SQLiteHelper.NASLOV_F, SQLiteHelper.ID_F, SQLiteHelper.ID_T, SQLiteHelper.URL_SLIKA, SQLiteHelper.AKCIJA};

    public PotrebnoSinhroniziratDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    public void close() {
        dbHelper.close();
    }

    public ArrayList<ArrayList<SeznamAzure>> pridobiSeznameZaSinhronizacijo(String idUp){
        ArrayList<ArrayList<SeznamAzure>> sa = new ArrayList<ArrayList<SeznamAzure>>();
        ArrayList<SeznamAzure> dodaj = new ArrayList<SeznamAzure>();
        ArrayList<SeznamAzure> odstrani = new ArrayList<SeznamAzure>();

        Cursor cursor =
                database.query(SQLiteHelper.TABELA_SINH_SEZNAMI,  // a. table
                        SINH_SEZ_COLUMNS, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        SeznamAzure s;
        Log.i("cursor size", cursor.getCount() + "");
        cursor.moveToFirst();
        Log.i("cursor 0", cursor.getString(0));
        Log.i("cursor 1", cursor.getString(1));
        Log.i("cursor 2", cursor.getString(2));
        Log.i("cursor 3", cursor.getString(3));
        Log.i("cursor 4", cursor.getString(4));

        while (!cursor.isAfterLast()) {
            s=cursorToSeznam(cursor, idUp);
            //Log.i("naslov filma", s.getNaslovFilma());
            //Log.i("get string 3", cursor.getString(3));
            if(cursor.getString(5).equals("odstrani"))
                odstrani.add(s);
            else
                dodaj.add(s);
            cursor.moveToNext();
    }
        //Log.i("dodaj size", dodaj.size()+"");
        //Log.i("odstrani site", odstrani.size() + "");
        sa.add(dodaj);
        sa.add(odstrani);
        return sa;
    }

    public void izbrisiNaCakanju(){
        database.execSQL("DELETE FROM " + SQLiteHelper.TABELA_SINH_SEZNAMI);
        database.execSQL("VACUUM");

    }

    public boolean registriran(){
        Boolean registriran=false;
        Cursor cursor =
                database.query(SQLiteHelper.TABELA_REGISTRACIJA,  // a. table
                        REGISTRIRAN_COLUMNS, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if((cursor != null)&&(cursor.getCount()>0)){
            cursor.moveToFirst();

            registriran=Boolean.valueOf(cursor.getString(1));
        }
        return registriran;
    }

    public void registriraj(){

        ContentValues values=new ContentValues();
        values.put(SQLiteHelper.REGISTRIRAN, "true");
        database.update(SQLiteHelper.TABELA_REGISTRACIJA, values, null, null);

    }

    public void dodajSeznami(Film film, String tk_tipa, String akcija) {

        ContentValues values=new ContentValues();
        values.put(SQLiteHelper.NASLOV_F, film.getNaslov());
        values.put(SQLiteHelper.ID_F, film.getIdFilmApi());
        values.put(SQLiteHelper.ID_T, tk_tipa);
        values.put(SQLiteHelper.URL_SLIKA, film.getUrlDoSlike());
        values.put(SQLiteHelper.AKCIJA, akcija);
        if(akcija.equals("dodaj")) {
            Log.i("sled", "dodaj");
            Cursor cursor = database.query(SQLiteHelper.TABELA_SINH_SEZNAMI,  // a. table
                    SINH_SEZ_COLUMNS, // b. column names
                    SQLiteHelper.ID_F + "=" + film.getIdFilmApi() + " AND " + SQLiteHelper.ID_T + "=? AND " + SQLiteHelper.AKCIJA + " = ?",// c. selections
                    new String[]{tk_tipa, "odstrani"}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            if ((cursor != null) && (cursor.getCount() > 0)){
                cursor.moveToFirst();
                database.delete(SQLiteHelper.TABELA_SINH_SEZNAMI, SQLiteHelper.ID_SINH_SEZ + "=" + cursor.getInt(0), null);
             }
             else {
                database.insert(SQLiteHelper.TABELA_SINH_SEZNAMI, null, values);
                if (tk_tipa.equals("1"))
                    database.delete(SQLiteHelper.TABELA_SINH_SEZNAMI, SQLiteHelper.ID_F + "=" + film.getIdFilmApi() + " AND " + SQLiteHelper.ID_T + "= "  + 3 , null);
            }
        }
        if(akcija.equals("odstrani")){
            Log.i("sled", "odstrani");

            Cursor cursor = database.query(SQLiteHelper.TABELA_SINH_SEZNAMI,  // a. table
                    SINH_SEZ_COLUMNS, // b. column names
                    SQLiteHelper.ID_F + "=" + film.getIdFilmApi() + " AND " +  SQLiteHelper.ID_T + "=? AND " + SQLiteHelper.AKCIJA +" = ?",// c. selections
                    new String[] {tk_tipa, "dodaj"}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            Log.i("cursor.size", cursor.getCount()+" " +film.getNaslov() );
            if ((cursor != null)&&(cursor.getCount()>0))
            {
                cursor.moveToFirst();
                database.delete(SQLiteHelper.TABELA_SINH_SEZNAMI, SQLiteHelper.ID_SINH_SEZ + "=" + cursor.getInt(0), null);
            }
            else
                database.insert(SQLiteHelper.TABELA_SINH_SEZNAMI, null, values);
        }
    }

    private SeznamAzure cursorToSeznam(Cursor cursor, String idUp) {
        SeznamAzure s = new SeznamAzure();
        s.setNaslovFilma(cursor.getString(1));
        s.setTkIdFilma(cursor.getInt(2));
        s.setTkIdTipa(cursor.getInt(3));
        s.setTkIdUporabnika(idUp);
        s.setUrlSlika(cursor.getString(4));
        Log.i("url slika", cursor.getString(4));
        return s;
    }
}
