package com.rvir.filmi.filmi.priporocila;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Priporoci;
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.baza.beans.Uporabniki;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.film.FilmActivity;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class FragmentPrijateljiPriporocila extends Fragment implements PriporocilaInterface{
    View view = null;
    private ArrayList<Priporoci> priporoceniFilmi = null;
    private MobileServiceClient mClient;
    private MobileServiceTable<Priporoci> mPriporociTable;
    String idUp="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.content_priporocila_fragment_prijatelji, container, false);

        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null)
            idUp=sharedpreferences.getString("idUporabnika", null);


        try {
            // Create the Mobile Service Client instance, using the provided
            mClient = new MobileServiceClient(
                    "https://filmi.azure-mobile.net/",
                    "RlptNyMuuAbjJOmVDoQYBmvhLUgjam37",
                    getContext());
            mPriporociTable = mClient.getTable(Priporoci.class);

        } catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }

        GetPrijateljiPriporoceniTask task = new GetPrijateljiPriporoceniTask();
        task.execute();

        return view;

    }

    private class GetPrijateljiPriporoceniTask extends AsyncTask<String, Void, MobileServiceList<Priporoci>> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Prosimo, počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected MobileServiceList<Priporoci> doInBackground(String... urls) {
            MobileServiceList<Priporoci> result = null;

            try {
                result= mPriporociTable.where().field("id_komu").eq(idUp).execute().get();
            } catch (Exception e) { }
            return result;

        }

        @Override
        protected void onPostExecute(MobileServiceList<Priporoci> result) {
            if(pDialog.isShowing())
                pDialog.dismiss();

            priporoceniFilmi = result;


                ListView listView = ( ListView ) view.findViewById(R.id.listPrijateljiPriporocila);
                PriporocilaAdapter pa = new PriporocilaAdapter(getActivity(), priporoceniFilmi, FragmentPrijateljiPriporocila.this);
                listView.setAdapter(pa);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int idFilma = Integer.parseInt(priporoceniFilmi.get(position).getId_film());

                        Intent myIntent = new Intent(view.getContext(), FilmActivity.class);
                        myIntent.putExtra("id", idFilma);
                        startActivity(myIntent);

                    }
                });


        }
    }

    @Override
    public View remove(Priporoci p) {
       // System.out.println("remove priporocilo "+idFilmaApi);
      //odstrani iz seznama

        mPriporociTable.delete(p);

        GetPrijateljiPriporoceniTask task = new GetPrijateljiPriporoceniTask();
        task.execute();

        return view;
    }

}
