package com.rvir.filmi.filmi.seznami;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.filmi.MainActivity;

import java.net.MalformedURLException;

public class DeleteTask{

    private Activity a;

      public DeleteTask(Activity a, String tk_id_tipa, String tk_id_filma){

          this.a=a;
          SharedPreferences sharedpreferences = a.getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
          String idUp=sharedpreferences.getString("idUporabnika", null);
          Izbrisi task=new Izbrisi();
          task.execute(tk_id_tipa, idUp, tk_id_filma);

    }


    private class Izbrisi extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("on preExecte", "...jup");
            // Showing progress dialog
            //pDialog = new ProgressDialog(Registracija.this);
            //pDialog.setMessage("Registracija poteka...");
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... argumenti) {
            try {

                MobileServiceClient mClient;
                MobileServiceTable<SeznamAzure> mSeznamiTable=null;

                try {
                    // Create the Mobile Service Client instance, using the provided
                    mClient = new MobileServiceClient(
                            "https://filmi.azure-mobile.net/",
                            "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                            a);
                    mSeznamiTable = mClient.getTable(SeznamAzure.class);

                } catch (MalformedURLException e) {
                    //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
                }

                Log.i("v async je", mSeznamiTable.getTableName().toString());
                MobileServiceList<SeznamAzure> s=mSeznamiTable.where().field("tk_id_tipa").eq(argumenti[0]).and().field("tk_id_filma").eq(argumenti[2]).and().field("tk_id_uporabnika").eq(argumenti[1]).execute().get();

                Log.i("dolzina seznama", s.size()+"");

                mSeznamiTable.delete(s.get(0));


            } catch (Exception e) { }
            return null;
        }

        @Override
        protected void onPostExecute(String id) {
            //if(pDialog.isShowing())
            //   pDialog.dismiss();

        }
    }
}
