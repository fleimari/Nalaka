package com.example.nalaka;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.InputStream;
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
        final ImageView image = (ImageView) vi.findViewById(R.id.listIMG);
        VideoView video = (VideoView) vi.findViewById(R.id.listVideo);

        RatingBar stars = (RatingBar) vi.findViewById(R.id.starRating);
        stars.setRating(Float.parseFloat(currentArvostelu.getPisteet()));

        text.setText(currentArvostelu.getArvosteluTeksti());
        title.setText(currentArvostelu.getOtsikko());



        Log.d("Testi2", "getView: " + currentArvostelu.getKuvaUrl());

        if ( currentArvostelu.getKuvaUrl().isEmpty())
        {
            if (currentArvostelu.getViedoUrl().isEmpty())
            {
                image.setImageResource(R.drawable.pizzaimg);
                vi.findViewById(R.id.listVideo);
            }

        }
        else
        {
            ImageRequest imageRequest = new ImageRequest(currentArvostelu.getKuvaUrl(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            image.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            image.setImageResource(R.drawable.kebaba);
                        }
                    });
            MySingleton.getInstance().addToRequestQueue(imageRequest);
        }


        return vi;
    }

}

