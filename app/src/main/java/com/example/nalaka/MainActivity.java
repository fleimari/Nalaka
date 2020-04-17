package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list;
    ArrayList<ArvosteluClass> arvostelutLista = new ArrayList<>();
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySingleton.getInstance(this);

        /*
        String [] data = {"Toni on homo", "Dis was d good pizza and my likes", "Pesäpallo on Suomen kansallispeli ja pelisäännöt sen yleinen perusta. Pesäpallon pelisääntöjä täydentää Pesäpallotuomarin ohjekirja, jossa on esimerkkejä sääntökohtien soveltamisesta ja suositeltavista tuomareiden menettelytavoista", "moi"};
        String [] otsikkoData = {"Oulun paras kaenkky", "NomNom all day", "Mikä laji?", "NuGet"};
        Integer[] imageArray = {R.drawable.pizzaimg,R.drawable.kebaba,R.drawable.susinom,R.drawable.mmmbeer};*/


        ArvosteluClass arvostelu1 = new ArvosteluClass("-M4TOEDQPKXuYKQmjL7S");
        ArvosteluClass arvostelu2 = new ArvosteluClass("-M4TPdXyfp3_W_KMlRle");
        ArvosteluClass arvostelu3 = new ArvosteluClass("-M4TQAt4t18s1SNpN-jC");
        ArvosteluClass arvostelu4 = new ArvosteluClass("-M4TQZ5XI0ikwHlYR_5y");
        arvostelutLista.add(arvostelu1);
        arvostelutLista.add(arvostelu2);
        arvostelutLista.add(arvostelu3);
        arvostelutLista.add(arvostelu4);


        adapter = new CustomAdapter(this, arvostelutLista);
        list = (ListView)findViewById(R.id.customList);
        list.setAdapter(adapter);

        findViewById(R.id.search_img_btn).setOnClickListener(this);
        findViewById(R.id.logo_btn).setOnClickListener(this);
        findViewById(R.id.menu_img_btn).setOnClickListener(this);

        MySingleton.getInstance(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 2000);

        findViewById(R.id.btnSiirryHakuun).setOnClickListener(this);


    }

    public void goToPaasivu(View view){
        Intent intent = new Intent(this, Paasivu.class);
        startActivity(intent);
    }

    public void goToLuoArvostelu(View view){
        Intent intentLuoArvostelu = new Intent(this, luoArvostelu.class);
        startActivity(intentLuoArvostelu);
    }

    public void goToArvostelusivu(View view){
        Intent intentArvostelusivu = new Intent(this, Arvostelusivu.class);
        startActivity(intentArvostelusivu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_img_btn) {
            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
        if (v.getId() == R.id.btnSiirryHakuun) {

            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
        if (v.getId() == R.id.logo_btn){
            Intent intentPaasivu = new Intent (this, Paasivu.class);
            startActivity(intentPaasivu);
        }
        if (v.getId() == R.id.menu_img_btn) {

        }
    }
}
