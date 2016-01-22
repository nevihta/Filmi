package com.rvir.filmi.filmi.uporabnik;

import android.text.format.Time;
import android.util.Base64;
import android.util.Log;

import java.util.HashMap;

public class GeneratorKode {

    public String generirajKodo(){

        String koda="";

        int random = 1+(int)(Math.random()*8);  //ponovitev, kje se pojavi random številka

        HashMap <String, String> menjave = NapolniHashMap();


        Time cas = new Time();
        cas.setToNow();

        String osnova= cas.toString();

        Log.i("čas je", cas.toString());

        int j=1;
        for(int i=2; i<12; i=i+2){
                if(j==random)
                    koda+=1+(int)(Math.random()*9);
                j++;
             if(i==8)
                    i--;
                else
                    koda+=menjave.get(""+osnova.charAt(i)+osnova.charAt(i+1));

            Log.i("Koda " + i, koda);
        }
        koda+=osnova.charAt(13);
        koda+=osnova.charAt(14);

        Log.i("koda je:", koda);

        return koda;
    }

    private HashMap<String,String> NapolniHashMap() {

        HashMap <String, String> menjave= new HashMap<String, String>();
        menjave.put("00", "A");
        menjave.put("01", "B");
        menjave.put("02", "C");
        menjave.put("03", "D");
        menjave.put("04", "E");
        menjave.put("05", "F");
        menjave.put("06", "G");
        menjave.put("07", "H");
        menjave.put("08", "I");
        menjave.put("09", "J");
        menjave.put("10", "K");
        menjave.put("11", "L");
        menjave.put("12", "M");
        menjave.put("13", "N");
        menjave.put("14", "O");
        menjave.put("15", "P");
        menjave.put("16", "Q");
        menjave.put("17", "R");
        menjave.put("18", "S");
        menjave.put("19", "T");
        menjave.put("20", "U");
        menjave.put("21", "V");
        menjave.put("22", "W");
        menjave.put("23", "X");
        menjave.put("24", "Y");
        menjave.put("25", "Z");
        menjave.put("26", "a");
        menjave.put("27", "b");
        menjave.put("28", "c");
        menjave.put("29", "d");
        menjave.put("30", "e");
        menjave.put("31", "f");
        menjave.put("32", "g");
        menjave.put("33", "h");
        menjave.put("34", "i");
        menjave.put("35", "j");
        menjave.put("36", "k");
        menjave.put("37", "l");
        menjave.put("38", "m");
        menjave.put("39", "n");
        menjave.put("40", "o");
        menjave.put("41", "p");
        menjave.put("42", "q");
        menjave.put("43", "r");
        menjave.put("44", "s");
        menjave.put("45", "t");
        menjave.put("46", "u");
        menjave.put("47", "v");
        menjave.put("48", "w");
        menjave.put("49", "x");
        menjave.put("50", "y");
        menjave.put("51", "z");
        menjave.put("52", "0");
        menjave.put("53", "1");
        menjave.put("54", "2");
        menjave.put("55", "3");
        menjave.put("56", "4");
        menjave.put("57", "5");
        menjave.put("58", "6");
        menjave.put("59", "7");

        return menjave;
    }
}
