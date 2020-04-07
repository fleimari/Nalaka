package com.example.nalaka;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArvosteluClass {

    String arvosteluId = "";
    String arvosteluTeksti = "";
    String kaupunki = "ei nain";
    String kuvaUrl = "";
    String otsikko = "";
    String ravintola = "";
    String username = "";
    String viedoUrl = "";

    String url = "https://eighth-anvil-272013.firebaseio.com/Arvostelut.json?print=pretty";
    JsonObjectRequest jsonObjectRequest;

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
                            arvosteluTeksti = arvosteluJson.getString("Arvosteluteksti");
                            kaupunki = arvosteluJson.getString("Kaupunki");
                            kuvaUrl = arvosteluJson.getString("KuvaUrl");
                            otsikko = arvosteluJson.getString("Otsikko");
                            ravintola = arvosteluJson.getString("Ravintola");
                            username = arvosteluJson.getString("Username");
                            viedoUrl = arvosteluJson.getString("VideoUrl");


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

}
