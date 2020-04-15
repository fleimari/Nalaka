package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String [] data = {"Toni on homo", "Dis was d good pizza and my likes", "Pesäpallo on Suomen kansallispeli ja pelisäännöt sen yleinen perusta. Pesäpallon pelisääntöjä täydentää Pesäpallotuomarin ohjekirja, jossa on esimerkkejä sääntökohtien soveltamisesta ja suositeltavista tuomareiden menettelytavoista", "moi"};
        String [] otsikkoData = {"Oulun paras kaenkky", "NomNom all day", "Mikä laji?", "NuGet"};
        Integer[] imageArray = {R.drawable.pizzaimg,R.drawable.kebaba,R.drawable.susinom,R.drawable.mmmbeer};

        CustomAdapter adapter = new CustomAdapter(this, data, otsikkoData, imageArray);
        list = (ListView)findViewById(R.id.customList);
        list.setAdapter(adapter);

        findViewById(R.id.search_img_btn).setOnClickListener(this);
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
        if (v.getId() == R.id.btnSiirryHakuun)
        {
            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
        if (v.getId() == R.id.search_img_btn){
            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
    }
}
