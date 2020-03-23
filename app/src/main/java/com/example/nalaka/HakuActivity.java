package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class HakuActivity extends AppCompatActivity implements View.OnClickListener {

    AutoCompleteTextView atv;
    String[] kaupungit = new String[]{"Helsinki", "Turku", "Tampere", "Oulu", "Vaasa", "Pori", "Rovaniemi", "Espoo", "Vantaa", "Lappeenranta", "Tornio", "Kemi", "Oulunsalo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haku);

        findViewById(R.id.BtnHaku).setOnClickListener(this);
        atv = findViewById(R.id.AutoHaku);
        atv.setAdapter(new ArrayAdapter<String>(HakuActivity.this, android.R.layout.simple_list_item_1, kaupungit));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnHaku)
        {

        }

    }
}
