package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Paasivu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paasivu);
    }

    public void goToLuoArvostelu(View view){
        Intent intent = new Intent(this, luoArvostelu.class);
        startActivity(intent);
    }
}
