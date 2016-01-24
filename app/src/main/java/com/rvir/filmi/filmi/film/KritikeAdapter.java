package com.rvir.filmi.filmi.film;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Kritika;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.seznami.FragmentPriljubljeni;
import com.rvir.filmi.filmi.seznami.PriljubljeniInterface;

import java.util.ArrayList;

public class KritikeAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Kritika> data;
    private static LayoutInflater inflater = null;

    public KritikeAdapter(Activity a, ArrayList<Kritika> d) {
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
            vi = inflater.inflate(R.layout.custom_kritike_list, null);

        TextView avtor = (TextView) vi.findViewById(R.id.avtor);
        TextView mnenje = (TextView) vi.findViewById(R.id.mnenje);

        Kritika kritika = data.get(position);

        // Setting all values in listview
        avtor.setText(kritika.getAvtor());
        mnenje.setText(kritika.getBesedilo());

        return vi;
    }
}