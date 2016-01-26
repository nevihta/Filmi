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
import com.rvir.filmi.baza.beans.SeznamAzure;
import com.rvir.filmi.filmi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SeznamiPrijateljAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<SeznamAzure> data;
    private static LayoutInflater inflater = null;

    public SeznamiPrijateljAdapter(Activity a, ArrayList<SeznamAzure> d) {
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
            vi = inflater.inflate(R.layout.custom_filmi_list, null);

        TextView title = (TextView) vi.findViewById(R.id.txt);
        ImageView imageIcon = (ImageView) vi.findViewById(R.id.flag);

        SeznamAzure sa = data.get(position);

        // Setting all values in listview
        title.setText(sa.getNaslovFilma());
        Picasso.with(activity.getBaseContext())
                .load(sa.getUrlSlika())
                .resize(150, 150)
                .into(imageIcon);
        return vi;
    }
}