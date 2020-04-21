package com.example.nalaka;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ArvosteluClass implements Serializable {

    String arvosteluId = "";
    String arvosteluTeksti = "";
    String kaupunki = "";
    String kuvaUrl = "";
    String otsikko = "";
    String ravintola = "";
    String username = "";
    String viedoUrl = "";
    String peukut = "";
    String pisteet = "0";
    ArrayList<String> tagit = new ArrayList<>();


    String url = "https://eighth-anvil-272013.firebaseio.com/Arvostelut.json?print=pretty";
    public static transient JsonObjectRequest jsonObjectRequest;
    public static transient ImageLoader imageLoader;

    public ArvosteluClass(String id) {
        arvosteluId = id;
        haeJson();

    }

    public String getArvosteluTeksti() {
        return arvosteluTeksti;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public String getKuvaUrl() {
        return kuvaUrl;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public String getRavintola() {
        return ravintola;
    }

    public String getUsername() {
        return username;
    }

    public String getViedoUrl() {
        return viedoUrl;
    }

    public String getPeukut() { return peukut; }

    public String getPisteet() { return pisteet; }

    public ArrayList<String> getTagit() { return tagit; }

    public void haeJson()
    {
        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("testia", "onResponse:" + response.toString());
                        Log.d("testia", "onResponse:" + arvosteluId);

                        try {

                            JSONObject arvosteluJson = response.getJSONObject(arvosteluId);
                            arvosteluTeksti = arvosteluJson.getString("Teksti");
                            kaupunki = arvosteluJson.getString("Kaupunki");
                            kuvaUrl = arvosteluJson.getString("KuvaUrl");
                            otsikko = arvosteluJson.getString("Otsikko");
                            ravintola = arvosteluJson.getString("Ravintola");
                            username = arvosteluJson.getString("User");
                            viedoUrl = arvosteluJson.getString("VideoUrl");
                            peukut = arvosteluJson.getString("Peukut");
                            pisteet = arvosteluJson.getString("Pisteet");

                            JSONObject tagi = arvosteluJson.getJSONObject("Tags");
                            JSONArray tagiArr = tagi.names();

                            for (int i = 0; i < tagiArr.length(); i++) {
                                tagit.add(tagi.getString(tagiArr.get(i).toString()));
                                //Log.d("testiTagit", "onResponse: " + tagi.getString(tagiArr.get(i).toString()));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("testia", "onResponse: ei onnistunut");
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("testia", "onErrorResponse: ei toimi");

                    }
                });

        MySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void haeKuva()
    {

    }
}
