package com.example.nalaka;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<ArvosteluClass> {

    private  Context context;
    private ArrayList<ArvosteluClass> arvostelutList = new ArrayList<>();
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, ArrayList<ArvosteluClass> list){
        super(context, 0, list);
        this.context = context;
        this.arvostelutList = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(vi == null)
            vi = LayoutInflater.from(context).inflate(R.layout.list_row,parent,false);

        ArvosteluClass currentArvostelu = arvostelutList.get(position);

        TextView text = (TextView) vi.findViewById(R.id.arvosteluteksti);
        TextView title = vi.findViewById(R.id.title);
        TextView ravintola = vi.findViewById(R.id.textViewLisKauptJaRavintola);
        TextView tagit = vi.findViewById(R.id.textViewListTagit);
        final ImageView image = (ImageView) vi.findViewById(R.id.listIMG);
        VideoView video = (VideoView) vi.findViewById(R.id.listVideo);

        RatingBar stars = (RatingBar) vi.findViewById(R.id.starRating);
        stars.setRating(Float.parseFloat(currentArvostelu.getPisteet()));

        text.setText(currentArvostelu.getArvosteluTeksti());
        title.setText(currentArvostelu.getOtsikko());
        ravintola.setText(currentArvostelu.getRavintola() + " | ");
        ravintola.append(currentArvostelu.getKaupunki());


        tagit.setText("Tagit:");

        for (int i = 0; i < currentArvostelu.getTagit().size(); i ++)
        {
            tagit.append(" " + currentArvostelu.getTagit().get(i));
        }


        Log.d("Testi2", "getView: " + currentArvostelu.getKuvaUrl());

        if ( currentArvostelu.getKuvaUrl().isEmpty())
        {
            if (currentArvostelu.getViedoUrl().isEmpty())
            {
                image.setImageResource(R.drawable.pizzaimg);
                //image.setVisibility(View.INVISIBLE);
                //video.setVisibility(View.VISIBLE);
            }
            image.setImageResource(R.drawable.pizzaimg);
            //Uri videoUri = Uri.parse(currentArvostelu.getViedoUrl());
            //video.setVideoURI(videoUri);
        }
        else
        {
            //image.setVisibility(View.VISIBLE);
            //video.setVisibility(View.INVISIBLE);
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

