package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArvosteluClass testiOlio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSiirryHakuun).setOnClickListener(this);

        MySingleton.getInstance(this);

        testiOlio = new ArvosteluClass("Arvostelu2");

    }

    public void goToPaasivu(View view){
        Intent intent = new Intent(this, Paasivu.class);
        startActivity(intent);
    }

    public void goToLuoArvostelu(View view){
        Intent intentLuoArvostelu = new Intent(this, luoArvostelu.class);
        startActivity(intentLuoArvostelu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSiirryHakuun)
        {
            Log.d("testia", "onClick: " + testiOlio.getArvosteluTeksti()+ " " + testiOlio.getKaupunki());
            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
    }
}
