package com.rvir.filmi.filmi.seznami;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.filmi.R;

import java.util.ArrayList;

public class SeznamiOgledaniAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Film> data;
    private static LayoutInflater inflater = null;

    public SeznamiOgledaniAdapter(Activity a, ArrayList<Film> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.custom_seznami_ogledani_list, null);

        TextView title = (TextView) vi.findViewById(R.id.title);
        TextView kategorije = (TextView) vi.findViewById(R.id.kategorije);

        Film film = data.get(position);

        // Setting all values in listview
        title.setText(film.getNaslov());
        kategorije.setText(film.getKategorije());
        return vi;
    }
}