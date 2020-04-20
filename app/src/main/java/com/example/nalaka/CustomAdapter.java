package com.example.nalaka;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<ArvosteluClass> {

    private  Context context;
    private ArrayList<ArvosteluClass> arvostelutList = new ArrayList<>();
    //private final String[] data;
    //private final String[] otsikkoData;
    //private final Integer[] imageIDarray;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, ArrayList<ArvosteluClass> list){
        super(context, 0, list);
        this.context = context;
        this.arvostelutList = list;
    }

    /*
    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }*/


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(vi == null)
            vi = LayoutInflater.from(context).inflate(R.layout.list_row,parent,false);
            //vi = inflater.inflate(R.layout.list_row, null);

        ArvosteluClass currentArvostelu = arvostelutList.get(position);




        TextView text = (TextView) vi.findViewById(R.id.arvosteluteksti);
        TextView title = vi.findViewById(R.id.title);
        ImageView image = vi.findViewById(R.id.listIMG);

        RatingBar stars = (RatingBar) vi.findViewById(R.id.starRating);
        stars.setRating(Float.parseFloat(currentArvostelu.getPisteet()));

        text.setText(currentArvostelu.getArvosteluTeksti());
        title.setText(currentArvostelu.getOtsikko());
        //image.setImageURI(currentArvostelu.getKuvaUrl());

        return vi;
    }
}
