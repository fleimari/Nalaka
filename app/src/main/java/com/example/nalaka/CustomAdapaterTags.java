package com.example.nalaka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapaterTags extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public CustomAdapaterTags(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
        TextView myTextView = (TextView) vi.findViewById(R.id.textViewTag);
        myTextView.setText(data.get(position));
        return vi;
    }
}
