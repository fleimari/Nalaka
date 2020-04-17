package com.example.nalaka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class luoArvostelu extends AppCompatActivity implements View.OnClickListener {

    Button btnKamera;
    ImageView ivAnnos;
    Spinner spinnerKaupunki, spinnerRavintola, spinnerTags;
    ArrayAdapter<CharSequence> adapterKaupungit, adapterRavintolat, adapterTags;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage; // kuvan liittämiseen jo valmiiksi reference
    JSONArray arr;
    ArrayList<String> kaupunkiList;
    ArrayList<String> ravintolaList;
    ArrayList<String> tagiList;
    ArrayAdapter<String> kaupunkiAdapter;
    ArrayAdapter<String> ravintolaAdapter;
    ArrayAdapter<String> tagiAdapter;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luo_arvostelu);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Realtime tietokanta reference
        kaupunkiList = new ArrayList<String>();
        kaupunkiList.add("");
        ravintolaList = new ArrayList<String>();
        ravintolaList.add("");
        tagiList = new ArrayList<String>();
        tagiList.add("");

        kaupunkiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kaupunkiList);
        kaupunkiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ravintolaAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ravintolaList);
        ravintolaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tagiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tagiList);
        tagiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        queue = Volley.newRequestQueue(this);


        // Storage tietokanta reference
        //storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();

         btnKamera = (Button) findViewById(R.id.buttonLisaaKuva);
         ivAnnos = (ImageView) findViewById(R.id.annosKuva);
         spinnerKaupunki = (Spinner) findViewById(R.id.spinnerKaupunki);
         spinnerRavintola = (Spinner) findViewById(R.id.spinnerRavintola);
         spinnerTags = (Spinner) findViewById(R.id.spinnerTags);

         spinnerKaupunki.setAdapter(kaupunkiAdapter);
         spinnerRavintola.setAdapter(ravintolaAdapter);
         spinnerTags.setAdapter(tagiAdapter);

        findViewById(R.id.button2).setOnClickListener(this);
        haeKaupungit();
        haeTagit();

        spinnerKaupunki.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                haeRavintolat(item.toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2){
            //lisaaArvostelu();
        }

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ivAnnos.setImageBitmap(bitmap);
    }

    public void otaKuva (View view){
        Intent intentKuva = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentKuva, 0);
    }


    public void valitseGalleria (View view){
        Intent intentGalleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGalleria.setType("image/* video/*");
        startActivityForResult(intentGalleria, 0);
    }


    public void lisaaArvostelu(){

        // Siinä on nyt asetettu arvot mutta eihän se ole kuin lukea nuista kentistä ja korvata arvot niillä
        String var = mDatabase.push().getKey();
        String kaupunki = "Tampere";
        String kuvaUrl = "https://img.devrant.com/devrant/rant/r_1973724_9QTSY.jpg";
        String otsikko = "Da best";
        String peukut = "19";
        String pisteet = "5";
        String ravintola = "Tampereen kepappila";
        String teksti = "Hjuva paikka tulla sina kaumaan";
        String user = "Pekka";
        String videoUrl = "https://www.youtube.com/watch?v=iL7nX9W3aOU";

        mDatabase.child("Arvostelut").child(var).child("Kaupunki").setValue(kaupunki);
        mDatabase.child("Arvostelut").child(var).child("KuvaUrl").setValue(kuvaUrl);
        mDatabase.child("Arvostelut").child(var).child("Otsikko").setValue(otsikko);
        mDatabase.child("Arvostelut").child(var).child("Peukut").setValue(peukut);
        mDatabase.child("Arvostelut").child(var).child("Pisteet").setValue(pisteet);
        mDatabase.child("Arvostelut").child(var).child("Ravintola").setValue(ravintola);
        mDatabase.child("Arvostelut").child(var).child("Teksti").setValue(teksti);
        mDatabase.child("Arvostelut").child(var).child("User").setValue(user);
        mDatabase.child("Arvostelut").child(var).child("VideoUrl").setValue(videoUrl);

        // Tähän pitää varmaan rakentaa joku for-looppi että osaa tehdä sen verran komentoja kuin halutaan lisätä tageja.
        String tag1 = "Kiinalainen";
        String tag2 = "Japanilainen";
        mDatabase.child("Arvostelut").child(var).child("Tags").push().setValue(tag1);
        mDatabase.child("Arvostelut").child(var).child("Tags").push().setValue(tag2);
    }

    public void lisaaTagi(String tagi){
        mDatabase.child("Tags").push().setValue(tagi);
    }

    public void lisaaKaupunki(String kaupunki, String ravintola){

        mDatabase.child("Kaupungit").child(kaupunki).push().setValue(ravintola);
    }

    public void haeKaupungit(){

        String url = "https://eighth-anvil-272013.firebaseio.com/Kaupungit.json?print=pretty";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray list = response.names();
                            for(int i = 0; i<response.length();i++){
                                kaupunkiList.add(list.get(i).toString());
                                kaupunkiAdapter.notifyDataSetChanged();
                            }
                        }catch (Exception e){}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void haeRavintolat(final String kaupunki){

        ravintolaList.clear();
        ravintolaList.add("");
        ravintolaAdapter.notifyDataSetChanged();
        String url = "https://eighth-anvil-272013.firebaseio.com/Kaupungit.json?print=pretty";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject arr = response.getJSONObject(kaupunki);
                            JSONArray testlist = arr.names();
                            for(int i =0;i<testlist.length();i++){
                                String testi = arr.getString(testlist.getString(i).toString());
                                ravintolaList.add(testi);
                                kaupunkiAdapter.notifyDataSetChanged();
                            }

                        }catch (Exception e){}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void haeTagit(){
        String url = "https://eighth-anvil-272013.firebaseio.com/Tags.json?print=pretty";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //JSONObject arr = response.getJSONObject("Tags");
                            JSONArray testlist = response.names();
                            for(int i =0;i<testlist.length();i++){
                                String testi = response.getString(testlist.getString(i).toString());
                                tagiList.add(testi);
                                tagiAdapter.notifyDataSetChanged();
                            }
                        }catch (Exception e){}
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
