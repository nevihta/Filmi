package com.rvir.filmi.filmi.uporabnik;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rvir.filmi.baza.beans.Prijatelji;
import com.rvir.filmi.filmi.R;

import java.util.ArrayList;

public class PrijateljiAdapter extends BaseAdapter {
    PrijateljiInterface prijateljiInterface;
    private Activity activity;
    private ArrayList<Prijatelji> data;
    private static LayoutInflater inflater = null;

    public PrijateljiAdapter(Activity a, ArrayList<Prijatelji> d, PrijateljiActivity p) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        prijateljiInterface=p;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.custom_prijatelji, null);

        TextView title = (TextView) vi.findViewById(R.id.title);

        Prijatelji prijatelji = data.get(position);

        // Setting all values in listview
        title.setText(prijatelji.getUp_ime());

        ImageView delete = (ImageView) vi.findViewById(R.id.delete);
        delete.setTag(prijatelji);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println(view.getTag().toString());
                prijateljiInterface.remove((Prijatelji)view.getTag());
            }
        });

        return vi;
    }
}

