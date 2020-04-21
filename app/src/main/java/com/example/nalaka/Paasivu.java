package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Paasivu extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView list;
    ArrayList<ArvosteluClass> arvostelutLista = new ArrayList<>();
    CustomAdapter adapter;
    RequestQueue queue;
    ArrayList<String> idList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paasivu);
        queue = Volley.newRequestQueue(this);
        
        MySingleton.getInstance(this);
        findViewById(R.id.search_img_btn).setOnClickListener(this);
        findViewById(R.id.logo_btn).setOnClickListener(this);
        findViewById(R.id.menu_img_btn).setOnClickListener(this);

        getDatRavintolaJson();

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

    public void getDatRavintolaJson(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String url = "https://eighth-anvil-272013.firebaseio.com/Arvostelut.json?print=pretty";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray testlist = response.names();
                            for(int i =0;i<testlist.length();i++){
                                String id = testlist.getString(i).toString();
                                ArvosteluClass arvostelu = new ArvosteluClass(id);
                               arvostelutLista.add(arvostelu);
                            }
                        }catch (Exception e){ }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsonObjectRequest);
            }
        });
    }
}