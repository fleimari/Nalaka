package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

public class Paasivu extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView list;
    ArrayList<ArvosteluClass> arvostelutLista = new ArrayList<>();
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paasivu);

        MySingleton.getInstance(this);
        findViewById(R.id.search_img_btn).setOnClickListener(this);
        findViewById(R.id.logo_btn).setOnClickListener(this);
        findViewById(R.id.menu_img_btn).setOnClickListener(this);

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
        intent.putExtra("AlueetClass",arvostelutLista.get(position));
        startActivity(intent);
    }

    public void updateList()
    {
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_img_btn) {
            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
        if (v.getId() == R.id.logo_btn){
            Intent intentPaasivu = new Intent (this, Paasivu.class);
            startActivity(intentPaasivu);
        }
        if (v.getId() == R.id.menu_img_btn) {
            PopupMenu popup = new PopupMenu(Paasivu.this, findViewById(R.id.menu_img_btn));
            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.three:
                            Toast.makeText(Paasivu.this, "Farewell", Toast.LENGTH_SHORT).show();
                            finish();
                            return true;
                        case R.id.two:
                            Toast.makeText(Paasivu.this, "Nothing here sorry", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.one:
                            Intent intentPaasivu = new Intent (Paasivu.this, Paasivu.class);
                            startActivity(intentPaasivu);
                            Toast.makeText(Paasivu.this, "Pääsivu", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
        }
    }
}