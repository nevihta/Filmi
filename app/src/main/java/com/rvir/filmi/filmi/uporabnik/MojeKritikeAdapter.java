package com.rvir.filmi.filmi.uporabnik;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Kritika;
import com.rvir.filmi.filmi.R;

import java.util.ArrayList;

public class MojeKritikeAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Kritika> data;
    private static LayoutInflater inflater = null;

    public MojeKritikeAdapter(Activity a, ArrayList<Kritika> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.custom_seznami_moje_kritike_list, null);

        TextView film = (TextView) vi.findViewById(R.id.title);
        TextView mnenje = (TextView) vi.findViewById(R.id.kritika);

        Kritika kritika = data.get(position);

        // Setting all values in listview
        film.setText(kritika.getNaslovF());
        mnenje.setText(kritika.getBesedilo());

        return vi;
    }
}