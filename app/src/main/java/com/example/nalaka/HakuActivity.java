package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class HakuActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    AutoCompleteTextView atv;
    String[] kaupungit = new String[]{"Helsinki", "Turku", "Tampere", "Oulu", "Vaasa", "Pori", "Rovaniemi", "Espoo", "Vantaa", "Lappeenranta", "Tornio", "Kemi", "Oulunsalo"};
    String[] ruokalajit = new String[]{"Pizza","Kebab","Porkkanalaatikko","Hapurilainen","Hummus","Keitto","Pihvi","Kalaa"};
    ArrayList<String> haettavatTagi = new ArrayList<>();
    ArrayList<String> haettavatTagit2 = new ArrayList<>();

    TextView textviewTagit;

    ListView myList;
    ListView myTagList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haku);
        textviewTagit = findViewById(R.id.TextViewTagit);

        findViewById(R.id.BtnTaginLisays).setOnClickListener(this);
        findViewById(R.id.BtnHaku).setOnClickListener(this);

        atv = findViewById(R.id.AutoHaku);
        atv.setAdapter(new ArrayAdapter<String>(HakuActivity.this, android.R.layout.simple_list_item_1, kaupungit));

        String [] data1 = {"Toni on homo", "Dis was d good pizza and my likes", "Pesäpallo on Suomen kansallispeli ja pelisäännöt sen yleinen perusta. Pesäpallon pelisääntöjä täydentää Pesäpallotuomarin ohjekirja, jossa on esimerkkejä sääntökohtien soveltamisesta ja suositeltavista tuomareiden menettelytavoista", "moi"};
        ArrayList<String> data = new ArrayList<>();
        Collections.addAll(data, data1);
        String [] otsikkoDataArray = {"Oulun paras kaenkky", "NomNom all day", "Mikä laji?", "NuGet"};
        ArrayList<String> otsikkoData = new ArrayList<>();
        Collections.addAll(otsikkoData, otsikkoDataArray);
        Integer[] imageArray = {R.drawable.pizzaimg,R.drawable.kebaba,R.drawable.susinom,R.drawable.mmmbeer};

        CustomAdapter adapter = new CustomAdapter(this, data, otsikkoData, imageArray);
        myList = findViewById(R.id.customListHaku);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(this);



        //String [] testiTagssia = {"testi1", "testi2", "testi3", "testi4"};
        //Collections.addAll(haettavatTagit2, testiTagssia);
        //Log.d("Testi", "onCreate: " + haettavatTagit2.get(0) + " " + haettavatTagit2.get(1));



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnTaginLisays)
        {

            if(atv.getText().toString() != null)
            {

                //textviewTagit.setText("Tagit: ");
                haettavatTagi.add(atv.getText().toString());
                Log.d("testia", "onClick: " + haettavatTagi.get(0));

            }
            atv.setText("");

            haettavatTagit2.add("testi1");

            //Log.d("Testi", "onCreate: " + haettavatTagit2.get(0) + " " + haettavatTagit2.get(1));
            CustomAdapaterTags tagAdapter = new CustomAdapaterTags(this, haettavatTagit2);
            myTagList = findViewById(R.id.customListTagit);
            myTagList.setAdapter(tagAdapter);
        }
        if (v.getId() == R.id.BtnHaku)
        {
            haettavatTagi.clear();
        }



    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioKaupunki:
                if (checked)
                    atv.setText("");
                    atv.setAdapter(new ArrayAdapter<String>(HakuActivity.this, android.R.layout.simple_list_item_1, kaupungit));
                    break;
            case R.id.radioRuokalaji:
                if (checked)
                    atv.setText("");
                    atv.setAdapter(new ArrayAdapter<String>(HakuActivity.this, android.R.layout.simple_list_item_1, ruokalajit));
                    break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.customListHaku)
        {
            Intent intentMainActivity = new Intent(this, MainActivity.class);
            startActivity(intentMainActivity);
        }
        Log.d("testic", "onItemClick: " + position + " " + id);
    }

    public void tagitTestaus()
    {

    }
}


