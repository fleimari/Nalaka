package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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

public class HakuActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //ArrayList<String> haettavatTagi = new ArrayList<>();
    Spinner spinnerKaupunki, spinnerRavintola, spinnerTags;
    ArrayList<String> kaupunkiList;
    ArrayList<String> ravintolaList;
    ArrayList<String> tagiList;
    ArrayAdapter<String> kaupunkiAdapter,ravintolaAdapter,tagiAdapter;
    ArrayList<ArvosteluClass> arvostelutHakuun = new ArrayList<>();
    ListView list;
    CustomAdapter adapter;
    boolean onkoTagi = false;



    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haku);

        kaupunkiList = new ArrayList<String>();
        ravintolaList = new ArrayList<String>();
        tagiList = new ArrayList<String>();
        ravintolaList.add("Valitse");
        ravintolaList.add("Kaikki");

        queue = Volley.newRequestQueue(this);

        findViewById(R.id.BtnHaku).setOnClickListener(this);
        findViewById(R.id.logo_btn).setOnClickListener(this);
        findViewById(R.id.menu_img_btn).setOnClickListener(this);

        kaupunkiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kaupunkiList);
        kaupunkiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ravintolaAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ravintolaList);
        ravintolaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tagiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tagiList);
        tagiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerKaupunki = (Spinner) findViewById(R.id.spinnerKaupunki2);
        spinnerRavintola = (Spinner) findViewById(R.id.spinnerRavintola2);
        spinnerTags = (Spinner) findViewById(R.id.spinnerTags2);

        spinnerKaupunki.setAdapter(kaupunkiAdapter);
        spinnerRavintola.setAdapter(ravintolaAdapter);
        spinnerTags.setAdapter(tagiAdapter);

        haeKaupungit();
        haeTagit();

        spinnerKaupunki.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                haeRavintolat(item.toString());

                spinnerRavintola.setSelection(0);


            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerRavintola.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

                spinnerKaupunki.setSelection(0);
                spinnerRavintola.setSelection(0);

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void haeKaupungit(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                kaupunkiList.clear();
                kaupunkiList.add("Valitse");
                String url = "https://eighth-anvil-272013.firebaseio.com/Kaupungit.json?print=pretty";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray list = response.names();
                                    for(int i = 0; i<response.length();i++){
                                        kaupunkiList.add(list.get(i).toString());
                                    }
                                    kaupunkiAdapter.notifyDataSetChanged();
                                }catch (Exception e){}
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

    public void haeRavintolat(final String kaupunki){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ravintolaList.clear();
                ravintolaList.add("Valitse");
                ravintolaList.add("Kaikki");
                String url = "https://eighth-anvil-272013.firebaseio.com/Kaupungit.json?print=pretty";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject arr = response.getJSONObject(kaupunki);
                                    JSONArray testlist = arr.names();
                                    for(int i =0;i<testlist.length();i++){
                                        String ravintola = arr.getString(testlist.getString(i).toString());
                                        ravintolaList.add(ravintola);
                                    }
                                    kaupunkiAdapter.notifyDataSetChanged();

                                }catch (Exception e){}
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

    public void haeTagit(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tagiList.add("Valitse");
                String url = "https://eighth-anvil-272013.firebaseio.com/Tags.json?print=pretty";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray testlist = response.names();
                                    for(int i =0;i<testlist.length();i++){
                                        String testi = response.getString(testlist.getString(i).toString());
                                        tagiList.add(testi);
                                    }
                                    tagiAdapter.notifyDataSetChanged();
                                }catch (Exception e){}
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnHaku)
        {
            arvostelutHakuun.clear();
            if (!spinnerKaupunki.getSelectedItem().toString().equals("Valitse"))
            {
                Log.d("Testia5", "run: " + spinnerRavintola.getSelectedItem().toString() + "  " +spinnerKaupunki.getSelectedItem().toString() );
                if (spinnerRavintola.getSelectedItem().toString().equals("Valitse")|| spinnerRavintola.getSelectedItem().toString().equals("Kaikki"))
                {
                    String kaupunki = spinnerKaupunki.getSelectedItem().toString();
                    kaupunki = kaupunki.replaceAll(" ", "%20");
                    String url = "https://eighth-anvil-272013.firebaseio.com/Arvostelut.json?orderBy=%22Kaupunki%22&equalTo=%22" + kaupunki + "%22&print=pretty";
                    getDatRavintolaJson(url);
                }
                else
                {
                    String ravintola = spinnerRavintola.getSelectedItem().toString();

                    ravintola = ravintola.replaceAll(" ", "%20");
                    String url = "https://eighth-anvil-272013.firebaseio.com/Arvostelut.json?orderBy=%22Ravintola%22&equalTo=%22" + ravintola + "%22&print=pretty";
                    getDatRavintolaJson(url);

                }

            }
            else if (!spinnerTags.getSelectedItem().toString().equals("Valitse") )
            {
                String url = "https://eighth-anvil-272013.firebaseio.com/Arvostelut.json?print=pretty";
                getDatRavintolaJson(url);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        String tagi = spinnerTags.getSelectedItem().toString();
                        Log.d("Testia5", "run: " + arvostelutHakuun.size());
                        for (int i = arvostelutHakuun.size()-1; i >= 0 ; i--)
                        {
                            for ( int j = 0 ; j < arvostelutHakuun.get(i).getTagit().size() ; j++ )
                            {
                                Log.d("Testia5", "run: " + arvostelutHakuun.get(i).getTagit().get(j) + "  " + tagi);
                                if ( arvostelutHakuun.get(i).getTagit().get(j).equals(tagi) )
                                {

                                    onkoTagi = true;
                                }
                            }
                            if (onkoTagi == false)
                            {
                                arvostelutHakuun.remove(i);
                            }
                            else
                            {
                                onkoTagi = false;
                            }
                        }
                        Log.d("Testia5", "run: " + arvostelutHakuun.size());
                    }
                }, 5000);


            }


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    updateList();
                }
            }, 1000);


            adapter = new CustomAdapter(this, arvostelutHakuun);
            list = (ListView)findViewById(R.id.listViewHaku);
            list.setAdapter(adapter);
        }
        if (v.getId() == R.id.logo_btn){
            Intent intentPaasivu = new Intent (this, Paasivu.class);
            startActivity(intentPaasivu);
        }
        if (v.getId() == R.id.menu_img_btn) {
            PopupMenu popup = new PopupMenu(HakuActivity.this, findViewById(R.id.menu_img_btn));
            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.three:
                            Toast.makeText(HakuActivity.this, "Farewell", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                            return true;
                        case R.id.two:
                            Toast.makeText(HakuActivity.this, "Nothing here sorry", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.one:
                            Intent intentPaasivu = new Intent (HakuActivity.this, Paasivu.class);
                            startActivity(intentPaasivu);
                            Toast.makeText(HakuActivity.this, "Pääsivu", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
        }

    }

    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void getDatRavintolaJson(final String urli){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                String url = urli;
                Log.d("Testia3", "run: " + url);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONArray testlist = response.names();
                                    for(int i =0;i<testlist.length();i++){
                                        String id = testlist.getString(i).toString();
                                        ArvosteluClass arvostelu = new ArvosteluClass(id);
                                        arvostelutHakuun.add(arvostelu);
                                    }
                                }catch (Exception e){ Log.d("Testia3", "run: ei toimi1" + e);}
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

    public void updateList()
    {
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, Arvostelusivu.class);
        intent.putExtra("Arvostelu",arvostelutHakuun.get(position));
        startActivity(intent);
    }
}
