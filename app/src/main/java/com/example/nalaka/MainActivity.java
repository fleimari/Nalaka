package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
