package com.rvir.filmi.baza.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "FilmiDB";

    public static final String TABELA_FILMI = "filmi";
    public static final String ID_FILMA = "id_filma";
    public static final String ID_FILMA_API = "id_filma_api";
    public static final String NASLOV= "naslov";
    public static final String LETO_IZIDA = "leto_izida";
    public static final String KATEGORIJE = "kategorije";
    public static final String IGRALCI = "igralci";
    public static final String REZISERJI = "reziserji";
    public static final String OPIS= "opis";
    public static final String OCENA = "ocena";
    public static final String MOJA_OCENA = "mojaOcena";
    public static final String SLIKA = "urlDoSlike";
    public static final String VIDEO = "urlDoVidea";

    public static final String TK_ID_FILM = "tk_id_film";


    public static final String TABELA_TIP_SEZNAM = "tip_seznama";
    public static final String ID_TIPA= "id_tipa";
    public static final String NAZIV_T= "naziv";

    public static final String TABELA_SEZNAMI = "seznami";
    public static final String ID_SEZNAM = "id_zapisa";
    public static final String TK_ID_FILM_S = "tk_id_film";
    public static final String TK_ID_TIP= "tk_id_tipa";

    public static final String TABELA_REGISTRACIJA = "registriran";
    public static final String ID_REG = "id";
    public static final String REGISTRIRAN = "registriran";

    public static final String TABELA_SINH_SEZNAMI = "sinh_seznami";
    public static final String ID_SINH_SEZ = "id";
    public static final String NASLOV_F= "naslov_f";
    public static final String ID_F = "tk_id_filma";
    public static final String ID_T= "tk_id_tipa";
    public static final String URL_SLIKA="url_do_slike";
    public static final String AKCIJA="akcija";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FILMI_TABLE = "CREATE TABLE "+ TABELA_FILMI +" ( " +
                ID_FILMA+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID_FILMA_API+" INTEGER, "+
                NASLOV+" TEXT, "+
                LETO_IZIDA+" INTEGER, "+
                KATEGORIJE + " TEXT, " +
                IGRALCI+" TEXT, "+
                REZISERJI + " TEXT, " +
                OPIS+ " TEXT, "+
                OCENA + " TEXT, " +
                MOJA_OCENA + " TEXT, " +
                SLIKA + " TEXT, " +
                VIDEO + " TEXT) ";

        db.execSQL(CREATE_FILMI_TABLE);

         String CREATE_TIP_SEZNAMA_TABLE = "CREATE TABLE "+ TABELA_TIP_SEZNAM +" ( " +
                ID_TIPA +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAZIV_T +" TEXT) ";

        db.execSQL(CREATE_TIP_SEZNAMA_TABLE);

        String CREATE_SEZNAM_TABLE = "CREATE TABLE "+ TABELA_SEZNAMI +" ( " +
                ID_SEZNAM+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TK_ID_FILM+" INTEGER, " +
                TK_ID_TIP +" INTEGER) ";

        db.execSQL(CREATE_SEZNAM_TABLE);

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.NAZIV_T, "ogledan"); //id=1 v globalni
        db.insert(SQLiteHelper.TABELA_TIP_SEZNAM, null, values);
        values.put(SQLiteHelper.NAZIV_T, "priljubljen"); //id=2 v globalni
        db.insert(SQLiteHelper.TABELA_TIP_SEZNAM, null, values);
        values.put(SQLiteHelper.NAZIV_T, "wish"); //id=3 v globalni
        db.insert(SQLiteHelper.TABELA_TIP_SEZNAM, null, values);

        String CREATE_SINH_SEZNAM_TABLE = "CREATE TABLE "+ TABELA_SINH_SEZNAMI +" ( " +
                ID_SINH_SEZ+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NASLOV_F+" TEXT, " +
                ID_F + " INTEGER, "+
                ID_T + " INTEGER," +
                URL_SLIKA + " TEXT, " +
                AKCIJA + " TEXT) ";
        db.execSQL(CREATE_SINH_SEZNAM_TABLE);


        String CREATE_REGISTRACIJA_TABLE = "CREATE TABLE "+ TABELA_REGISTRACIJA + " ( " +
                ID_REG + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REGISTRIRAN+" TEXT)";

        db.execSQL(CREATE_REGISTRACIJA_TABLE);
        values = new ContentValues();
        values.put(SQLiteHelper.REGISTRIRAN, "false");
        db.insert(SQLiteHelper.TABELA_REGISTRACIJA, null, values);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_SEZNAMI);
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_TIP_SEZNAM);
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_FILMI);
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_SINH_SEZNAMI);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_REGISTRACIJA);

        this.onCreate(db);
    }
}
