package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

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

    }

    public void goToPaasivu(View view){
        Intent intent = new Intent(this, Paasivu.class);
        startActivity(intent);
    }
}
