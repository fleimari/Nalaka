package com.example.nalaka;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final String[] data;
    private final String[] otsikkoData;
    private final Integer[] imageIDarray;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, String[] data, String[] otsikkoData, Integer[] imageIDarray){

        this.context = context;
        this.data = data;
        this.otsikkoData = otsikkoData;
        this.imageIDarray = imageIDarray;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null)
            vi = inflater.inflate(R.layout.list_row, null);
        TextView text = (TextView) vi.findViewById(R.id.arvosteluteksti);
        TextView title = vi.findViewById(R.id.title);
        ImageView image = vi.findViewById(R.id.listIMG);
        text.setText(data[position]);
        title.setText(otsikkoData[position]);
        image.setImageResource(imageIDarray[position]);
        return vi;
    }
}
