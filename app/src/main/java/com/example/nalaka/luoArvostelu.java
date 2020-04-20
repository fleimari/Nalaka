package com.example.nalaka;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class luoArvostelu extends AppCompatActivity implements View.OnClickListener {

    Button btnKuva, btnVideo;
    ImageView picAnnos;
    VideoView vidAnnos;
    Spinner spinnerKaupunki, spinnerRavintola, spinnerTags;
    EditText otsikko,arvostelu;
    RatingBar tahdet;
    private static final int GALLERY_PICTURE_REQUEST = 1;
    private static final int GALLERY_VIDEO_REQUEST = 2;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage; // kuvan liittämiseen jo valmiiksi reference
    ArrayList<String> kaupunkiList;
    ArrayList<String> ravintolaList;
    ArrayList<String> tagiList;
    ArrayAdapter<String> kaupunkiAdapter,ravintolaAdapter,tagiAdapter;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luo_arvostelu);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Realtime tietokanta reference
        kaupunkiList = new ArrayList<String>();
        ravintolaList = new ArrayList<String>();
        tagiList = new ArrayList<String>();

        kaupunkiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kaupunkiList);
        kaupunkiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ravintolaAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ravintolaList);
        ravintolaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tagiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tagiList);
        tagiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        queue = Volley.newRequestQueue(this);

        otsikko = findViewById(R.id.annoksenNimi);
        arvostelu = findViewById(R.id.etArvosteluTxt);
        tahdet = findViewById(R.id.ratingBar);
        //Storage tietokanta reference
        //storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();

         btnKuva = (Button) findViewById(R.id.buttonKuva);
         btnVideo = (Button) findViewById(R.id.buttonVideo);
         picAnnos = (ImageView) findViewById(R.id.annosKuva);
         vidAnnos = (VideoView) findViewById(R.id.annosVideo);

         spinnerKaupunki = (Spinner) findViewById(R.id.spinnerKaupunki);
         spinnerRavintola = (Spinner) findViewById(R.id.spinnerRavintola);
         spinnerTags = (Spinner) findViewById(R.id.spinnerTags);

         spinnerKaupunki.setAdapter(kaupunkiAdapter);
         spinnerRavintola.setAdapter(ravintolaAdapter);
         spinnerTags.setAdapter(tagiAdapter);

        findViewById(R.id.buttonJulkaiseArvostelu).setOnClickListener(this);
        haeKaupungit();
        haeTagit();
        haeRavintolat("Oulu");

        spinnerKaupunki.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.toString() == "Lisää kaupunki"){
                        //popUpKaupunki("Lisää kaupunki");
                        //ravintolaList.clear();
                        //ravintolaList.add("Valitse");
                        //ravintolaAdapter.notifyDataSetChanged();
                    arvostelu.setText("Lisää kaupunki valittu");
                }else {
                    haeRavintolat(item.toString());
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerRavintola.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.toString() == "Lisää ravintola"){
                    //popUpRavintola("Lisää ravintola");
                    arvostelu.setText("Lisää ravintola valittu");
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.toString() == "Lisää tagi"){
                    arvostelu.setText("Lisää tagi valittu");
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonJulkaiseArvostelu){
            if(otsikko.length() != 0){
                if(spinnerKaupunki.getSelectedItem().toString() != "Valitse"){
                    if(spinnerRavintola.getSelectedItem().toString() != "Valitse"){
                        if(spinnerTags.getSelectedItem().toString() != "Valitse"){
                            if(arvostelu.length() != 0){
                                String kaupunki = spinnerKaupunki.getSelectedItem().toString();
                                String ravintola = spinnerRavintola.getSelectedItem().toString();
                                String tag = spinnerTags.getSelectedItem().toString();
                                String nimi = otsikko.getText().toString();
                                String pisteet = Float.toString(tahdet.getRating());
                                String pohdinta = arvostelu.getText().toString();
                                lisaaArvostelu(kaupunki,nimi,pisteet,ravintola,pohdinta,tag);
                                Toast.makeText(this, "Arvostelu lisätty", Toast.LENGTH_SHORT).show();
                                otsikko.setText("");
                                arvostelu.setText("");
                                tahdet.setRating(0);
                                spinnerTags.setSelection(0);
                                spinnerRavintola.setSelection(0);
                                spinnerKaupunki.setSelection(0);
                            }else{
                                Toast.makeText(this, "Arvostelun teksti puuttuu", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(this, "Tagia ei valittu", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Ravintolaa ei valittu", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Kaupunkia ei valittu", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Annoksen nimi puuttuu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri kuvaData = data.getData();
            picAnnos.setImageURI(kuvaData);

            picAnnos.setVisibility(View.VISIBLE);
            vidAnnos.setVisibility(View.INVISIBLE);
        }
        if (requestCode == GALLERY_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri videoData = data.getData();
            vidAnnos.setVideoURI(videoData);

            vidAnnos.setVisibility(View.VISIBLE);
            picAnnos.setVisibility(View.INVISIBLE);

            MediaController mediaController = new MediaController(this);
            vidAnnos.setMediaController(mediaController);
            mediaController.setAnchorView(vidAnnos);

        }

    }

    public void valitseKuva(View view){
        Intent intentKuva = new Intent(Intent.ACTION_GET_CONTENT);
        intentKuva.setType("image/*");
        startActivityForResult(intentKuva.createChooser(intentKuva, "Valitse kuva"), GALLERY_PICTURE_REQUEST);
    }

    public void valitseVideo(View view){
        Intent intentVideo = new Intent(Intent.ACTION_GET_CONTENT);
        intentVideo.setType("video/*");
        startActivityForResult(intentVideo.createChooser(intentVideo, "Valitse video"), GALLERY_VIDEO_REQUEST);
    }

    public void lisaaArvostelu(String kaupunki,String otsikko, String pisteet, String ravintola, String teksti, String tag){

        // Siinä on nyt asetettu arvot mutta eihän se ole kuin lukea nuista kentistä ja korvata arvot niillä
        String var = mDatabase.push().getKey();
        String kuvaUrl = "https://img.devrant.com/devrant/rant/r_1973724_9QTSY.jpg";
        String peukut = "0";
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
        mDatabase.child("Arvostelut").child(var).child("Tags").push().setValue(tag);
    }

    public void lisaaTagi(String tagi){
        mDatabase.child("Tags").push().setValue(tagi);
    }

    public void lisaaKaupunki(String kaupunki){
        mDatabase.child("Kaupungit").child(kaupunki).push().setValue("Valitse");
        haeKaupungit();
    }
    public void lisaaRavintola(String kaupunki, String ravintola){
        mDatabase.child("Kaupungit").child(kaupunki).push().setValue(ravintola);
    }

    public void haeKaupungit(){
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
                            kaupunkiList.add("Lisää kaupunki");
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

    public void haeRavintolat(final String kaupunki){

        ravintolaList.clear();
        ravintolaList.add("Valitse");
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
                            ravintolaList.add("Lisää ravintola");
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

    public void haeTagit(){
        tagiList.add("Valitse");
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
                            }
                            tagiList.add("Lisää tagi");
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

    public void popUpKaupunki(String otsikko){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(otsikko);
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newKaupunki = input.getText().toString();
                lisaaKaupunki(newKaupunki);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    public void popUpRavintola(String otsikko){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(otsikko);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newRavintola = input.getText().toString();
                lisaaRavintola(spinnerKaupunki.getSelectedItem().toString(),newRavintola);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
}
