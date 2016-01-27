package com.rvir.filmi.filmi.priporocila;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.baza.beans.Priporoci;
import com.rvir.filmi.filmi.R;
import com.rvir.filmi.filmi.seznami.FragmentOgledani;

import java.util.ArrayList;

public class PriporocilaAdapter extends BaseAdapter {
    PriporocilaInterface priporocilaInterface;
    private Activity activity;
    private ArrayList<Priporoci> data;
    private static LayoutInflater inflater = null;

    public PriporocilaAdapter(Activity a, ArrayList<Priporoci> d, FragmentPrijateljiPriporocila f) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        priporocilaInterface = f;
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
            vi = inflater.inflate(R.layout.custom_priporocila_prijatelji_list, null);

        TextView title = (TextView) vi.findViewById(R.id.title);
        //TextView movie = (TextView) vi.findViewById(R.id.movie);

        final Priporoci priporocilo = data.get(position);

        String message = "Oseba "+priporocilo.getUp_kdo()+" vam je priporočila ogled filma "+priporocilo.getNaslov_f();
        // Setting all values in listview

        title.setText(message);
        //movie.setText(priporocilo.getNaslov_f());
        ImageView delete = (ImageView) vi.findViewById(R.id.delete);
        //delete.setTag(priporocilo);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                priporocilaInterface.remove(priporocilo);
            }
        });

        return vi;
    }
}