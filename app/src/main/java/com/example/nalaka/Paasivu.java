package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Paasivu extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    ArrayList<ArvosteluClass> arvostelutLista = new ArrayList<>();
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paasivu);

        MySingleton.getInstance(this);

        ArvosteluClass arvostelu1 = new ArvosteluClass("-M4TOEDQPKXuYKQmjL7S");
        ArvosteluClass arvostelu2 = new ArvosteluClass("-M4TPdXyfp3_W_KMlRle");
        ArvosteluClass arvostelu3 = new ArvosteluClass("-M4TQAt4t18s1SNpN-jC");
        ArvosteluClass arvostelu4 = new ArvosteluClass("-M4TQZ5XI0ikwHlYR_5y");
        arvostelutLista.add(arvostelu1);
        arvostelutLista.add(arvostelu2);
        arvostelutLista.add(arvostelu3);
        arvostelutLista.add(arvostelu4);

        adapter = new CustomAdapter(this, arvostelutLista);
        list = (ListView)findViewById(R.id.listViewPaasivu);
        list.setAdapter(adapter);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                updateList();

            }
        }, 2000);

    }

    public void goToLuoArvostelu(View view){
        Intent intent = new Intent(this, luoArvostelu.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Log.d("testiPaa", "onItemClick: " + arvostelutLista.get(position).getOtsikko());

        Intent intent = new Intent(this, Arvostelusivu.class);
        intent.putExtra("Arvostelu",arvostelutLista.get(position));
        startActivity(intent);
    }

    public void updateList()
    {
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(this);
    }
}