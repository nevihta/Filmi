package com.rvir.filmi.filmi.seznami;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.filmi.R;

import java.util.ArrayList;

public class SeznamiPriljubljeniAdapter extends BaseAdapter {
    PriljubljeniInterface priljubljeniInterface;
    private Activity activity;
    private ArrayList<Film> data;
    private static LayoutInflater inflater = null;

    public SeznamiPriljubljeniAdapter(Activity a, ArrayList<Film> d, FragmentPriljubljeni f) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        priljubljeniInterface = f;
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
            vi = inflater.inflate(R.layout.custom_seznami_priljubljeni_list, null);

        TextView title = (TextView) vi.findViewById(R.id.title);
        TextView kategorije = (TextView) vi.findViewById(R.id.kategorije);

        Film film = data.get(position);

        // Setting all values in listview
        title.setText(film.getNaslov());
        kategorije.setText(film.getKategorije());

        ImageView delete = (ImageView) vi.findViewById(R.id.delete);
        delete.setTag(new Integer(film.getIdFilma()));
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println(view.getTag().toString());
                priljubljeniInterface.remove(Integer.parseInt(view.getTag().toString()));
            }
        });

        ImageView share = (ImageView) vi.findViewById(R.id.share);
        share.setTag(new Integer(film.getIdFilma()));
        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println(view.getTag().toString());
                priljubljeniInterface.recommend(Integer.parseInt(view.getTag().toString()));
            }
        });

        return vi;
    }
}