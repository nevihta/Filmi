package com.rvir.filmi.filmi.seznami;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Film;
import com.rvir.filmi.filmi.MainActivity;
import com.rvir.filmi.filmi.R;

import java.util.ArrayList;

public class SeznamiOgledaniAdapter extends BaseAdapter {
    OgledaniInterface ogledaniInterface;
    private Activity activity;
    private ArrayList<Film> data;
    private static LayoutInflater inflater = null;
    private boolean prijavljen=false;

    public SeznamiOgledaniAdapter(Activity a, ArrayList<Film> d, FragmentOgledani f) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ogledaniInterface = f;
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

        SharedPreferences sharedpreferences = activity.getSharedPreferences(MainActivity.seja, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("idUporabnika", null)!=null)
                 prijavljen=true;


        TextView title = (TextView) vi.findViewById(R.id.title);
        TextView kategorije = (TextView) vi.findViewById(R.id.kategorije);

        final Film film = data.get(position);

        // Setting all values in listview
        title.setText(film.getNaslov());
        kategorije.setText(film.getKategorije());

        ImageView delete = (ImageView) vi.findViewById(R.id.delete);
        delete.setTag(R.string.idFilma, new Integer(film.getIdFilma()));
        delete.setTag(R.string.idFilmaApi, new Integer(film.getIdFilmApi()));
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ogledaniInterface.remove(Integer.parseInt(view.getTag(R.string.idFilma).toString()), Integer.parseInt(view.getTag(R.string.idFilmaApi).toString()));
            }
        });


        ImageView write = (ImageView) vi.findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ogledaniInterface.writeReview(film.getIdFilmApi(), film.getNaslov());
            }
        });



        if(!prijavljen){
            write.setVisibility(View.GONE);
        }

        return vi;
    }
}